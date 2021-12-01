fun main() {
    fun part1(input: List<Int>) = input
        .zipWithNext()
        .count { it.second > it.first }

    fun part2(input: List<Int>) = input
        .windowed(3, 1) { it.sum() }
        .let { part1(it) }

    val input = readInput("Day01") { it.toInt() }
    println(part1(input))
    println(part2(input))
}
