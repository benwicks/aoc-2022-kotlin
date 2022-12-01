fun main() {
    fun part1(input: List<String>): Int {
        return getCalorieSumsPerElf(input).maxOrNull() ?: -1
    }

    fun part2(input: List<String>): Int {
        val mutableCalorieSumsPerElf = getCalorieSumsPerElf(input).toMutableList()

        var sumOfTop3Sums = 0

        for (i in 1..3) {
            val maxCalorieSum = mutableCalorieSumsPerElf.maxOrNull()
            mutableCalorieSumsPerElf.remove(maxCalorieSum)
            maxCalorieSum?.let {
                sumOfTop3Sums += maxCalorieSum
            }
        }

        return sumOfTop3Sums
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24_000)
    check(part2(testInput) == 45_000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

fun getCalorieSumsPerElf(input: List<String>): List<Int> {
    val caloriesPerElf = mutableListOf<Int>()
    var caloriesForCurrentElf = 0
    for (line in input) {
        if (line.isNotEmpty()) {
            val caloriesInSnack = line.toInt()
            caloriesForCurrentElf += caloriesInSnack
        } else {
            caloriesPerElf.add(caloriesForCurrentElf)
            caloriesForCurrentElf = 0
        }
    }

    caloriesPerElf.add(caloriesForCurrentElf)
    return caloriesPerElf
}
