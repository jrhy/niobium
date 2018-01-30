package niobium

data class Tuple4<A,B,C,D>(val a: A, val b: B, val c:C, val d:D) {
    fun <U> append(r: U) = Tuple5(a,b,c,d,r)
}
data class Tuple5<A,B,C,D,E>(val a: A, val b: B, val c:C, val d:D, val e:E) {
    fun <U> append(r: U) = Tuple6(a,b,c,d,e,r)
}
data class Tuple6<A,B,C,D,E,F>(val a: A, val b: B, val c:C, val d:D, val e:E, val f:F) {
    fun <U> append(r: U) = Tuple7(a,b,c,d,e,f,r)
}
data class Tuple7<A,B,C,D,E,F,G>(val a: A, val b: B, val c:C, val d:D, val e:E, val f:F, val g:G) {
    fun <U> append(r: U) = Tuple8(a,b,c,d,e,f,g,r)
}
data class Tuple8<A,B,C,D,E,F,G,H>(val a: A, val b: B, val c:C, val d:D, val e:E, val f:F, val g:G, val h:H) {
    fun <U> append(r: U) = Tuple9(a,b,c,d,e,f,g,h,r)
}
data class Tuple9<A,B,C,D,E,F,G,H,I>(val a: A, val b: B, val c:C, val d:D, val e:E, val f:F, val g:G, val h:H, val i:I) {
    fun <U> append(r: U) = Tuple10(a,b,c,d,e,f,g,h,i,r)
}
data class Tuple10<A,B,C,D,E,F,G,H,I,J>(val a: A, val b: B, val c:C, val d:D, val e:E, val f:F, val g:G, val h:H, val i:I, val j:J)
