fun main() {
    fun Char.toPriority(): Int {
        return if (code > 90) {
            code - 96
        } else {
            code - 38
        }
    }

    fun part1(input: List<String>): Int {
        var repeatedCharSum = 0
        for (line in input) {
            val numChars = line.length
            val halfwayPoint = numChars / 2
            val firstHalf = line.subSequence(0, halfwayPoint).toSet()
            val secondHalf = line.subSequence(halfwayPoint, numChars).toSet()
            repeatedCharSum += firstHalf.first { it in secondHalf }.toPriority()
        }
        return repeatedCharSum
    }

    fun part2(input: List<String>): Int {
        var groupBadgeSum = 0
        input.chunked(3).forEach { group ->
            val elf1 = group.first().toSet()
            val elf2 = group[1].toSet()
            val elf3 = group.last().toSet()
            groupBadgeSum += elf1.first { it in elf2 && it in elf3 }.toPriority()
        }
        return groupBadgeSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
