fun day3part2() {
    val grid = getResourceAsText("day_3")
        .lines()

    val numberMap = mutableMapOf<Int, MutableMap<Int, Int>>()

    val numberRegex = "[0-9]+".toRegex()
    for ((lineIndex, line) in grid.withIndex()) {
        for (result in numberRegex.findAll(line)) {
            val range = result.range
            val number = result.value.toInt()
            val map = numberMap.computeIfAbsent(lineIndex) { mutableMapOf() }
            for (index in range) {
                map[index] = number
            }
        }
    }

    var sum = 0

    for ((lineIndex, line) in grid.withIndex()) {
        for (result in "\\*".toRegex().findAll(line)) {
            val range = result.range
            val checkRange = maxOf(range.first - 1, 0)..minOf(range.last + 1, line.length - 1)

            val indexOfLineAbove = lineIndex - 1
            val indexOfLineBelow = lineIndex + 1

            val lineAbove = if (indexOfLineAbove < 0) "" else grid[indexOfLineAbove].substring(checkRange)
            val lineBelow = if (indexOfLineBelow > (grid.size - 1)) "" else grid[indexOfLineBelow].substring(checkRange)
            val lineSame = line.substring(checkRange)

            val showString = lineAbove + System.lineSeparator() + lineSame + System.lineSeparator() + lineBelow
            println(showString)

            val numbersAbove = numberRegex
                .findAll(lineAbove)
                .map { matchResult -> numberMap[indexOfLineAbove]!![checkRange.first + matchResult.range.first]
                }
                .toList()

            val numbersSame = numberRegex
                .findAll(lineSame)
                .map { matchResult -> numberMap[lineIndex]!![checkRange.first + matchResult.range.first] }
                .toList()

            val numbersBelow = numberRegex
                .findAll(lineBelow)
                .map { matchResult -> numberMap[indexOfLineBelow]!![checkRange.first + matchResult.range.first] }
                .toList()

            val numbers = (numbersAbove + numbersSame + numbersBelow).filterNotNull()

            if (numbers.size == 2) {
                val (num1, num2) = numbers

                println("+==== $num1 ==== $num2 ====+")

                val gearRation = num1 * num2

                sum += gearRation
            }

            println("+=============+")
        }
    }

    println(sum)
}


