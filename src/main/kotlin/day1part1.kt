fun day1part1() {
    val result = getResourceAsText("day_1")
        .lines()
        .map { "[0-9]".toRegex().findAll(it).map { matchResult -> matchResult.value }.toList() }
        .map {
            val first = it.first()
            val last = it.last()

            (first + last).toInt()
        }
        .reduce { acc, i -> acc + i }

    println(result)
}


