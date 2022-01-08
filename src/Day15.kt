import java.lang.Long.min
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@Deprecated("Too slow")
fun Solve_A_Rek(input: List<List<Int>>, x: Int = 0, y: Int = 0): Long {
    val currentField = input.getOrNull(y)?.getOrNull(x)

    if (input.size - 1 == y && input.first().size - 1 == x) {
        return 0
    } else if (currentField == null) {
        return Long.MAX_VALUE
    } else {
        return currentField + min(Solve_A_Rek(input, x + 1, y), Solve_A_Rek(input, x, y + 1))
    }

}


data class Result(
    val prev: List<List<V?>>,
    val dist: List<List<Long>>
)

data class V(
    val y: Int,
    val x: Int,
) {
    override fun toString(): String {
        return "($x, $y)"
    }
}

private fun getNeighbors(y: Int, x: Int) =
    listOf(V(y + 1, x), V(y, x + 1))

fun Solve_Dijkstra(cost: List<List<Int>>): Result {


    val xSize = cost.first().size
    val ySize = cost.size

    val prev = List(ySize) { MutableList<V?>(xSize) { null } }
    val dist = List(ySize) { MutableList(xSize) { Long.MAX_VALUE } }

    dist[0][0] = 0

    val tuples = (0 until ySize).flatMap { y -> (0 until xSize).map { x -> V(y, x) } }
    val Q = tuples.toMutableList()

    while (Q.isNotEmpty()) {
        val u = Q.minByOrNull { (y, x) -> dist[y][x] }!!
        Q.remove(u)

        val neighbors = getNeighbors(u.y, u.x)
        for (v in neighbors) {
            if (!Q.contains(v)) {
                continue
            }

            val alt = dist[u.y][u.x] + cost[v.y][v.x]
            if (alt < dist[v.y][v.x]) {
                dist[v.y][v.x] = alt
                prev[v.y][v.x] = u
            }
        }
    }

    return Result(
        prev = prev,
        dist = dist
    )

}


@OptIn(ExperimentalTime::class)
fun main() {
//    val testInput = readInput("Day15_test")
    val realInput = readInput("Day15")

    val cost = realInput.map { it.toCharArray().map { it.digitToInt() } }
    val widenedCostMatrix = cost.widenMatrix(5)

    val time = measureTime {
        val (_, dist) = Solve_Dijkstra(widenedCostMatrix)
        println("Cost:" + dist.last().last())
    }

    println("Took $time ms")


}

fun List<List<Int>>.widenMatrix(times: Int) =
    (0 until times).flatMap { yRepetition ->
        this.mapIndexed { y, xList ->
            (0 until times).flatMap { xRepetition ->
                xList.map { it.increaseCost(xRepetition + yRepetition) }
            }
        }
    }

fun Int.increaseCost(by: Int): Int {
    val newCost = this + by
    return if (newCost > 9) {
        newCost - 9
    } else {
        newCost
    }
}

fun List<List<V?>>.dumpVertices() {
    this.forEach { y ->
        y.forEach { print("${it ?: "(X, X)"} ") }
        println()
    }
}

fun List<List<Long>>.dumpLong() {
    this.forEach { y ->
        y.forEach { value -> print("$value ") }
        println()
    }

}

fun List<List<Int>>.dumpInt() {
    this.forEach { y ->
        y.forEach { value -> print("$value ") }
        println()
    }
}

