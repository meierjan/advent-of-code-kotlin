data class PositionCounter(
	var high: Int,
	var low: Int
)


fun main() {
	fun part1(input: List<String>): Int {
		val initVector = Array(input.size) { PositionCounter(0, 0) }
		val result = input.fold(initVector) { vector, inputLine ->
			val newVector = inputLine.mapIndexed { positionInLine, charInLine ->
				val counterForPos = vector[positionInLine]
				when (charInLine) {
					'0' -> counterForPos.copy(low = counterForPos.low + 1)
					'1' -> counterForPos.copy(high = counterForPos.high + 1)
					else -> vector[positionInLine].copy()
				}
			}

			newVector.toTypedArray()
		}

		val gamma = result.joinToString("") { if(it.high > it.low) "1" else "0" }.toInt(2)
		val epsilon = result.joinToString("") { if(it.high < it.low) "1" else "0" }.toInt(2)

		return gamma * epsilon

	}

		fun part2(input: List<String>): Int {
			return 0
		}

		// test if implementation meets criteria from the description, like:
		val testInput = readInput("Day03_test")

		part1(testInput).let {
			println(it)
			check(it == 198)
		}

//	part2(testInput).let {
//		println(it)
//	}

		val input = readInput("Day03")
		part1(input).let {
			println(it)
		}

		part2(input).let {
			println(it)
		}

	}
