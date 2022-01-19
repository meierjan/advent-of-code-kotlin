import SnailFishNumber.Number
import SnailFishNumber.Pair
import kotlin.math.ceil
import kotlin.math.floor

sealed class SnailFishNumber {

    abstract var parent: SnailFishNumber?

    data class Number(
        var number: Long,
        override var parent: SnailFishNumber? = null
    ) : SnailFishNumber() {
        fun plus(value: Long) {
            number += value
        }

        override fun toString(): String {
            return number.toString()
        }

    }

    data class Pair(
        var left: SnailFishNumber,
        var right: SnailFishNumber,
        override var parent: SnailFishNumber? = null
    ) : SnailFishNumber() {
        override fun toString(): String {
            return "[$left,$right]"
        }
    }
}

operator fun SnailFishNumber.plus(b: SnailFishNumber?): SnailFishNumber {
    if (b == null) {
        return this
    }

    val additionResult = Pair(
        this,
        b
    )
    additionResult.left.parent = additionResult
    additionResult.right.parent = additionResult
    return additionResult

}

fun SnailFishNumber.magnitude(): Long =
    when (this) {
        is Number -> this.number
        is Pair -> 3 * this.left.magnitude() + 2 * this.right.magnitude()
    }

fun SnailFishNumber.reduce(): SnailFishNumber {
    while (reduceSailfishNumber(this)) {
    }
    return this
}

fun SnailFishNumber.Number.split(): Pair {
    val splitValue = Pair(
        left = Number(floor(number / 2f).toLong()),
        right = Number(ceil(number / 2f).toLong()),
        parent = parent
    )

    splitValue.left.parent = splitValue
    splitValue.right.parent = splitValue

    return splitValue
}

private fun goUpFindLeft(pair: SnailFishNumber?): Pair? =
    if (pair?.parent == null)
        null
    else if ((pair.parent as Pair).left !== pair)
        pair.parent as Pair
    else
        goUpFindLeft(pair.parent)


private fun goUpFindRight(pair: SnailFishNumber?): Pair? =
    if (pair?.parent == null)
        null
    else if ((pair.parent as Pair).right !== pair)
        pair.parent as Pair
    else
        goUpFindRight(pair.parent)


private fun findLeft(number: SnailFishNumber?): Number? =
    when (number) {
        null -> null
        is Number -> number
        is Pair -> when (val left = number.left) {
            is Number -> left
            is Pair -> findLeft(left)
        }
    }

private fun findRight(number: SnailFishNumber?): Number? =
    when (number) {
        null -> null
        is Number -> number
        is Pair -> when (val right = number.right) {
            is Number -> right
            is Pair -> findRight(right)
        }
    }


private fun reduceSnailfishNumberByExplode(current: SnailFishNumber, depth: Long = 0): Boolean {
    // we know this can never work
    if (depth > 4) return false

    return when {
        current is Pair && depth == 4L -> {
            val (left, right) = current

            val nextLeft = findRight(goUpFindLeft(left)?.left)
            val nextRight = findLeft(goUpFindRight(right)?.right)

            nextLeft?.plus((left as Number).number)
            nextRight?.plus((right as Number).number)

            val parent = current.parent as Pair
            if (parent.left === current) {
                parent.left = Number(0, parent)
            } else {
                parent.right = Number(0, parent)
            }
            true
        }
        else ->
            when (current) {
                is Number -> false
                is Pair -> reduceSnailfishNumberByExplode(current.left, depth + 1) ||
                        reduceSnailfishNumberByExplode(current.right, depth + 1)
            }


    }
}

private fun reduceSnailfishNumberBySplit(current: SnailFishNumber): Boolean {
    return when {
        // split
        current is Number -> {
            val matches = current.number >= 10
            if(matches) {
                val parent = current.parent as Pair
                val splitPair = current.split()
                if (parent.right === current) {
                    parent.right = splitPair
                } else {
                    parent.left = splitPair
                }
                true
            } else {
                false
            }
        }
        else -> reduceSnailfishNumberBySplit((current as Pair).left) || reduceSnailfishNumberBySplit((current as Pair).right)
    }

}


fun reduceSailfishNumber(current: SnailFishNumber): Boolean {
    return reduceSnailfishNumberByExplode(current) || reduceSnailfishNumberBySplit(current)
}

fun findMiddleComma(input: String): Int {
    var bracketCount = 0
    for ((index, char) in input.withIndex()) {
        if (char == ']') bracketCount--
        else if (char == '[') bracketCount++
        else if (char == ',' && bracketCount == 1) return index
        else continue
    }
    throw IllegalArgumentException("there is no comma")
}

fun parseSailfishNumber(input: String): SnailFishNumber =
    if (!input.contains(",")) {
        Number(input.toLong())
    } else {
        val center = findMiddleComma(input)
        val a = input.substring(1, center)
        val b = input.substring(center + 1, input.length - 1)

        Pair(parseSailfishNumber(a), parseSailfishNumber(b)).apply {
            left.parent = this
            right.parent = this
        }
    }



fun main() {


    val x = readInput("Day18")
        .map { parseSailfishNumber(it) }
        .reduce { acc, snailFishNumber -> (acc + snailFishNumber).reduce() }

    println(x.magnitude())


}



