import java.lang.NullPointerException

fun day1part2() {
    val numbers = (0..9).map { it.toString() to it }
        .toList()

    val stringNumbers = listOf(
        "zero" to 0,
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )

    val result = getResourceAsText("day_1")
        .lines()
        .map { line ->

            val (indexN, valueN) = numbers
                .map { (str, num) -> line.indexOf(str) to num }
                .filter { (index, _) -> index != -1 }
                .minByOrNull { (index, _) -> index }
                ?: (-1 to null)

            val (indexS, valueS) = stringNumbers
                .map { (str, num) -> line.indexOf(str) to num }
                .filter { (index, _) -> index != -1 }
                .minByOrNull { (index, _) -> index }
                ?: (-1 to null)

            val (lastIndexN, lastValueN) = numbers
                .map { (str, num) -> line.lastIndexOf(str) to num }
                .filter { (index, _) -> index != -1 }
                .maxByOrNull { (index, _) -> index }
                ?: (-1 to null)

            val (lastIndexS, lastValueS) = stringNumbers
                .map { (str, num) -> line.lastIndexOf(str) to num }
                .filter { (index, _) -> index != -1 }
                .maxByOrNull { (index, _) -> index }
                ?: (-1 to null)

            println("$line, first - StrInd: $indexS \tStrVal: $valueS - \tNumInd: $indexN \tNumVal: $valueN")
            println("$line, last  - StrInd: $lastIndexS \tStrVal: $lastValueS - \tNumInd: $lastIndexN \tNumVal: $lastValueN")

            val value = if (valueS == null) {
                valueN
            } else if (valueN == null) {
                valueS
            } else if (indexS < indexN) {
                valueS
            } else {
                valueN
            }

            val lastValue = if (lastValueS == null) {
                lastValueN
            } else if (lastValueN == null) {
                lastValueS
            } else if (lastIndexS > lastIndexN) {
                lastValueS
            } else {
                lastValueN
            }

            println("$line first - $value")
            println("$line last  - $lastValue")

            if (value == null || lastValue == null) {
                throw NullPointerException()
            }

            val num = value * 10 + lastValue

            println("+====== $num =======+")

            num
        }
        .reduce { acc, i -> acc + i }

    println(result)
}
