import kotlin.math.max
import kotlin.math.min

fun day5part2() {
    val numberRegex = "[0-9]+".toRegex()

    val lines = getResourceAsText("day_5")
        .lines()

    var currentSection: Section? = null
    val seedRanges = mutableListOf<LongRange>()
    val sectionMaps = mutableMapOf<Section, MutableList<Ranges>>()

    for (line in lines) {
        val indexOfColon = line.indexOf(':')
        if (indexOfColon >= 0) {
            val sectionName = line.substring(0..<indexOfColon)

            if (sectionName == "seeds") {
                val seedNumbers = line.substring(indexOfColon)

                numberRegex.findAll(seedNumbers)
                    .map { it.value.toLong() }
                    .chunked(2)
                    .map { (seedRangeStart, seedRangeLength) -> seedRangeStart..<(seedRangeStart + seedRangeLength) }
                    .toCollection(seedRanges)
            } else {
                currentSection = sectionFromName(sectionName)
            }
        } else if (line.isBlank()) {
            continue
        } else {
            if (currentSection == null) {
                throw RuntimeException("unreachable")
            }
            val (destinationRangeStart, sourceRangeStart, rangeLength) = numberRegex.findAll(line)
                .map { it.value.toLong() }
                .toList()

            sectionMaps[currentSection] = sectionMaps[currentSection] ?: mutableListOf()
            sectionMaps[currentSection]!!.add(Ranges(destinationRangeStart, sourceRangeStart, rangeLength))
        }
    }

    println("==== seeds =====")
    println(seedRanges)

    val results = mutableListOf<LongRange>()

    for (seedRange in seedRanges) {
        var seedRangeValues = listOf(seedRange)
        for (nextSection in sectionsOrder) {
            val sectionResult = mutableListOf<LongRange>()

            for (seedRangeValue in seedRangeValues) {
                val selectedRanges = mutableListOf<Pair<Long, LongRange>>()
                for (ranges in sectionMaps[nextSection]!!) {
                    val range = ranges.sourceRangeStart..<(ranges.sourceRangeStart + ranges.rangeLength)
                    val shiftBy = ranges.destinationRangeStart - ranges.sourceRangeStart
                    val overlapRange = range.overlap(seedRangeValue)
                    if (overlapRange != null) {
                        selectedRanges.add(shiftBy to overlapRange)
                    }
                }

                val nextSeedValue = if (selectedRanges.isEmpty()) {
                    listOf(seedRangeValue)
                } else {
                    selectedRanges.map { (shiftBy, range) -> (range.first + shiftBy)..(range.last + shiftBy) }
                }

                sectionResult.addAll(nextSeedValue)
                println("+==== $seedRangeValue - $nextSeedValue - $selectedRanges ====+")
            }

            println("+=========== $seedRangeValues - $nextSection - $sectionResult ================+")
            seedRangeValues = sectionResult
        }
        println("+==== $ ====+")
        results.addAll(seedRangeValues)
    }

    println("==== all results =====")
    println(results)

    println("==== result =====")
    println(results.minOfOrNull { it.first })

}

fun LongRange.overlap(other: LongRange): LongRange?  {
    // https://stackoverflow.com/questions/36035074/how-can-i-find-an-overlap-between-two-given-ranges

    val maxOfStarts = max(first, other.first)
    val minOfEnds = min(last, other.last)

    return if (maxOfStarts <= minOfEnds) {
        maxOfStarts..<minOfEnds
    } else {
        null
    }
}

