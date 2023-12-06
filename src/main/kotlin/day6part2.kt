fun day6part2() {

    // t - total time
    // d - distance to beat
    // ht - hold time
    // rt - release time
    // ht + rt = t

    // n - amount of attempts
    // n = t

    // win condition:
    // ht * rt > d

    val numberRegex = "[0-9]+".toRegex()

    val (timeLine, distanceLine) = getResourceAsText("day_6").lines()
    val time = numberRegex.findAll(timeLine).map { it.value }.joinToString("").toLong()
    val distance = numberRegex.findAll(distanceLine).map { it.value }.joinToString("").toLong()

    println("+==== $time - $distance =====+")

    var numberOfWaysToBeat = 0;
    for (ht in 1..<time) {
        val rt = time - ht
        val d = rt * ht
        if (d > distance) {
            numberOfWaysToBeat++
        }
    }

    println("+==== $numberOfWaysToBeat =====+")
}


