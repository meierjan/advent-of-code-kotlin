var flashCounter = 0

fun main() {
    val input = readInput("Day11")
        .map {
            it.split("")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
                .toMutableList()
        }.toMutableList()


//    input.print("Step 0")
//
//    for (i in 1..100) {
//        input.step()
//        input.print("Step $i")
//    }
//
//    println(flashCounter)

    for(round in  1..1000) {
        input.step()
        if(input.didAllFlash()) {
            println("All flashed in round $round")
            break
        }
    }

}

fun MutableList<MutableList<Int>>.step() {

    // First, the energy level of each octopus increases by 1.
    this.forEachIndexed { y, list ->
        list.forEachIndexed { x, _ ->
            this[y][x]++
            // Then, any octopus with an energy level greater than 9 flashes. This increases the energy level of all
            // adjacent octopuses by 1, including octopuses that are diagonally adjacent. If this causes an octopus to
            // have an energy level greater than 9, it also flashes. This process continues as long as new octopuses
            // keep having their energy level increased beyond 9. (An octopus can only flash at most once per step.)
            if (this[y][x] == 10) {
                this.flash(y, x)
            }
        }
    }

    // Finally, any octopus that flashed during this step has its energy level set to 0, as it used all of its
    // energy to flash.
    this.forEachIndexed { y, list ->
        list.forEachIndexed { x, _ ->
            if (this[y][x] > 9) {
                this[y][x] = 0
            }
        }
    }
}

fun MutableList<MutableList<Int>>.flash(y: Int, x: Int) {
    flashCounter++

    for (yf in y - 1..y + 1) {
        for (xf in x - 1..x + 1) {

            val pointToInc = this.getOrNull(yf)?.getOrNull(xf)
            if (pointToInc != null) {
                this[yf][xf]++

                if (this[yf][xf] == 10) {
                    this.flash(yf, xf)
                }
            }
        }
    }
}

fun MutableList<MutableList<Int>>.print(header : String) {

    val result = this.map { it.joinToString("") }.joinToString("\n")
    println("$header\n------\n$result\n\n")
}

fun MutableList<MutableList<Int>>.didAllFlash() : Boolean =
    this.all { it.all { it == 0 } }

