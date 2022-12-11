fun main() {
    // 20th, 60th, 100th, 140th, 180th, and 220th cycles
    val interestingPositions = generateSequence(20) { it + 40 }.take(6).toList()

    fun isInterestingCycle(cycleNumber: Int): Boolean {
        return cycleNumber in interestingPositions
    }

    fun part1(input: List<String>): Int {
        var sumOfCycleNumberAndSignalStrengthProducts = 0
        // sum every 20th signal strength * its cycle #
        var cycleNumber = 0
        var registerXValue = 1
        for (operation in input) {
            if (operation == "noop") {
                cycleNumber++
                if (isInterestingCycle(cycleNumber)) {
                    sumOfCycleNumberAndSignalStrengthProducts += (cycleNumber * registerXValue)
                }
            } else {
                cycleNumber++
                if (isInterestingCycle(cycleNumber)) {
                    sumOfCycleNumberAndSignalStrengthProducts += (cycleNumber * registerXValue)
                }
                cycleNumber++
                if (isInterestingCycle(cycleNumber)) {
                    sumOfCycleNumberAndSignalStrengthProducts += (cycleNumber * registerXValue)
                }
                registerXValue += operation.split(" ")[1].toInt()
            }
        }
        // Find the signal strength during the 20th, 60th, 100th, 140th, 180th, and 220th cycles.
        // What is the sum of these six signal strengths?
        return sumOfCycleNumberAndSignalStrengthProducts
    }

    fun isSpriteInCurrentDrawPixel(cycleIndex: Int, registerXValue: Int) = cycleIndex % 40 in registerXValue - 1..registerXValue + 1

    fun part2(input: List<String>) {
        // It seems like the X register controls the horizontal position of a sprite. Specifically, the sprite is 3 pixels wide, and the X register sets the horizontal position of the middle of that sprite.
        // (In this system, there is no such thing as "vertical position": if the sprite's horizontal position puts its pixels where the CRT is currently drawing, then those pixels will be drawn.)
        // If the sprite is positioned such that one of its three pixels is the pixel currently being drawn, the screen produces a lit pixel (#); otherwise, the screen leaves the pixel dark (.).
        var cycleIndex = 0
        var registerXValue = 1
        val imageOutputPixels = mutableListOf<Boolean>()

        for (operation in input) {
            if (operation == "noop") {
                imageOutputPixels += isSpriteInCurrentDrawPixel(cycleIndex, registerXValue)
                cycleIndex++

            } else {
                imageOutputPixels += isSpriteInCurrentDrawPixel(cycleIndex, registerXValue)
                cycleIndex++
                imageOutputPixels += isSpriteInCurrentDrawPixel(cycleIndex, registerXValue)
                cycleIndex++
                registerXValue += operation.split(" ")[1].toInt()
            }
        }

        // Render the image given by your program. What eight capital letters appear on your CRT?
        imageOutputPixels.chunked(40).forEach { line ->
            println(line.map { if (it) '#' else '.' }.joinToString(""))
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13_140)
    println(part2(testInput))

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
