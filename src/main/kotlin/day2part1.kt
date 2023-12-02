fun day2part1() {
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
        .filter { (_, sets) ->
            val notPossible = sets.any { set ->
                val notPossible = set.any { (num, color) ->
                    val notPossible = when (color) {
                        "red" -> num > 12
                        "green" -> num > 13
                        "blue" -> num > 14
                        else -> throw RuntimeException("unreachable")
                    }

                    notPossible
                }

                notPossible
            }

            !notPossible
        }
        .map { (gameId, _) -> gameId }
        .reduce { acc, gameId -> acc + gameId }

    println(gameIdSum)
}


