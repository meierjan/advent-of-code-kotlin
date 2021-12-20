data class LanternFish(var counter: Int = 8) {
	fun tick(): LanternFish? {
		val fish: LanternFish? = if (counter == 0) {
			counter = 6
			LanternFish()
		} else {
			counter--
			null
		}

		return fish
	}
}


fun main() {

	val fish = readInput("Day06_test").first()
		.split(",")
		.map { it.toInt() }
		.map { LanternFish(it) }
		.toMutableList()

	for(i in 1..80) {
		val newFishSpawned = fish.mapNotNull { it.tick() }
		fish.addAll(newFishSpawned)
	}

	println(fish.size)

	val fish2 = readInput("Day06").first()
		.split(",")
		.map { it.toInt() }
		.map { LanternFish(it) }
		.toMutableList()

	for(i in 1..80) {
		val newFishSpawned = fish2.mapNotNull { it.tick() }
		fish2.addAll(newFishSpawned)
	}

	println(fish2.size)
}