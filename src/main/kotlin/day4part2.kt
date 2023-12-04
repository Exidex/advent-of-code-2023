
fun day4part2() {
    val numberRegex = "[0-9]+".toRegex()

    val cardAmounts = mutableMapOf<Int, Int>()

    val cards = getResourceAsText("day_4").lines()

    for (line in cards) {
        val matchResult = "^Card +(?<cardid>[0-9]{1,3}): +(?<winning>.*) \\| +(?<ours>.*)\$"
            .toRegex()
            .matchEntire(line)
            ?: throw NullPointerException(line)

        val groups = matchResult.groups
        val cardId = groups["cardid"]!!.value.toInt()
        val winning = groups["winning"]!!.value
        val ours = groups["ours"]!!.value

        val winningNumbers = numberRegex
            .findAll(winning)
            .map { it.value.toInt() }
            .toSet()

        val ourNumbers = numberRegex
            .findAll(ours)
            .map { it.value.toInt() }
            .toSet()

        val matchingNumberAmount = winningNumbers.intersect(ourNumbers).size

        cardAmounts.compute(cardId) { _, oldValue ->
            if (oldValue == null) 1 else oldValue + 1
        }
        val amountOfCurrentCard = (cardAmounts[cardId] ?: 0)

        for (index in 1.. matchingNumberAmount) {
            val cardIndex = cardId + index
            if (cardIndex <= cards.size) {
                cardAmounts.compute(cardIndex) { _, oldValue ->
                    if (oldValue == null) amountOfCurrentCard else oldValue + amountOfCurrentCard
                }
            }
        }
    }

    val allCards = cardAmounts.values.reduce { acc, amount -> acc + amount }

    println(allCards)
}



