import java.util.*

fun main() {
    fun part1(input: List<String>): String {
        val (stacks, steps) = parseInput(input)
        for (step in steps) {
            val (numToMove, stackFromIndex, stackToIndex) = parseStep(step)
            for (i in 1..numToMove) {
                stacks[stackToIndex].push(stacks[stackFromIndex].pop())
            }
        }
        return getTopsOfEachStack(stacks)
    }

    fun part2(input: List<String>): String {
        val (stacks, steps) = parseInput(input)
        for (step in steps) {
            val (numToMove, stackFromIndex, stackToIndex) = parseStep(step)

            val tempStack = mutableListOf<Char>()
            for (i in 1..numToMove) {
                tempStack += stacks[stackFromIndex].pop()
            }
            for (crateIndex in tempStack.size - 1 downTo 0) {
                stacks[stackToIndex].push(tempStack[crateIndex])
            }
        }
        return getTopsOfEachStack(stacks)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

fun parseInput(input: List<String>): Pair<List<Stack<Char>>, List<String>> {
    val indexOfEmptyLine = input.indexOfFirst { it.isEmpty() }

    // parse starting config
    val stacks = parseStacks(input.subList(0, indexOfEmptyLine))

    // parse & execute steps
    val steps = input.subList(indexOfEmptyLine + 1, input.size)
    return Pair(stacks, steps)
}

fun parseStacks(initialConfig: List<String>): List<Stack<Char>> {
    val numStacks = initialConfig.last().split(" ").last().toInt()

    val stacks = buildList<Stack<Char>> {
        for (i in 1..numStacks) {
            this += Stack<Char>()
        }
    }

    for (i in initialConfig.size - 2 downTo 0) {
        val row = initialConfig[i]
        for (j in row.length - 2 downTo 1 step 4) {
            val c = row[j]
            if (c != ' ') {
                stacks[((j - 1) / 4)].push(c)
            }
        }
    }

    return stacks
}

fun parseStep(step: String): Triple<Int, Int, Int> {
    val parts = step.split(" from ")
    val numToMove = parts[0].split(" ").last().toInt()

    val fromTo = parts[1].split(" to ")
    val stackFromIndex = fromTo.first().toInt() - 1
    val stackToIndex = fromTo.last().toInt() - 1
    return Triple(numToMove, stackFromIndex, stackToIndex)
}

fun getTopsOfEachStack(stacks: List<Stack<Char>>) = stacks.map { it.pop() }.joinToString("")
