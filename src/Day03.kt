fun transposeColumn(input: Sequence<String>, column: Int) = input
    .map { it[column] }
    .joinToString("")

fun transpose(input: Sequence<String>) = sequence {
    for (column in input.first().indices) {
        yield(transposeColumn(input, column))
    }
}

fun gammaRateB(transposed: String) = transposed
    .count { it == '1' }
    .let { ones ->
        val zeros = transposed.length - ones
        if (ones > zeros) '1' else if (ones < zeros) '0' else null
    }

fun epsilonRateB(gammaRateB: Char?) = when (gammaRateB) {
    '1' -> '0'
    '0' -> '1'
    else -> null
}

fun epsilonRate(gammaRate: String) = gammaRate
    .map { epsilonRateB(it) }
    .joinToString("")

fun main() {

    fun part1(input: Sequence<String>) = transpose(input)
        .map { gammaRateB(it) }
        .joinToString("")
        .let { gammaRate ->
            gammaRate.toInt(2) * epsilonRate(gammaRate).toInt(2)
        }

    fun rating(input: Sequence<String>, bitCriteria: (String) -> Char): Int {
        var result = input.asSequence()
        for (i in input.first().indices) {
            if (result.count() == 1) {
                break
            }

            val criteria = bitCriteria(transposeColumn(result, i))
            result
                .filter { it[i] == criteria }
                .also { result = it }
        }
        return result.first().toInt(2)
    }

    fun part2(input: Sequence<String>): Int {
        val oxy = rating(input) { gammaRateB(it) ?: '1' }
        val co2 = rating(input) { epsilonRateB(gammaRateB(it)) ?: '0' }
        return oxy * co2
    }

    val input = readInput("Day03").asSequence()
    println(part1(input))
    println(part2(input))
}
