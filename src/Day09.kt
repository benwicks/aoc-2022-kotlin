import kotlin.math.abs

fun main() {
    fun parseMoves(input: List<String>): List<Pair<Char, Int>> {
        val headMoves = input.map {
            val pair = it.split(" ").take(2)
            Pair(pair[0][0], pair[1].toInt())
        }
        return headMoves
    }

    fun getLocationsVisitedFromMoves(headMoves: List<Pair<Char, Int>>): List<Pair<Int, Int>> {
        val headLocationsVisited = mutableListOf(0 to 0)

        for (move in headMoves) {
            var previousHeadX = headLocationsVisited.last().first
            var previousHeadY = headLocationsVisited.last().second
            val spacesToMove = move.second
            val directionToMove = move.first

            for (step in 1..spacesToMove) {
                val newHeadLocation: Pair<Int, Int> = when (directionToMove) {
                    'R' -> (previousHeadX + 1) to previousHeadY
                    'L' -> (previousHeadX - 1) to previousHeadY
                    'U' -> previousHeadX to (previousHeadY + 1)
                    else -> previousHeadX to (previousHeadY - 1)
                }
                previousHeadX = newHeadLocation.first
                previousHeadY = newHeadLocation.second
                headLocationsVisited += previousHeadX to previousHeadY
            }
        }
        return headLocationsVisited
    }

    fun getTailLocationsVisited(headLocationsVisited: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        val tailLocationsVisited = mutableListOf(0 to 0)
        for (headLocation in headLocationsVisited) {
            val tailLocation = tailLocationsVisited.last()
            if (headLocation != tailLocation &&
                    (abs(tailLocation.first - headLocation.first) > 1 ||
                            abs(tailLocation.second - headLocation.second) > 1)
            ) {
                // catch tail up
                val newTailLocation = if (tailLocation.first == headLocation.first) {
                    // move vertically (up or down?)
                    val newTailY = if (headLocation.second > tailLocation.second) {
                        headLocation.second - 1
                    } else {
                        headLocation.second + 1
                    }
                    tailLocation.first to newTailY
                } else if (tailLocation.second == headLocation.second) {
                    // move horizontally (left or right?)
                    val newTailX = if (headLocation.first > tailLocation.first) {
                        headLocation.first - 1
                    } else {
                        headLocation.first + 1
                    }
                    newTailX to tailLocation.second
                } else {
                    // move diagonally
                    if (headLocation.first > tailLocation.first) { // move right
                        if (headLocation.second > tailLocation.second) { // move up
                            tailLocation.first + 1 to tailLocation.second + 1
                        } else { // move down
                            tailLocation.first + 1 to tailLocation.second - 1
                        }
                    } else { // move left
                        if (headLocation.second > tailLocation.second) { // move up
                            tailLocation.first - 1 to tailLocation.second + 1
                        } else { // move down
                            tailLocation.first - 1 to tailLocation.second - 1
                        }
                    }
                }
                tailLocationsVisited += newTailLocation
            }
        }
        return tailLocationsVisited
    }

    fun part1(input: List<String>): Int {
        val headMoves = parseMoves(input)
        // Simulate your complete hypothetical series of motions.
        // How many positions does the tail of the rope visit at least once?

        val headLocationsVisited = getLocationsVisitedFromMoves(headMoves)

        val tailLocationsVisited = getTailLocationsVisited(headLocationsVisited)

        return tailLocationsVisited.toSet().size
    }

    fun part2(input: List<String>): Int {
        val headMoves = parseMoves(input)

        // Now there are 10 knots, not 2
        var tailLocationsVisited = getTailLocationsVisited(getLocationsVisitedFromMoves(headMoves))
        for (i in 2..9) {
            tailLocationsVisited = getTailLocationsVisited(tailLocationsVisited)
        }
        return tailLocationsVisited.toSet().size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    val testInput2 = readInput("Day09_test2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
