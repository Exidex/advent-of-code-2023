fun day2part2() {
    val games = getResourceAsText("day_2")
        .lines()
        .associate { line ->
            val matchResult = "^Game (?<gameid>[0-9]{1,3}): (?<rest>.*)\$"
                .toRegex()
                .matchEntire(line)
                ?: throw NullPointerException(line)

            val groups = matchResult.groups
            val gameId = groups["gameid"]!!.value.toInt()
            val sets = groups["rest"]!!.value

            val map = sets.split(";")
                .map { it.trim() }
                .map { set ->
                    set.split(",")
                        .map { it.trim() }
                        .map {
                            val (num, color) = it.split(" ")

                            num.toInt() to color
                        }
                }

            gameId to map
        }

    val gameIdSum = games
        .map { (_, sets) ->
            val minimalAmountPerColor = sets.fold(mutableMapOf<String, Int>()) { acc, set ->
                val amountPerColor = set.associate { (num, color) -> color to num }

                for ((color, amount) in amountPerColor) {
                    acc.merge(color, amount, ::maxOf)
                }

                acc
            }

            minimalAmountPerColor
        }
        .map { minimalAmountPerColor ->
            val power = minimalAmountPerColor.values.reduce { acc, value -> acc * value }

            power
        }
        .reduce { acc, power -> acc + power }

    println(gameIdSum)
}


