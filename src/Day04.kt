fun main() {
    fun extracted(pair: String): Pair<IntRange, IntRange> {
        val split = pair.split(',')
        val firstRange = split[0].split('-')
        val secondRange = split[1].split('-')

        val firstRangeStart = firstRange.first().toInt()
        val secondRangeStart = secondRange.first().toInt()
        val firstRangeEnd = firstRange.last().toInt()
        val secondRangeEnd = secondRange.last().toInt()

        return firstRangeStart..firstRangeEnd to secondRangeStart..secondRangeEnd
    }

    fun doesOneRangeFullyContainTheOther(parsedRangesPair: Pair<IntRange, IntRange>) =
            (parsedRangesPair.first.first <= parsedRangesPair.second.first && parsedRangesPair.first.last >= parsedRangesPair.second.last) ||
                    (parsedRangesPair.first.first >= parsedRangesPair.second.first && parsedRangesPair.first.last <= parsedRangesPair.second.last)

    fun part1(input: List<String>): Int {
        var sumFullyContainedRangesByPair = 0
        for (pair in input) {
            val parsedRangesPair = extracted(pair)

            // does one fully contain the other?
            if (doesOneRangeFullyContainTheOther(parsedRangesPair)) {
                sumFullyContainedRangesByPair++
            }
        }
        return sumFullyContainedRangesByPair
    }

    fun part2(input: List<String>): Int {
        var sumOverlappingRangesByPair = 0
        for (pair in input) {
            val parsedRangesPair = extracted(pair)

            // does one fully or partially contain the other?
            if (doesOneRangeFullyContainTheOther(parsedRangesPair) ||
                    parsedRangesPair.first.first in parsedRangesPair.second || parsedRangesPair.first.last in parsedRangesPair.second) {
                sumOverlappingRangesByPair++
            }
        }
        return sumOverlappingRangesByPair
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
