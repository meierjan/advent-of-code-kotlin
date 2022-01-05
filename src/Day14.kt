import kotlin.time.ExperimentalTime

val REPEAT = 40

fun List<Char>.applyRules(insertionRules: Map<Pair<Char, Char>, Char>) =
    this.flatMapIndexed { index, currentElement ->
        val nextElementInList = this.getOrNull(index + 1)
        listOf(currentElement, insertionRules[Pair(currentElement, nextElementInList)])
    }.filterNotNull()


fun solvePart1() {
    val input = readInput("Day14")

    val template = input.first().toList()
    val insertionRules = input.drop(2)
        .associate {
            val (key, value) = it.split(" -> ")
            (key[0] to key[1]) to value.first()
        }

    var currentList = template

    repeat(REPEAT) { i ->
        currentList = currentList.applyRules(insertionRules)
    }

    println(currentList)

    val elementOccurrences = currentList.groupBy { it }
        .mapValues { it.value.size }
        .toList()
        .sortedBy { it.second }

    println(elementOccurrences)


    val result = elementOccurrences.last().second - elementOccurrences.first().second

    println("Result $result")
}


fun Map<Pair<Char, Char>, Long>.applyRules(insertionRules: Map<Pair<Char, Char>, Char>): Map<Pair<Char, Char>, Long> {
    val counter = mutableMapOf<Pair<Char, Char>, Long>()

    this.forEach { (charPair, amount) ->
        val newChar = insertionRules[charPair]!!


        val newComb1 = Pair(charPair.first, newChar)
        val newComb2 = Pair(newChar, charPair.second)

//        println("$amount * $charPair + $newChar => $newComb1 & $newComb2")

        counter[newComb1] = (counter[newComb1] ?: 0) + amount
        counter[newComb2] = (counter[newComb2] ?: 0) + amount
    }



    return counter
}


fun solvePart2() {
    val input = readInput("Day14")

    val template = input.first().toList()
    val insertionRules = input.drop(2)
        .associate {
            val (key, value) = it.split(" -> ")
            (key[0] to key[1]) to value.first()
        }


    val windowedTemplate = template.windowed(2, 1).map { Pair(it[0], it[1]) }

    var mapOfOccurrences = windowedTemplate
        .groupBy { it }
        .mapValues { it.value.size.toLong() }

    repeat(REPEAT) {
        mapOfOccurrences = mapOfOccurrences.applyRules(insertionRules)
    }

    val occurrencesByCharacter = mapOfOccurrences
        .toList()
        // group by first character in pair
        .groupBy { it.first.first }
        // sum Occurrences per char
        .mapValues { it.value.sumOf { it.second } }
        .toMutableMap()

    // the last character is never counted as we only count pairs by first char in pair
    occurrencesByCharacter[template.last()] = occurrencesByCharacter[template.last()]!! + 1L

    val elementOccurrences = occurrencesByCharacter.toList().sortedBy { it.second }

    val result = elementOccurrences.last().second - elementOccurrences.first().second

    println(elementOccurrences)
    println("Result $result")



}

fun main() {

//    solvePart1()

    solvePart2()


}

