const val numUniqueCharsInStartOfPacketMarker = 4
const val numUniqueCharsInStartOfMessageMarker = 14

fun main() {
    fun findEndPositionOfMarker(input: String, numUniqueCharsInStartMarker: Int): Int {
        var mostRecentUniqueChars = mutableListOf<Char>()
        input.forEachIndexed { index, c ->
            val indexOfChar = mostRecentUniqueChars.indexOf(c)
            if (indexOfChar >= 0) {
                // remove up to the first occurrence of it
                mostRecentUniqueChars = mostRecentUniqueChars.subList(indexOfChar + 1, mostRecentUniqueChars.size)
            }
            mostRecentUniqueChars += c

            if (mostRecentUniqueChars.size == numUniqueCharsInStartMarker) {
                return index + 1
            }
        }
        return -1
    }

    fun part1(input: String): Int {
        return findEndPositionOfMarker(input, numUniqueCharsInStartOfPacketMarker)
    }

    fun part2(input: String): Int {
        return findEndPositionOfMarker(input,  numUniqueCharsInStartOfMessageMarker)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput.first()) == 7)
    check(part2(testInput.first()) == 19)

    val input = readInput("Day06")
    println(part1(input.first()))
    println(part2(input.first()))
}
