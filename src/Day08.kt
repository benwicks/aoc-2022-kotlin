fun main() {
    fun constructTreeGrid(input: List<String>) = arrayOf<IntArray>(
            *input.map { it.map { char -> char.code - 48 }.toIntArray() }.toTypedArray()
    )

    fun isVisibleFromLeft(treeGrid: Array<IntArray>, x: Int, y: Int): Boolean {
        val interiorTreeHeight = treeGrid[y][x]
        val rowOverToTree = treeGrid[y].take(x)
        return rowOverToTree.all { it < interiorTreeHeight }
    }

    fun isVisibleFromRight(treeGrid: Array<IntArray>, x: Int, y: Int): Boolean {
        val row = treeGrid[y]
        val interiorTreeHeight = row[x]
        val rowOverToTree = row.takeLast(row.size - x - 1) // .reversed()
        return rowOverToTree.all { it < interiorTreeHeight }
    }

    fun isVisibleFromTop(treeGrid: Array<IntArray>, x: Int, y: Int): Boolean {
        val interiorTreeHeight = treeGrid[y][x]
        val columnDownToTree = treeGrid.map { it[x] }.take(y)
        return columnDownToTree.all { it < interiorTreeHeight }
    }

    fun isVisibleFromBottom(treeGrid: Array<IntArray>, x: Int, y: Int): Boolean {
        val interiorTreeHeight = treeGrid[y][x]
        val columnSize = treeGrid.size
        val columnUpToTree = treeGrid.map { it[x] }.takeLast(columnSize - y - 1) // .reversed()
        return columnUpToTree.all { it < interiorTreeHeight }
    }

    fun part1(input: List<String>): Int {
        val width = input.first().length
        val height = input.size

        val perimeterTreesVisible = width*2 + height*2 - 4

        // how many trees are visible from outside the grid?
        var interiorTreesVisible = 0

        val treeGrid = constructTreeGrid(input)

        // iterate over interior trees
        for (y in 1..width - 2) {
            for (x in 1..height - 2) {
                if (isVisibleFromLeft(treeGrid, x, y)) {
                    interiorTreesVisible++
                } else if (isVisibleFromRight(treeGrid, x, y)) {
                    interiorTreesVisible++
                } else if (isVisibleFromTop(treeGrid, x, y)) {
                    interiorTreesVisible++
                } else if (isVisibleFromBottom(treeGrid, x, y)) {
                    interiorTreesVisible++
                }
            }
        }

        return perimeterTreesVisible + interiorTreesVisible
    }

    fun calculateScenicScore(treeGrid: Array<IntArray>, x: Int, y: Int): Int {
        val row = treeGrid[y]
        val columnSize = treeGrid.size

        val treeHeight = row[x]

        val rowLeftOfTree = row.take(x).reversed()
        val rowRightOfTree = row.takeLast(row.size - x - 1)
        val columnAboveTree = treeGrid.map { it[x] }.take(y).reversed()
        val columnBelowTree = treeGrid.map { it[x] }.takeLast(columnSize - y - 1)

        val indexOfFirstTreeVisibleLeft = rowLeftOfTree.indexOfFirst { it >= treeHeight }
        val numTreesVisibleLeft = if (indexOfFirstTreeVisibleLeft == -1) { rowLeftOfTree.size } else { indexOfFirstTreeVisibleLeft + 1 }
        val indexOfFirstTreeVisibleRight = rowRightOfTree.indexOfFirst { it >= treeHeight }
        val numTreesVisibleRight = if (indexOfFirstTreeVisibleRight == -1) { rowRightOfTree.size } else { indexOfFirstTreeVisibleRight + 1 }
        val indexOfFirstTreeVisibleUp = columnAboveTree.indexOfFirst { it >= treeHeight }
        val numTreesVisibleUp = if (indexOfFirstTreeVisibleUp == -1) { columnAboveTree.size } else { indexOfFirstTreeVisibleUp + 1 }
        val indexOfFirstTreeVisibleDown = columnBelowTree.indexOfFirst { it >= treeHeight }
        val numTreesVisibleDown = if (indexOfFirstTreeVisibleDown == -1) { columnBelowTree.size } else { indexOfFirstTreeVisibleDown + 1 }

        return numTreesVisibleLeft * numTreesVisibleRight * numTreesVisibleUp * numTreesVisibleDown
    }

    fun part2(input: List<String>): Int {
        // find the highest possible scenic score
        val width = input.first().length
        val height = input.size

        var maxScenicScore = 0
        val treeGrid = constructTreeGrid(input)

        for (y in 1..width - 2) {
            for (x in 1..height - 2) {
                val scenicScore = calculateScenicScore(treeGrid, x, y)
                if (scenicScore > maxScenicScore) {
                    maxScenicScore = scenicScore
                }
            }
        }

        return maxScenicScore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
