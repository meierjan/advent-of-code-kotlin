data class Trajectory(
    val velocity: Velocity,
    val trajectory: List<Point>
)


data class Point(
    val x: Int,
    val y: Int
)

data class TargetArea(
    val xStart: Int,
    val xEnd: Int,
    val yStart: Int,
    val yEnd: Int
)

data class Velocity(
    val forwardX: Int,
    val upwardY: Int
)

private fun targetAreaFromString(stringInput: String): TargetArea {
    val input = stringInput
        .drop(13)
        .split(", ")
        .flatMap {
            it.split("=")
                .drop(1)
                .map {
                    it.split("..")
                        .map { it.toInt() }
                }
        }

    return TargetArea(
        xStart = input[0][0],
        xEnd = input[0][1],
        yStart = input[1][0],
        yEnd = input[1][1]
    )
}

private fun isInTargetArea(point: Point, targetArea: TargetArea): Boolean =
    point.x in targetArea.xStart..targetArea.xEnd && point.y in targetArea.yStart..targetArea.yEnd


private fun simulate(velocity: Velocity, targetArea: TargetArea): Trajectory? {
    val startPoint = Point(0, 0)

    val trajectory = mutableListOf(startPoint)

    var currentPosition = startPoint
    var currentVelocity = velocity

    while (currentPosition.y >= targetArea.yEnd) {

        currentPosition = Point(
            x = currentPosition.x + currentVelocity.forwardX,
            y = currentPosition.y + currentVelocity.upwardY
        )

        trajectory.add(currentPosition)

        if (isInTargetArea(currentPosition, targetArea)) {
            return Trajectory(velocity, trajectory)
        }

        currentVelocity = Velocity(
            forwardX = currentVelocity.forwardX + (if (currentVelocity.forwardX == 0) 0 else if (currentVelocity.forwardX > 0) -1 else 1),
            upwardY = currentVelocity.upwardY - 1
        )

    }

    return null

}


fun main() {

    val inputString = readInput("Day17")
        .first()

    val targetArea = targetAreaFromString(inputString)


    val allMatchingTrajectories = (0..400).flatMap { y ->
        (0..400).mapNotNull { x ->
            simulate(Velocity(x, y), targetArea)
        }
    }

    println(allMatchingTrajectories.maxOf { it.trajectory.maxOf { it.y } })

}

