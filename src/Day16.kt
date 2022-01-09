import PacketType.EQUAL_TO
import PacketType.GREATER_THAN
import PacketType.LESS_THAN
import PacketType.LITERAL
import PacketType.MAXIMUM
import PacketType.MINIMUM
import PacketType.PRODUCT
import PacketType.SUM
import java.util.*

object PacketType {
    const val LITERAL = 4
    const val SUM = 0
    const val PRODUCT = 1
    const val MINIMUM = 2
    const val MAXIMUM = 3
    const val GREATER_THAN = 5
    const val LESS_THAN = 6
    const val EQUAL_TO = 7
}


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

fun Collection<Boolean>.toLong() =
    this.map { if (it) 1 else 0 }.joinToString("").toLong(2)

fun Collection<Boolean>.toInt() =
    this.map { if (it) 1 else 0 }.joinToString("").toInt(2)


// type 4
fun parseLiteral(buffer: Queue<Boolean>): Long {
    val binaryNumber = mutableListOf<Boolean>()
    while (buffer.isNotEmpty()) {
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
    return binaryNumber.toLong()

}

fun List<Long>.executeOperatorType(typeId: Int): Long =
    when (typeId) {
        SUM -> this.sum()
        PRODUCT -> this.reduce { acc, l -> acc * l }
        MINIMUM -> this.minOf { it }
        MAXIMUM -> this.maxOf { it }
        GREATER_THAN -> if(this[0] > this[1]) 1 else 0
        LESS_THAN -> if(this[1] > this[0] ) 1 else 0
        EQUAL_TO -> if (this[0] == this[1]) 1 else 0
        else -> throw UnsupportedOperationException("Type $typeId is not supported")
    }

fun parseOperator(buffer: Queue<Boolean>, operatorType: Int): Long {
    val lengthTypeId = buffer.poll()
    // 0 -> 15-bit number  is representing a number of bit's in the sub-package

    val parsedValues = mutableListOf<Long>()

    if (lengthTypeId) {
        val numberOfSubPackages = buffer.poll(11).toInt()

        (1..numberOfSubPackages).forEach { _ ->
            parsedValues.add(parse(buffer))
        }

    } else {
        val numOfBitsInSubPackage = buffer.poll(15).toInt()
        val subPackagePayload = LinkedList(buffer.poll(numOfBitsInSubPackage))

        while (subPackagePayload.isNotEmpty()) {
            parsedValues.add(parse(subPackagePayload))
        }

    }

    return parsedValues.executeOperatorType(operatorType)

}


fun parse(input: String): Long {


    val binaryInput = input.toCharArray()
        .flatMap { it.toBit() }

    val buffer: Queue<Boolean> = LinkedList(binaryInput)


    return parse(buffer)
}

var versionSum = 0
private fun parse(buffer: Queue<Boolean>): Long {

    val version = buffer.poll(3).toInt()
    versionSum += version

    @Suppress("MoveVariableDeclarationIntoWhen")
    val typeId = buffer.poll(3).toInt()

    val result = when (typeId) {
        LITERAL -> parseLiteral(buffer)
        SUM -> parseOperator(buffer, typeId)
        PRODUCT -> parseOperator(buffer, typeId)
        MINIMUM -> parseOperator(buffer, typeId)
        MAXIMUM -> parseOperator(buffer, typeId)
        GREATER_THAN -> parseOperator(buffer, typeId)
        LESS_THAN -> parseOperator(buffer, typeId)
        EQUAL_TO -> parseOperator(buffer, typeId)
        else -> throw UnsupportedOperationException("TypeId '$typeId' is not supported")
    }


    return result

}


fun main() {
    val input = readInput("Day16").first()
    println(parse(input))

}