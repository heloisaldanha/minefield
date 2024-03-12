package model

import java.util.Random


data class Table(
    val qtdLines: Int,
    val qtdColumns: Int,
    private val qtdMines: Int
) {
    private val fields = ArrayList<ArrayList<Field>>()
    private val callbacks = ArrayList<(EventTable) -> Unit>()

    init {
        generateFields()
        associateNear()
        sortMines()
    }

    private fun generateFields() {
        for (line in 0 until qtdLines) {
            fields.add(ArrayList())
            for (column in 0 until qtdColumns) {
                val newField = Field(line, column)
                newField.onEvent(this::verifyVictoryOrLoss)
                fields[line].add(newField)
            }
        }
    }


    private fun associateNear() {
        forEachFields { associateNear(it) }
    }

    private fun associateNear(field: Field) {
        val (line, column) = field
        val lines = arrayOf(line - 1, line, line + 1)
        val columns = arrayOf(column -1, column, column + 1)

        lines.forEach { l ->
            columns.forEach { c ->
                val current = fields.getOrNull(l)?.getOrNull(c)
                current?.takeIf { field != it }?.let { field.addNear(it) }
            }
        }
    }


    private fun sortMines() {
        val generator = Random()
        var selectedLine = -1
        var selectedColumn = -1
        var qtdMinesAtThisTime = 0

        while (qtdMinesAtThisTime < this.qtdMines) {
            selectedLine = generator.nextInt(qtdLines)
            selectedColumn = generator.nextInt(qtdColumns)

            val selectedField = fields[selectedLine][selectedColumn]
            if (selectedField.safe) {
                selectedField.mine()
                qtdMinesAtThisTime++
            }
        }
    }

    private fun goalAchieved(): Boolean {
        var playerWon = true
        forEachFields { if (!it.goalAchieved) playerWon = false }
        return playerWon
    }

    private fun verifyVictoryOrLoss(field: Field, event: EventField) {
        if (event == EventField.EXPLOSION) {
            callbacks.forEach { it(EventTable.LOSS) }
        } else if (goalAchieved()) {
            callbacks.forEach { it(EventTable.VICTORY) }
        }
    }

    fun forEachFields(callback: (Field) -> Unit) {
        fields.forEach { line -> line.forEach(callback) }
    }

    fun onEvent(callback: (EventTable) -> Unit) {
        callbacks.add { callback }
    }

    fun restart() {
        forEachFields { it.restart() }
        sortMines()
    }
}
