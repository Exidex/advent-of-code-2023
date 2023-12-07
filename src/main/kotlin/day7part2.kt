fun day7part2() {

    throw Exception("not working")

    val handsAndBids = getResourceAsText("day_7")
        .lines()
        .map { "(?<hand>[AKQJT98765432]{5}) (?<bid>[0-9]+)".toRegex().matchEntire(it)!! }
        .map { it.groups["hand"]!!.value to it.groups["bid"]!!.value.toInt() }

    val handToBids = handsAndBids.associate { it }

    val hands = handToBids.map { (hand, _) -> hand }

    val sortedByStrength = hands
        .map { it to getMaxPossible(it) }
        .sortedWith { (hando1, o1), (hando2, o2) ->
            val compare = compareHandTypes(o1, o2)

            when {
                compare < 0 -> compare
                compare == 0 -> compareHandTypes(hando1, hando2)
                compare > 0 -> compare
                else -> throw RuntimeException("unreachable")
            }
        }

    val result = sortedByStrength
        .onEach { println("${it.first} -> ${it.second}") }
        .reversed()
        .withIndex()
        .sumOf { (index, pair) ->
            val (hand, _) = pair
            val position = index + 1

//            println("$hand -> ${handToBids[hand]!!} * $position = ${handToBids[hand]!! * position}")

            handToBids[hand]!! * position
        }

    println(sortedByStrength)
    println("+=========+")
    println(handToBids)
    println("+=========+")
    println(result)
}

fun compareHandTypes(o1: String, o2: String): Int {
    val o1Type = getHandType(o1)
    val o2Type = getHandType(o2)

    val compareTo = o1Type.compareTo(o2Type)
    return when {
        compareTo < 0 -> compareTo
        compareTo == 0 -> compareHands(o1, o2)
        compareTo > 0 -> compareTo
        else -> throw RuntimeException("unreachable $compareTo")
    }
}


fun compareHands(o1: String, o2: String): Int {
    val compareList = o1.zip(o2)
        .map { (c1, c2) -> cardWithJokerFromName(c1) to cardWithJokerFromName(c2) }
        .map { (c1, c2) -> c1.compareTo(c2) }

    var result: Int? = null
    for (compare in compareList) {
        when {
            compare < 0 -> {
                result = -1
                break
            }

            compare == 0 -> continue
            compare > 0 -> {
                result = 1
                break
            }

            else -> throw RuntimeException("unreachable")
        }
    }

    return result ?: 0
}

fun getMaxPossible(hand: String): String {
    val uniqCards = hand.toSet()
        .map { uniqCard -> cardWithJokerFromName(uniqCard) to hand.filter { it == uniqCard }.length }

    var maxDuplicate = 0
    var maxDuplicateCard: CardWithJoker? = null
    for ((card, amount) in uniqCards) {
        if (amount > maxDuplicate || (maxDuplicateCard == null || card < maxDuplicateCard)) {
            maxDuplicate = amount
            maxDuplicateCard = card
        }
    }

    var replaced = hand.replace("J", maxDuplicateCard!!.value)

    if (replaced.contains('J')) {
        val biggest = hand.map { cardWithJokerFromName(it) }.min().value

        replaced = if (biggest == "J") {
            replaced.replace("J", "A")
        } else {
            replaced.replace("J", biggest)
        }
    }

    return replaced
}

enum class CardWithJoker(val value: String) {
    CardA("A"),
    CardK("K"),
    CardQ("Q"),
    CardT("T"),
    Card9("9"),
    Card8("8"),
    Card7("7"),
    Card6("6"),
    Card5("5"),
    Card4("4"),
    Card3("3"),
    Card2("2"),
    CardJ("J"),
}

fun cardWithJokerFromName(name: Char): CardWithJoker {
    for (enumValue in enumValues<CardWithJoker>()) {
        if (enumValue.value == name.toString()) {
            return enumValue
        }
    }
    throw RuntimeException("unknown: $name")
}

// 5933J -> 59333 vs 59339