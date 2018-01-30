package niobium

data class Match<T>(val match: String, val remainder: String, val value: T)

typealias Parser<T> = (s: String) -> Match<T>?

infix fun <T,U> Parser<T>.mapNotNull(transform: (T) -> U?) : Parser<U> = { s ->
    val match = this(s)
    if (match == null) {
        null
    } else {
        val transformed = transform(match.value)
        if (transformed == null)
            null
        else
            Match(match.match, match.remainder, transformed)
    }
}

infix fun <T,U> Parser<T>.map(transform: (T) -> U) : Parser<U> = { s ->
    val match = this(s)
    if (match == null)
        null
    else
        Match(match.match, match.remainder, transform(match.value))
}

infix fun <T> Parser<T>.filter(filter: (T) -> Boolean) : Parser<T> = { s ->
    val match = this(s)
    if (match != null && filter(match.value))
        match
    else
        null
}

infix fun <T> Parser<T>.or(other: Parser<T>): Parser<T> = { this(it) ?: other(it) }

infix fun <J,U> Parser<J>.and(right: Parser<U>): Parser<Pair<J,U>> =
        andParser(this, right, { l, r -> Pair(l, r) })

// used by -jvm and -js and() methods, broken out to work around
// "declarations have the same JVM signature"
fun <L,R,U> andParser(left: Parser<L>,
                      right: Parser<R>,
                      tupleAppender: (L,R) -> U)
        : Parser<U>
        = { s ->
    val first = left(s)
    val second = first?.remainder?.let { right(it) }
    if (second == null) {
        null
    } else {
        Match(first.match + second.match, second.remainder, tupleAppender(first.value, second.value))
    }
}

fun <T> multiple(parser: Parser<T>, min: Int = 0, max: Int? = null): Parser<List<T>> {
    tailrec fun matcher(s: String,
                        parser: Parser<T>,
                        min: Int = 0,
                        max: Int? = null,
                        matchSoFar: Match<List<T>>? = null)
            : Match<List<T>>? {
        return if (max == 0) {
            matchSoFar
        } else {
            val t = parser(s)
            if (t == null) {
                if (min > 0)
                    null
                else
                    matchSoFar ?: emptyListMatch(s)
            } else {
                matcher(t.remainder, parser,
                        min = if (min > 0) min - 1 else 0,
                        max = max?.minus(1),
                        matchSoFar = matchSoFar.combineMatch(Match(t.match, t.remainder, listOf(t.value))))
            }
        }
    }
    return { s: String -> matcher(s, parser, min, max) }
}

fun <T> optional(inner: Parser<T>): Parser<T?> = {
    val res : Match<out T?> = inner(it) ?: emptyMatch(it)
    res as Match<T?>
}
fun <T> zeroOrOne(inner: Parser<T?>): Parser<T?> =
        optional(inner)
fun <T> zeroOrMore(inner: Parser<T>, max: Int? = null): Parser<List<T>> =
        multiple(inner, min = 0, max = max)
fun <T> oneOrMore(inner: Parser<T>, max: Int? = null): Parser<List<T>> =
        multiple(inner, min = 1, max = max)

fun <T> emptyMatch(s: String): Match<T?> = Match("", s, null)
fun <T> emptyListMatch(s: String): Match<List<T>> = Match("", s, emptyList())

fun <T,U> separated(termParser: Parser<T>,
                            delimParser: Parser<U>,
                            min: Int = 0,
                            max: Int? = null)
        : Parser<List<T>> {
    val termList = (termParser and
            multiple((delimParser and termParser).map { it.second },
                    min = kotlin.math.max(min - 1, 0),
                    max = max?.minus(1)))
            .map { listOf(it.first) + it.second }
    if (min > 0)
        return termList
    return optional(termList).map { it ?: emptyList() }
}

fun <T> Match<List<T>>?.combineMatch(next: Match<List<T>>): Match<List<T>> {
    if (this == null)
        return next
    return Match(match + next.match, next.remainder, value + next.value)
}
