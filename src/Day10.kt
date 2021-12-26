import java.util.*

enum class BracketType {
    curly, square, normal, angle
}

sealed class Validation {
    data class Valid(
        val missingPart: List<BracketType>
    ) : Validation()

    data class Invalid(
        val position: Int,
        val expected: BracketType,
        val wasChar: String
    ) : Validation()
}

fun validate(lineContent: String): Validation {
    val list = lineContent.split("")
    val stack = Stack<BracketType>()
    for ((charIndex, currentBracket) in list.withIndex()) {
        when (currentBracket.trim()) {
            "(" -> stack.push(BracketType.normal)
            "[" -> stack.push(BracketType.square)
            "{" -> stack.push(BracketType.curly)
            "<" -> stack.push(BracketType.angle)
            ")" -> {
                val expectedStyle = stack.popOrNull()
                if (expectedStyle != BracketType.normal) {
                    return Validation.Invalid(charIndex, BracketType.normal, currentBracket)
                }
            }
            "]" -> {
                if (stack.popOrNull() != BracketType.square) {
                    return Validation.Invalid(charIndex, BracketType.square, currentBracket)
                }
            }
            "}" -> {
                if (stack.popOrNull() != BracketType.curly) {
                    return Validation.Invalid(charIndex, BracketType.curly, currentBracket)
                }
            }
            ">" -> {
                if (stack.popOrNull() != BracketType.angle) {
                    return Validation.Invalid(charIndex, BracketType.angle, currentBracket)
                }
            }
        }
    }

    return Validation.Valid(
        stack.map { it }
    )
}

fun main() {
    val input = readInput("Day10")

    val scores = input.map { validate(it) }
        .filterIsInstance<Validation.Valid>()
        .map {
            it.missingPart.reversed().fold(0L) { acc, now -> (acc * 5L) + now.getPart2Score() }
        }
        .sorted()

    println(scores[scores.size / 2])

//    println(scores)


}

fun BracketType.getClosingBracket(): String =
    when (this) {
        BracketType.normal -> ")"
        BracketType.square -> "]"
        BracketType.curly -> "}"
        BracketType.angle -> ">"
    }

fun BracketType.getPart1Score(): Int =
    when (this) {
        BracketType.normal -> 3
        BracketType.square -> 57
        BracketType.curly -> 1197
        BracketType.angle -> 25137
    }

fun BracketType.getPart2Score(): Int =
    when (this) {
        BracketType.normal -> 1
        BracketType.square -> 2
        BracketType.curly -> 3
        BracketType.angle -> 4
    }


fun <E> Stack<E>.popOrNull(): E? =
    if (isEmpty()) {
        null
    } else {
        pop()
    }

