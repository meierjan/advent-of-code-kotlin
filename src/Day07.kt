import java.lang.Math.abs
import java.lang.Math.min
import kotlin.math.cos

fun calcPrice(file: String, cost: (Int) -> Int): Int {
	val input = readInput(file).first()
		.split(",")
		.map { it.toInt() }

	val costs = (1..input.maxOf { it })
		.map { targetPos ->
			input.sumOf { currentPost -> cost(abs(targetPos - currentPost)) }
		}

	return costs.minOf { it }

}

fun main() {

	val minCostTest1 = calcPrice("Day07_test") { it }
	println("Min cost ${minCostTest1}")

	val minCost1 = calcPrice("Day07") { it }
	println("Min cost ${minCost1}")

	val minCostTest2 = calcPrice("Day07_test") { (1..it).sumOf { it } }
	println("Min cost ${minCostTest2}")

	val minCost2 = calcPrice("Day07") { (1..it).sumOf { it } }
	println("Min cost ${minCost2}")

}