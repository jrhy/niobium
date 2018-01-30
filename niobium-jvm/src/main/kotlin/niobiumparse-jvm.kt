package niobium

@JvmName("and2")
infix fun <J,K,U> Parser<Pair<J,K>>.and(right: Parser<U>): Parser<Triple<J,K,U>> =
    andParser(this, right, { l, r -> Triple(l.first, l.second, r) })
@JvmName("and3")
infix fun <J,K,L,U> Parser<Triple<J,K,L>>.and(right: Parser<U>): Parser<Tuple4<J,K,L,U>> =
    andParser(this, right, { l, r -> Tuple4(l.first, l.second, l.third, r) })
@JvmName("and4")
infix fun <J,K,L,M,U> Parser<Tuple4<J,K,L,M>>.and(right: Parser<U>): Parser<Tuple5<J,K,L,M,U>> =
    andParser(this, right, { l, r -> l.append(r) })
@JvmName("and5")
infix fun <J,K,L,M,N,U> Parser<Tuple5<J,K,L,M,N>>.and(right: Parser<U>): Parser<Tuple6<J,K,L,M,N,U>> =
    andParser(this, right, { l, r -> l.append(r) })
@JvmName("and6")
infix fun <J,K,L,M,N,O,U> Parser<Tuple6<J,K,L,M,N,O>>.and(right: Parser<U>): Parser<Tuple7<J,K,L,M,N,O,U>> =
    andParser(this, right, { l, r -> l.append(r) })
@JvmName("and7")
infix fun <J,K,L,M,N,O,P,U> Parser<Tuple7<J,K,L,M,N,O,P>>.and(right: Parser<U>): Parser<Tuple8<J,K,L,M,N,O,P,U>> =
    andParser(this, right, { l, r -> l.append(r) })
@JvmName("and8")
infix fun <J,K,L,M,N,O,P,Q,U> Parser<Tuple8<J,K,L,M,N,O,P,Q>>.and(right: Parser<U>): Parser<Tuple9<J,K,L,M,N,O,P,Q,U>> =
    andParser(this, right, { l, r -> l.append(r) })
@JvmName("and9")
infix fun <J,K,L,M,N,O,P,Q,R,U> Parser<Tuple9<J,K,L,M,N,O,P,Q,R>>.and(right: Parser<U>): Parser<Tuple10<J,K,L,M,N,O,P,Q,R,U>> =
    andParser(this, right, { l, r -> l.append(r) })
