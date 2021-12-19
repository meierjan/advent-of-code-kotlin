import java.lang.IllegalArgumentException

data class Command(
	val type: String,
	val amount: Int
)

data class Position(
	val horizontal: Int = 0,
	val depth: Int = 0
) {
	val result: Int = horizontal * depth
}

fun main() {
	fun part1(input: List<String>): Position {
		val cmds = input.map {
			val (cmd, amount) = it.split(" ")
			Command(cmd, amount.toInt())
		}

		return cmds.fold(Position()) { acc, command ->
			when (command.type) {
				"forward" -> acc.copy(horizontal = acc.horizontal + command.amount)
				"down" -> acc.copy(depth = acc.depth + command.amount)
				"up" -> acc.copy(depth = acc.depth - command.amount)
				else -> throw IllegalArgumentException("invalid input type ${command.type}")
			}
		}
	}

	fun part2(input: List<String>): Int {
		return 0
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day02_test")
	println(part1(testInput))
	check(part1(testInput) == Position(15, 10))

	val input = readInput("Day02")
	println(part1(input))
	println(part2(input))
}
