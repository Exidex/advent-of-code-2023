fun day6part1() {

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
    val times = numberRegex.findAll(timeLine).map { it.value.toInt() }.toList()
    val distances = numberRegex.findAll(distanceLine).map { it.value.toInt() }.toList()
    val races = times.zip(distances)

    val results = mutableListOf<Int>()
    for ((time, distance) in races) {
        var numberOfWaysToBeat = 0;
        for (ht in 1..<time) {
            val rt = time - ht
            val d = rt * ht
            if (d > distance) {
                numberOfWaysToBeat++
            }
        }
        results.add(numberOfWaysToBeat)
        println("+==== $numberOfWaysToBeat =====+")
    }

    println("==== result =====")
    println(results.reduce { acc, i -> acc * i })
}


