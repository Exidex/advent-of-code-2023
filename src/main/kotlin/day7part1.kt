fun day7part1() {

    val handsAndBids = getResourceAsText("day_7")
        .lines()
        .map { "(?<hand>[AKQJT98765432]{5}) (?<bid>[0-9]+)".toRegex().matchEntire(it)!! }
        .map { it.groups["hand"]!!.value to it.groups["bid"]!!.value.toInt() }

    val handToBids = handsAndBids.associate { it }

    val hands = handToBids.map { (hand, _) -> hand }

    val sortedByStrength = hands.sortedWith { o1, o2 ->
        val o1Type = getHandType(o1)
        val o2Type = getHandType(o2)

        val compareTo = o1Type.compareTo(o2Type)
        when {
            compareTo < 0 -> compareTo
            compareTo == 0 -> {
                val compareList = o1.zip(o2)
                    .map { (c1, c2) -> cardFromName(c1.toString()) to cardFromName(c2.toString()) }
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

                        else -> throw RuntimeException("unreachable $compareTo")
                    }
                }

                if (result == null) {
                    throw RuntimeException("unreachable")
                }

                result
            }

            compareTo > 0 -> compareTo
            else -> throw RuntimeException("unreachable $compareTo")
        }
    }

    val result = sortedByStrength
        .reversed()
        .withIndex()
        .sumOf { (index, hand) ->
            val position = index + 1

            println("$hand -> ${handToBids[hand]!!} * $position = ${handToBids[hand]!! * position}")

            handToBids[hand]!! * position
        }

    println(sortedByStrength)
    println("+=========+")
    println(handToBids)
    println("+=========+")
    println(result)
}


fun getHandType(hand: String): Type {
    val uniq = hand.toSet()

    return when (uniq.size) {
        1 -> Type.FiveOfAKind
        2 -> {
            var maxDuplicate = 0
            for (c in uniq) {
                val duplicates = hand.filter { it == c }.length
                if (duplicates > maxDuplicate) {
                    maxDuplicate = duplicates
                }
            }

            when (maxDuplicate) {
                3 -> Type.FullHouse
                4 -> Type.FourOfAKind
                else -> throw RuntimeException("unreachable")
            }
        }
        3 -> {
            var maxDuplicate = 0
            for (c in uniq) {
                val duplicates = hand.filter { it == c }.length
                if (duplicates > maxDuplicate) {
                    maxDuplicate = duplicates
                }
            }

            when (maxDuplicate) {
                2 -> Type.TwoPair
                3 -> Type.ThreeOfAKind
                else -> throw RuntimeException("unreachable")
            }
        }

        4 -> Type.OnePair
        5 -> Type.HighCard
        else -> throw RuntimeException("unreachable")
    }
}

enum class Type {
    FiveOfAKind,
    FourOfAKind,
    FullHouse,
    ThreeOfAKind,
    TwoPair,
    OnePair,
    HighCard,
}

enum class Card(val value: String) {
    CardA("A"),
    CardK("K"),
    CardQ("Q"),
    CardJ("J"),
    CardT("T"),
    Card9("9"),
    Card8("8"),
    Card7("7"),
    Card6("6"),
    Card5("5"),
    Card4("4"),
    Card3("3"),
    Card2("2")
}

fun cardFromName(name: String): Card {
    for (enumValue in enumValues<Card>()) {
        if (enumValue.value == name) {
            return enumValue
        }
    }
    throw RuntimeException("unknown: $name")
}