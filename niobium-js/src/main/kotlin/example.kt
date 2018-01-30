import kotlin.browser.document
import niobium.and
import niobium.map
import niobium.or
import niobium.pat

val aaabRecognizer = pat("aaab")
val bRecognizer = pat("b")
val intParser = pat("\\d+").map { it.value.toIntOrNull() }
fun main(args: Array<String>) {
    if (true)
		return
    val res = (aaabRecognizer and aaabRecognizer)("aaabaaabjazz").toString()
    val b = (aaabRecognizer or bRecognizer)("b")
    val c = (pat("a") or
            pat("b") or
            pat("c"))("c")
    val abc = pat("a") and pat("b") and pat("c")
    val embeddedIntParser = (pat("a*") and intParser and pat("b*"))
            .map { it.second }
    /* parses to:
                   /         \
                  *           b
                 /  \
               a    123
     */
    val parsedInt = embeddedIntParser("a123b")
    document.getElementById("foo")!!.innerHTML = res + b + c + parsedInt + abc("abcd")
}
