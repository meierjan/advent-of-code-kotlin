import readInput

fun main() {
	val input = readInput("Day08")
	val count = input.flatMap { line ->
		val (definitionPart, activePart) = line.split("|")
		activePart.split(" ")
			.filter { it.isNotBlank() }
			.map { it.trim() }
	}
		.onEach { println("'$it'") }
		.filter {  listOf(2, 4, 3, 7).contains(it.length) }
		.count()

	println(count)
}