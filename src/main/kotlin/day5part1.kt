import Section.*

fun day5part1() {
    val numberRegex = "[0-9]+".toRegex()

    val lines = getResourceAsText("day_5")
        .lines()

    var currentSection: Section? = null
    val seeds = mutableListOf<Long>()
    val sectionMaps = mutableMapOf<Section, MutableList<Ranges>>()

    for (line in lines) {
        val indexOfColon = line.indexOf(':')
        if (indexOfColon >= 0) {
            val sectionName = line.substring(0..<indexOfColon)

            if (sectionName == "seeds") {
                val seedNumbers = line.substring(indexOfColon)

                numberRegex.findAll(seedNumbers)
                    .map { it.value.toLong() }
                    .toCollection(seeds)
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

    val results = mutableListOf<Long>()

    for (seed in seeds) {
        var seedValue = seed
        for (nextSection in sectionsOrder) {
            var selectedRange: Ranges? = null
            for (ranges in sectionMaps[nextSection]!!) {
                val range = ranges.sourceRangeStart..<(ranges.sourceRangeStart + ranges.rangeLength)
                if (range.contains(seedValue)) {
                    if (selectedRange != null) {
                        throw RuntimeException("duplicate selectedRange")
                    }
                    selectedRange = ranges
                }
            }

            val nextSeedValue = if (selectedRange == null) {
                seedValue
            } else {
                seedValue + (selectedRange.destinationRangeStart - selectedRange.sourceRangeStart)
            }

            println("+==== $seedValue - $nextSection - $selectedRange - $nextSeedValue ====+")

            seedValue = nextSeedValue
        }
        println("+==== $ ====+")
        results.add(seedValue)
    }

    println("==== all results =====")
    println(results)

    println("==== result =====")
    println(results.min())
}

val sectionsOrder = listOf(
    SEED_TO_SOIL,
    SOIL_TO_FERTILIZER,
    FERTILIZER_TO_WATER,
    WATER_TO_LIGHT,
    LIGHT_TO_TEMPERATURE,
    TEMPERATURE_TO_HUMIDITY,
    HUMIDITY_TO_LOCATION,
)

data class Ranges(var destinationRangeStart: Long, var sourceRangeStart: Long, var rangeLength: Long)

enum class Section(val section: String) {
    SEED_TO_SOIL("seed-to-soil map"),
    SOIL_TO_FERTILIZER("soil-to-fertilizer map"),
    FERTILIZER_TO_WATER("fertilizer-to-water map"),
    WATER_TO_LIGHT("water-to-light map"),
    LIGHT_TO_TEMPERATURE("light-to-temperature map"),
    TEMPERATURE_TO_HUMIDITY("temperature-to-humidity map"),
    HUMIDITY_TO_LOCATION("humidity-to-location map"),
}

fun sectionFromName(section: String): Section {
    for (enumValue in enumValues<Section>()) {
        if (enumValue.section == section) {
            return enumValue
        }
    }
    throw RuntimeException("unknown: $section")
}