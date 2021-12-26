import java.util.*

data class Vertex(
    val from: String,
    val to: String
) {
    fun reverse(): Vertex =
        Vertex(to, from)
}


fun getAll(input: List<Vertex>): List<List<String>> {
    val result = Stack<List<String>>()

    val currentRoutes = Stack<List<String>>()

    currentRoutes.push(listOf("start"))

    while (currentRoutes.isNotEmpty()) {
        val currentItem = currentRoutes.pop()
        val currentPosition = currentItem.last()

        // get all possible routes
        val newVerticesToGo = input
            .filter { it.from == currentPosition || it.to == currentPosition }
            .map { if (it.to == currentPosition) it.reverse() else it }
            .filter { it.to != "start" }

        for (vertex in newVerticesToGo) {
            val isSmallCave = vertex.to.all { it.isLowerCase() } && !vertex.to.isStartOrEnd()
            val isAlreadyVisited = currentItem.contains(vertex.to)
            val hasVisitedASmallCaveTwice = currentItem.hasVisitedASmallCaveTwice()

            if (isSmallCave && isAlreadyVisited && hasVisitedASmallCaveTwice) {
                continue
            }

            val newPath = currentItem.plus(vertex.to)
            if (vertex.to == "end") {
                result.push(newPath)
            } else {
                currentRoutes.push(newPath)
            }

        }
    }
    return result.toList()

}

fun List<String>.hasVisitedASmallCaveTwice(): Boolean {
    val smallCaveList = this.filter { it.all { it.isLowerCase() } }

    return smallCaveList.size != smallCaveList.distinct().size
}

fun String.isStartOrEnd() : Boolean =
    this == "start" || this == "end"

fun String.canNotVisitTwice(): Boolean =
    this.all { it.isLowerCase() }

fun main() {
    val input = readInput("Day12")
        .map {
            val (from, to) = it.split("-")
            Vertex(from, to)
        }
    //.toSet()

   val allPaths =  getAll(input)

    allPaths.forEach { println(it) }
    println("total paths: ${allPaths.size}")


}