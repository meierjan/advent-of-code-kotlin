fun List<List<Int>>.isLowPoint(y: Int, x: Int): Boolean {
    val up = this.getOrNull(y - 1)?.getOrNull(x) ?: 9
    val down = this.getOrNull(y + 1)?.getOrNull(x) ?: 9
    val left = this.getOrNull(y)?.getOrNull(x - 1) ?: 9
    val right = this.getOrNull(y)?.getOrNull(x + 1) ?: 9

    val current = this[y][x]

    return current < up &&
            current < down &&
            current < left &&
            current < right
}



fun main() {


    val input = readInput("Day10")
        .map { it.split("").filterNot { it.isEmpty() }.map { it.toInt() } }

   val result =  input.flatMapIndexed { y: Int, list: List<Int> ->
        list.mapIndexed { x, value ->  Triple(y,x, input.isLowPoint(y, x)) }
    }

    result.filter { it.third }
        .forEach { (y,x, isTrue)  -> println("( $y, $x, $isTrue, value = ${input[y][x]} )")}

    val sum = result.filter { it.third }
        .map { (y,x,_)-> input[y][x] + 1 }
        .sum()

    println("Sum of risk levels = $sum")

}
