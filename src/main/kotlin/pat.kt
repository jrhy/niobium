package niobium

fun pat(regex: Regex): Parser<MatchResult> {
//class pat(val regex: Regex) : Parser<MatchResult> {
//    override fun parse(s: String): Match<MatchResult>? {
    return { s: String ->
        val result = regex.find(s, 0)
        // TODO: find a better way to fail the match on the first character ("^"?)
        if (result == null || result.range.start > 0) {
            null
        } else {
            Match(result.value, s.substring(result.range.last + 1), result)
        }
    }
}

fun pat(pattern: String): Parser<MatchResult> {
    val regex = Regex(pattern)
    return pat(regex)
}

