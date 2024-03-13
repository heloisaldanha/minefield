package view

import model.Table
import java.awt.GridLayout
import javax.swing.JPanel

class TablePanel(table: Table): JPanel() {

    init {
        layout = GridLayout(table.qtdLines, table.qtdColumns)
        table.forEachFields { field ->
            val button = ButtonField(field)
            add(button)
        }
    }
}