data class FoldOperation(
    val atIndex: Int,
    val direction: Char
)

data class Coordinate(
    val x: Int,
    val y: Int,
)


fun main() {
    val input = readInput("Day13")

    val firstEmptyLine = input.indexOfFirst { it.isEmpty() }

    val pointsInMatrix = input.slice(0 until firstEmptyLine)
        .filter { it.isNotEmpty() }
        .map {
            val (x, y) = it.split(",")
            Coordinate(x.toInt(), y.toInt())
        }

    val sizeX = pointsInMatrix.maxOf { it.x } + 1
    val sizeY = pointsInMatrix.maxOf { it.y } + 1

    val foldInstructions = input.slice(firstEmptyLine until input.size)
        .filter { it.isNotEmpty() }
        .map {
            val direction = if (it.contains("x")) 'x' else 'y'
            val (_, foldIndex) = it.split("=")
            FoldOperation(foldIndex.toInt(), direction)
        }


    val matrix = MutableList(sizeY) { MutableList(sizeX) { false } }

    for (coordinate in pointsInMatrix) {
        val (x, y) = coordinate
        matrix[y][x] = true
    }


    var currentMatrix: List<List<Boolean>> = matrix
    currentMatrix.dump("Beginning")


    for (operation in foldInstructions) {
        currentMatrix = foldMatrix(currentMatrix, operation)
    }

    currentMatrix.dump("result")


}

fun foldMatrix(matrix: List<List<Boolean>>, instruction: FoldOperation): List<List<Boolean>> {

    val sizeX = if (instruction.direction == 'x') instruction.atIndex else matrix.first().size
    val sizeY = if (instruction.direction == 'y') instruction.atIndex else matrix.size

    val newMatrix = MutableList(sizeY) { MutableList(sizeX) { false } }

    newMatrix.forEachIndexed { y, list ->
        list.forEachIndexed { x, _ ->
            val mirror = if (instruction.direction == 'x') {
                matrix.getOrNull(y)?.getOrNull(instruction.atIndex * 2 - x)
            } else {
                matrix.getOrNull(instruction.atIndex * 2 - y)?.getOrNull(x)
            } ?: false

            newMatrix[y][x] = matrix[y][x] || mirror
        }
    }

    return newMatrix
}

fun List<List<Boolean>>.dump(header: String) {

    println(header)
    for (list in this) {
        for (flags in list) {
            print(
                if (flags) {
                    '#'
                } else {
                    '.'
                }
            )
        }
        println()
    }
    println()

}
