data class Board(
	val fields: List<List<Field>>
) {
	data class Field(
		val number: Int,
		var isCalled: Boolean = false,
		var calledInRound: Int? = null
	) {
		fun drawn(round: Int) {
			isCalled = true
			calledInRound = round
		}
	}

	var lastDraw: Field? = null

	private val unmarkedSum: Int
		get() = fields.sumOf { it.filterNot { it.isCalled }.sumOf { it.number } }

	val score: Int?
		get() {
			return if (hasBingo()) {
				unmarkedSum * lastDraw?.number!!
			} else {
				null
			}
		}

	fun numberDrawn(number: Int, round: Int) {
		fields.forEach {
			val found = it.find { it.number == number }
			if(found != null) {
				lastDraw = found
			}
			found?.drawn(round)
		}
	}

	fun hasBingo(): Boolean =
		fields.any { it.all { it.isCalled } }
				|| fields.indices.any { i -> fields.all { it[i].isCalled } }


}


fun getNumbersFromString(input: String) =
	input.split(",")
		.map { it.toInt() }

fun getBoardsFromInput(input: List<String>): List<Board> {
	return input.chunked(6)
		.map { board ->
			val rows = board.map {
				it.split(" ")
					.filter { it.isNotBlank() }
					.map { Board.Field(it.toInt()) }
			}.filterNot { it.isEmpty() }
			Board(rows)
		}
}

fun main() {
	val input = readInput("Day04")

	val numbers = getNumbersFromString(input.first())
	val boards = getBoardsFromInput(input.drop(2)).toMutableList()

	numbers.forEachIndexed { i, drawnNumber ->
		val round = i + 1
		println("Round: $round")
		println("Drawn number: $drawnNumber")
		println("Boards left: ${boards.size}")

		boards.forEach { it.numberDrawn(drawnNumber, round) }

		val boardWithBingo = boards.filter { it.hasBingo() }
		if (boardWithBingo.isNotEmpty()) {
			boardWithBingo.forEach {
			println("bingo with score '${it.score}'!")
			boards.remove(it)
			}
		}
		println("----")
	}

}