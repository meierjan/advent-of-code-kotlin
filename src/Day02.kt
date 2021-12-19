import java.lang.IllegalArgumentException

data class Command(
	val type: String,
	val amount: Int,
)

data class Position(
	val horizontal: Int = 0,
	val depth: Int = 0,
	val aim: Int = 0,
) {
	val result: Int = horizontal * depth
}

fun List<String>.toCommands(): List<Command> =
	this.map {
		val (cmd, amount) = it.split(" ")
		Command(cmd, amount.toInt())
	}

fun main() {
	fun part1(input: List<String>): Position {

		val cmds = input.toCommands()

		return cmds.fold(Position()) { acc, command ->
			when (command.type) {
				"forward" -> acc.copy(horizontal = acc.horizontal + command.amount)
				"down" -> acc.copy(depth = acc.depth + command.amount)
				"up" -> acc.copy(depth = acc.depth - command.amount)
				else -> throw IllegalArgumentException("invalid input type ${command.type}")
			}
		}
	}

	fun part2(input: List<String>): Position {
		val cmds = input.toCommands()

		return cmds.fold(Position()) { acc, command ->
			when (command.type) {
				"forward" -> acc.copy(
					depth = acc.depth + (acc.aim * command.amount),
					horizontal = acc.horizontal + command.amount,
				)
				"down" -> acc.copy(aim = acc.aim + command.amount)
				"up" -> acc.copy(aim = acc.aim - command.amount)
				else -> throw IllegalArgumentException("invalid input type ${command.type}")
			}
		}
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day02_test")

	part1(testInput).let {
		println(it)
		println(it.result)
		check(it == Position(15, 10))
	}

	part2(testInput).let {
		println(it)
		println(it.result)
		check(it.horizontal == 15 && it.depth == 60)
	}

	val input = readInput("Day02")
	part1(input).let {
		println(it)
		println(it.result)
	}

	part2(input).let {
		println(it)
		println(it.result)
	}

}
