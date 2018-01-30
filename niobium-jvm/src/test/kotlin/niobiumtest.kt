package niobium

import org.junit.Assert.*
import org.junit.Test

class NiobiumTest {

    val aParser = pat("a*")
    val bParser = pat("b*")
    val int: Parser<Int> = pat("\\d+").mapNotNull { it.value.toIntOrNull() }
    val embeddedIntParser : Parser<Int?> =
            (aParser and int and bParser).map { it.second }

    @Test fun happyCase() = assertEquals(123, embeddedIntParser("a123b")?.value)

	val intListParser : Parser<List<Int>> = separated(int, pat(",\\s*"))
	val embeddedIntListParser : Parser<List<Int?>> =
            (aParser and intListParser and bParser).map { it.second }

    val a = pat("a")
    val b = pat("b")
    val c = pat("c")
    @Test fun optional_empty() = assertEquals(null, (a and optional(b) and c)("ac")?.value?.second)
    @Test fun optional_present() = assertEquals("b", (a and optional(b) and c)("abc")?.value?.second?.value)

    @Test fun optional_withMap() {
        val ESC = "%c".format(27)
        val number = pat("\\d+") mapNotNull { it.value.toIntOrNull() }
        val vt100Csi = pat("\\x1B\\[")
        fun regularAnsiWithOptionalNumber(char: Parser<MatchResult>): Parser<Int?> =
                (vt100Csi and optional(number) and char) map { it.second }
        val eraseInLine = regularAnsiWithOptionalNumber(pat("K"))
        assertNotNull(eraseInLine("${ESC}[K"))
    }

    @Test fun zeroOrMore_zero() = assertEquals(emptyList<String>(), zeroOrMore(pat("a"))("")?.value)
    @Test fun zeroOrMore_one() = assertEquals(listOf("a"), zeroOrMore(pat("a"))("a")?.value?.map { it.value })
    @Test fun zeroOrMore_many() = assertEquals(listOf("a", "a", "a"), zeroOrMore(pat("a"))("aaa")?.value?.map { it.value })
    @Test fun zeroOrMore_max() = assertEquals(listOf("a", "a", "a"), zeroOrMore(pat("a"), max = 3)("aaaaa")?.value?.map { it.value })

    @Test fun oneOrMore_zero() = assertEquals(null, oneOrMore(pat("a"))(""))
    @Test fun oneOrMore_one() = assertEquals(listOf("a"), oneOrMore(pat("a"))("a")?.value?.map { it.value })
    @Test fun oneOrMore_many() = assertEquals(listOf("a", "a", "a"), oneOrMore(pat("a"))("aaa")?.value?.map { it.value })
    @Test fun oneOrMore_max() = assertEquals(listOf("a", "a", "a"), oneOrMore(pat("a"), max = 3)("aaaaa")?.value?.map { it.value })

    @Test fun multiple_min_unmet() = assertEquals(null, multiple(pat("a"), min = 10)("aaaaa")?.value?.map { it.value })
    @Test fun multiple_min_met() = assertEquals(listOf("a", "a", "a", "a", "a"), multiple(pat("a"), min = 3)("aaaaa")?.value?.map { it.value })
    @Test fun multiple_min_exact() = assertEquals(listOf("a", "a", "a", "a", "a"), multiple(pat("a"), min = 5)("aaaaa")?.value?.map { it.value })

    @Test fun intListParserTest_empty() = assertEquals(emptyList<Int?>(),
            embeddedIntListParser("ab")?.value)
    @Test fun intListParserTest_single() = assertEquals(listOf(123),
            embeddedIntListParser("a123b")?.value)
    @Test fun intListParserTest_multiple() = assertEquals(listOf(123, 456),
            embeddedIntListParser("a123,456b")?.value)
    @Test fun intListParserTest_afterDelimiter() = assertEquals(listOf(123),
            (aParser and intListParser and pat(",") and bParser).map { it.b }("a123,b")?.value)

    @Test fun implRejectsMatchAfterBeginning() = assertNull(pat("\n")("asdf\n"))

    interface common
    @Test fun or()  {
        data class even(val a: Int) : common
        data class odd (val b: Int) : common
        val evenParser = int filter { it % 2 == 0 } map { even(it) as common }
        val oddParser  = int filter { it % 2 == 1 } map { odd(it) as common }
        val oddOrEven = evenParser or oddParser
        assertEquals(odd(1), (oddOrEven("1")?.value))
        assertEquals(even(2), (oddOrEven("2")?.value))
    }
}
