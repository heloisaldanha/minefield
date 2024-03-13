package view

import model.EventField
import model.Field
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.SwingUtilities

private val FIELD_BACKGROUND_COLOR = Color(184, 184, 184)
private val MARK_BACKGROUND_COLOR = Color(8, 179, 247)
private val EXPLOSION_BACKGROUND_COLOR = Color(189, 66, 68)
private val TEXT_COLOR = Color(0, 100, 0)

class ButtonField(private val field: Field): JButton() {
    init {
        font = font.deriveFont(Font.BOLD)
        background = FIELD_BACKGROUND_COLOR
        isOpaque = true
        border= BorderFactory.createBevelBorder(0)
        addMouseListener(MouseClickListener(field, { it.open() }, { it.updateMark() }))

        field.onEvent(this::applyStyle)
    }

    private fun applyStyle(field: Field, eventField: EventField) {
        when(eventField) {
            EventField.EXPLOSION -> applyStyleExplosion()
            EventField.OPEN -> applyStyleOpen()
            EventField.MARK -> applyStyleMark()
            else -> applyStyleDefault()
        }

        SwingUtilities.invokeLater {
            repaint()
            validate()
        }
    }

    private fun applyStyleDefault() {
        background = FIELD_BACKGROUND_COLOR
        border = BorderFactory.createBevelBorder(0)
        text = ""
    }

    private fun applyStyleMark() {
        background = MARK_BACKGROUND_COLOR
        foreground = Color.BLACK
        text = "M"
    }

    private fun applyStyleOpen() {
        background = FIELD_BACKGROUND_COLOR
        border = BorderFactory.createLineBorder(Color.GRAY)

        foreground = when(field.qtdMinedNear) {
            1 -> TEXT_COLOR
            2 -> Color.BLUE
            3 -> Color.YELLOW
            4, 5, 6 -> Color.RED
            else -> Color.PINK
        }

        text = if (field.qtdMinedNear > 0) field.qtdMinedNear.toString() else ""

    }

    private fun applyStyleExplosion() {
        background = EXPLOSION_BACKGROUND_COLOR
        text = "X"
    }
}