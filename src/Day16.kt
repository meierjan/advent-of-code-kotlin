import java.util.*

fun Char.toBit(): List<Boolean> =
    when (this) {
        '0' -> "0000".toCharArray().map { it == '1' }
        '1' -> "0001".toCharArray().map { it == '1' }
        '2' -> "0010".toCharArray().map { it == '1' }
        '3' -> "0011".toCharArray().map { it == '1' }
        '4' -> "0100".toCharArray().map { it == '1' }
        '5' -> "0101".toCharArray().map { it == '1' }
        '6' -> "0110".toCharArray().map { it == '1' }
        '7' -> "0111".toCharArray().map { it == '1' }
        '8' -> "1000".toCharArray().map { it == '1' }
        '9' -> "1001".toCharArray().map { it == '1' }
        'A' -> "1010".toCharArray().map { it == '1' }
        'B' -> "1011".toCharArray().map { it == '1' }
        'C' -> "1100".toCharArray().map { it == '1' }
        'D' -> "1101".toCharArray().map { it == '1' }
        'E' -> "1110".toCharArray().map { it == '1' }
        'F' -> "1111".toCharArray().map { it == '1' }
        else -> throw UnsupportedOperationException("cannot convert non hex char $this")
    }

fun <E> Queue<E>.poll(amount: Int): List<E> =
    (0 until amount).map {
        this.poll()
    }

fun Collection<Boolean>.toInt() =
    this.map { if (it) 1 else 0 }.joinToString("").toInt(2)


// type 4
fun parseLiteral(buffer: Queue<Boolean>) {
    var parsedBits = 6 // type-id & version

    val binaryNumber = mutableListOf<Boolean>()
    while (buffer.isNotEmpty()) {
        parsedBits += 5
        val packet = buffer.poll(5)
        // is 1 if it's not the last package
        val packetHeader = packet.first()
        val payLoad = packet.drop(1)

        binaryNumber.addAll(payLoad)

        // if the packetHeader is not 1 -> last package -> stop
        if (!packetHeader) {
            break
        }
    }
    val bitesToDrop = parsedBits % 4
//    buffer.poll(bitesToDrop)
//    println(binaryNumber.toInt())
}

//
fun parseOperator(buffer: Queue<Boolean>) {
    val lengthTypeId = buffer.poll()
    // 0 -> 15-bit number  is representing a number of bit's in the sub-package
    if (lengthTypeId) {
        val numberOfSubPackages = buffer.poll(11).toInt()

        (1..numberOfSubPackages).forEach {
            parse(buffer)
        }

    } else {
        val numOfBitsInSubPackage = buffer.poll(15).toInt()
        val subPackagePayload = LinkedList(buffer.poll(numOfBitsInSubPackage))

        while (subPackagePayload.isNotEmpty()) {
            parse(subPackagePayload)
        }

    }

}


fun parse(input: String) {


    val binaryInput = input.toCharArray()
        .flatMap { it.toBit() }

    val buffer: Queue<Boolean> = LinkedList(binaryInput)


    parse(buffer)
}

var versionSum = 0
private fun parse(buffer: Queue<Boolean>) {

    val version = buffer.poll(3).toInt()
    versionSum += version
    val typeId = buffer.poll(3).toInt()

    when (typeId) {
        4 -> parseLiteral(buffer)
        else -> parseOperator(buffer)
    }
}


fun main() {
    val input = readInput("Day16").first()

//parse("A0016C880162017C3686B18A3D4780")
    parse(input)

    println(versionSum)

}