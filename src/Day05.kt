import java.lang.Integer.max
import java.lang.Integer.min

data class Line(
	val from: Position,
	val to: Position,
) {
	data class Position(
		val x: Int,
		val y: Int,
	)
}

object LineDeserializer {
	fun deserialize(input: String): Line {
		val (from, to) = input.split(" -> ")
		val (fromX, fromY) = from.split(",")
		val (toX, toY) = to.split(",")
		return Line(
			from = Line.Position(fromX.toInt(), fromY.toInt()),
			to = Line.Position(toX.toInt(), toY.toInt())
		)
	}
}


fun drawLine(matrix: List<MutableList<Int>>, line: Line, includeDiagonal : Boolean = false) {
	if (line.from.x == line.to.x) {
		val yPoints = min(line.from.y, line.to.y) .. max(line.from.y, line.to.y)
		yPoints.forEach {
//			matrix[line.from.x][it]++
			matrix[it][line.from.x]++
		}
	} else if (line.from.y == line.to.y){
		val xPoints = min(line.from.x, line.to.x) .. max(line.from.x, line.to.x)
		xPoints.forEach {
//			matrix[it][line.from.y]++
			matrix[line.from.y][it]++
		}
	} else if(includeDiagonal) {
		val seq1 = if(line.from.x > line.to.x) line.from.x downTo line.to.x else line.from.x .. line.to.x
		val seq2 = if(line.from.y > line.to.y) line.from.y downTo line.to.y else line.from.y .. line.to.y
		seq1.zip(seq2) { x,y ->
			println("$x,$y")
			matrix[y][x]++
		}
	}
}

fun drawMatrix(matrix: List<MutableList<Int>>) {
	matrix.forEach {
		it.forEach {
			print(if (it == 0) "." else it)
		}
		println()
	}
}

fun createMatrix(lines: List<Line>): List<MutableList<Int>> {
	val matrixSize =
		lines.maxOf { line ->
			max(
				max(line.from.x, line.from.y),
				max(line.to.x, line.to.y)
			)
		} + 1

	return List(matrixSize) { MutableList(matrixSize) { 0 } }
}

fun main() {
	val serializer = LineDeserializer

	val input = readInput("Day05_test")
	val lines = input.map { serializer.deserialize(it) }
	val matrix = createMatrix(lines)
	lines.forEach { drawLine(matrix, it) }
	drawMatrix(matrix)
	println("result for test:")
	println(matrix.sumOf { it.count { it > 1} })

	val input2 = readInput("Day05")
	val lines2 = input2.map { serializer.deserialize(it) }
	val matrix2 = createMatrix(lines2)
	lines2.forEach { drawLine(matrix2, it, true) }
	drawMatrix(matrix2)

	println("result for full set:")
	println(matrix2.sumOf { it.count { it > 1} })



}