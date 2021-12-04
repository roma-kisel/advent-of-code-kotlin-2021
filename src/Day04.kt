data class Cell(val value: Int, val marked: Boolean)

class BingoBoard(private val array: Sequence<Sequence<Cell>>) {

    private fun transposeColumn(column: Int) = array
        .map { it.elementAt(column) }

    private fun transpose() = array.withIndex().map { it.index }
        .map { transposeColumn(it) }
        .let { BingoBoard(it) }

    private fun winnerRow() = array.any { row -> row.all { it.marked } }

    private fun select(selector: (Cell) -> Boolean) = array
        .map { it.filter(selector) }
        .flatten()

    private fun mark(number: Int) = array
        .map { row -> row.map { if (it.value == number) it.copy(marked = true) else it } }
        .let { BingoBoard(it) }

    private fun winner() = winnerRow() || transpose().winnerRow()

    fun unmarked() = select { it.marked.not() }

    fun markAllUntilWinner(numbers: Sequence<Int>) = numbers.withIndex()
        .scan(BingoBoard(array) to 0) { (board, _), (index, number) -> board.mark(number) to index }
        .dropWhile { (board, _) -> board.winner().not() }
        .first()
}

fun numbers(input: String) = input
    .split(",")
    .asSequence()
    .map { it.toInt() }

fun board(input: String) = input.split("\n")
    .asSequence()
    .filter { it.isNotBlank() }
    .map { row ->
        row.split(" ")
            .asSequence()
            .filter { it.isNotBlank() }
            .map { Cell(it.toInt(), false) }
    }
    .let { BingoBoard(it) }

fun main() {

    fun finalScore(winner: BingoBoard, index: Int, numbers: Sequence<Int>) =
        winner.unmarked().sumOf { cell -> cell.value } * numbers.elementAt(index)

    fun scoreOf(
        report: Map<Int, List<Pair<BingoBoard, Int>>>,
        numbers: Sequence<Int>,
        stepsIndexSelector: (Set<Int>) -> Int
    ): Int? {
        val index = stepsIndexSelector(report.keys)
        return report[index]?.first()?.let { (board, _) -> finalScore(board, index, numbers) }
    }

    fun part1(report: Map<Int, List<Pair<BingoBoard, Int>>>, numbers: Sequence<Int>): Int? {
        return scoreOf(report, numbers) { indexes -> indexes.minOf { it } }
    }

    fun part2(report: Map<Int, List<Pair<BingoBoard, Int>>>, numbers: Sequence<Int>): Int? {
        return scoreOf(report, numbers) { indexes -> indexes.maxOf { it } }
    }

    val inputs = readInputAsText("Day04")
        .split("\n\n")

    val numbers = numbers(inputs.first())
    val boards = inputs.subList(1, inputs.size)
        .map { board(it) }
        .asSequence()

    val report = lazy {
        boards
            .map { it.markAllUntilWinner(numbers) }
            .groupBy { (_, indexNumber) -> indexNumber }
    }

    println(part1(report.value, numbers))
    println(part2(report.value, numbers))
}
