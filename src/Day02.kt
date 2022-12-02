import kotlin.IllegalArgumentException

fun main() {
    fun calculateTotalScore(input: List<String>, selfPlayInterpretationMethod: SelfPlayInterpretationMethod): Int {
        var totalScore = 0
        for (round in input) {
            // parse plays in each round
            val opponentPlay = Play.from(round[0])
            val selfPlayCode = round[2]
            val selfPlay = when (selfPlayInterpretationMethod) {
                SelfPlayInterpretationMethod.WHAT_TO_PLAY -> Play.from(selfPlayCode)
                SelfPlayInterpretationMethod.HOW_TO_PLAY -> Play.fromHowToPlayCode(selfPlayCode, opponentPlay)
            }

            // calculate scores for each round
            val selfPlayScore = selfPlay.scoreForPlaying
            val roundOutcomeScore = selfPlay.calculateOutcome(opponentPlay).score

            // sum score of each round
            totalScore += selfPlayScore + roundOutcomeScore
        }
        return totalScore
    }

    fun part1(input: List<String>): Int {
        return calculateTotalScore(input, SelfPlayInterpretationMethod.WHAT_TO_PLAY)
    }

    fun part2(input: List<String>): Int {
        return calculateTotalScore(input, SelfPlayInterpretationMethod.HOW_TO_PLAY)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

enum class SelfPlayInterpretationMethod {
    WHAT_TO_PLAY,
    HOW_TO_PLAY
}

enum class Play(val opponentCode: Char, val selfWhatToPlayCode: Char, val scoreForPlaying: Int) {
    ROCK('A', 'X', 1),
    PAPER('B', 'Y', 2),
    SCISSORS('C', 'Z', 3);

    fun winsAgainst(): Play = when (this) {
        ROCK -> SCISSORS
        PAPER -> ROCK
        SCISSORS -> PAPER
    }

    fun losesAgainst(): Play = when (this) {
        ROCK -> PAPER
        PAPER -> SCISSORS
        SCISSORS -> ROCK
    }
    fun calculateOutcome(other: Play) = when (other) {
        this -> Outcome.DRAW
        this.winsAgainst() -> Outcome.WON
        else -> Outcome.LOST
    }

    companion object {
        fun from(playCode: Char): Play {
            for (play in Play.values()) {
                if (playCode == play.opponentCode || playCode == play.selfWhatToPlayCode) {
                    return play
                }
            }
            throw IllegalArgumentException("'$playCode' is not a valid play code.")
        }

        fun fromHowToPlayCode(selfPlayCode: Char, opponentPlay: Play): Play {
            return when (selfPlayCode) {
                'X' -> { // I need to lose
                    opponentPlay.winsAgainst()
                }
                'Y' -> { // I need to end the round in a draw
                    opponentPlay
                }
                'Z' -> { // I need to win
                    opponentPlay.losesAgainst()
                }
                else -> throw IllegalArgumentException("'$selfPlayCode' is not a valid \"how to play\" code.")
            }
        }
    }
}

enum class Outcome(val score: Int) {
    LOST(0),
    DRAW(3),
    WON(6)
}