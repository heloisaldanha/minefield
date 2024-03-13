package view

import model.EventTable
import model.Table
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

fun main(args: Array<String>) {
    MainScreen()
}

class MainScreen: JFrame() {
    private val table = Table(16, 30, 89)
    private val tablePanel = TablePanel(table)

    init {
        table.onEvent(this::showResult)
        add(tablePanel)

        setSize(690, 438)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Campo Minado"
        isVisible = true
    }

    private fun showResult(eventTable: EventTable) {
        SwingUtilities.invokeLater {
            val msg = when(eventTable) {
                EventTable.VICTORY -> "Você ganhou!!!"
                EventTable.LOSS -> "Você perdeu... :P"
            }

            JOptionPane.showMessageDialog(this, msg)
            table.restart()

            tablePanel.repaint()
            tablePanel.validate()
        }
    }
}
