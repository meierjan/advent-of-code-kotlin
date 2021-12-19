import java.lang.Integer.max

fun main() {
	fun part1(input: List<String>): Int {
		val inputAsIntArray = input.map { it.toInt() }.toIntArray()
		var counter = 0
		inputAsIntArray
			.forEachIndexed { index, value ->
				if (value > inputAsIntArray[max(index - 1, 0)]) {
					counter++
				}
			}
		return counter
	}

	fun part2(input: List<String>): Int {
		val result = input.map { it.toInt() }
			.windowed(3,1, false)
			.map { it.sum() }

		return part1(result.map { it.toString() })
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day01_test")
	check(part1(testInput) == 7)
	check(part2(testInput) == 5)

	val input = readInput("Day01")
	println(part1(input))
	println(part2(input))
}
