import kotlin.math.pow

fun day4part1() {
    val numberRegex = "[0-9]+".toRegex()

    val result = getResourceAsText("day_4")
        .lines()
        .map { line ->

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

            val intersect = winningNumbers.intersect(ourNumbers)

            println(cardId)
            println(winningNumbers)
            println(ourNumbers)
            println(intersect)

            val worth = 2f.pow(intersect.size - 1).toInt()

            println("+======= $worth ========+")

            worth
        }
        .reduce { acc, i -> acc + i }

    println(result)
}
