import java.math.BigInteger


fun calculateForFishAndDays(ages: List<Int>, days: Int): Long {
	val maxAge = 9

	var ageGroups = MutableList<Long>(maxAge) { 0 }

	ages.forEach { ageGroups[it]++ }

	(1..days).forEach {
		val pregnant = ageGroups.first()
		ageGroups = ageGroups.drop(1).toMutableList()
		ageGroups[6] += pregnant
		ageGroups.add(8, pregnant)
	}

	return ageGroups.sum()
}

fun main() {

	val ages = readInput("Day06").first()
		.split(",")
		.map { it.toInt() }


	val fishCount = calculateForFishAndDays(ages, 256)

	println(fishCount)
}