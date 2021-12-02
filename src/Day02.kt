import Move.Type.*
import java.util.*

class Move(val type: Type, val value: Int) {

    constructor(typeAsString: String, value: Int) :
            this(valueOf(typeAsString.uppercase()), value)

    enum class Type {
        FORWARD,
        DOWN,
        UP
    }
}

fun main() {
    fun part1(input: List<Move>) = input
        .map { if (it.type == UP) Move(DOWN, -it.value) else it }
        .groupBy { it.type }
        .map { (_, moves) -> moves.sumOf { it.value } }
        .reduce { a, b -> a * b }

    fun part2(input: List<Move>) = input
        .fold(Triple(0, 0, 0)) { (aim, depth, horizontal), move ->
            when (move.type) {
                FORWARD -> Triple(aim, depth + aim * move.value, horizontal + move.value)
                DOWN -> Triple(aim + move.value, depth, horizontal)
                UP -> Triple(aim - move.value, depth, horizontal)
            }
        }
        .let { (_, depth, horizontal) -> depth * horizontal }

    val input = readInput("Day02") { Scanner(it).run { Move(next(), nextInt()) } }
    println(part1(input))
    println(part2(input))
}
