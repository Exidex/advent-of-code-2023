fun day3part1() {
    val grid = getResourceAsText("day_3")
        .lines()

    var sum = 0

    for ((lineIndex, line) in grid.withIndex()) {
        for (result in "[0-9]+".toRegex().findAll(line)) {
            val range = result.range
            val number = result.value.toInt()
            val checkRange = maxOf(range.first - 1, 0)..minOf(range.last + 1, line.length - 1)

            val indexOfLineAbove = lineIndex - 1
            val indexOfLineBelow = lineIndex + 1

            val lineAbove = if (indexOfLineAbove < 0) "" else grid[indexOfLineAbove].substring(checkRange)
            val lineBelow = if (indexOfLineBelow > (grid.size - 1)) "" else grid[indexOfLineBelow].substring(checkRange)
            val sameLine = line.substring(checkRange)

            val checkString = lineAbove + sameLine + lineBelow

            val showString = lineAbove + System.lineSeparator() + sameLine + System.lineSeparator() + lineBelow
            println(showString)

            val containsSymbol = checkString.contains("[^0-9.]".toRegex())

            println("+=== $containsSymbol ======= $number ===+")

            if (containsSymbol) {
                sum += number
            }
        }
    }

    println(sum)
}


