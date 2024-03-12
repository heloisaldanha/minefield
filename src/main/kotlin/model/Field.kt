package model

data class Field(
    var line: Int,
    var column: Int
) {
    private var near = ArrayList<Field>()
    private var callbacks = ArrayList<(Field, EventField) -> Unit>()

    var marked: Boolean = false
    var opened: Boolean = false
    var mined: Boolean = false

    val unmarked: Boolean = !marked
    val closed: Boolean = !opened
    val safe: Boolean = !mined
    val goalAchieved: Boolean = safe && opened || mined && marked
    val qtdMinedNear: Int = near.filter { it.mined }.size
    val isNearSafe: Boolean = near.map { it.safe }.reduce { result, safe -> result && safe }

    fun addNear(near: Field) {
        near.addNear(near)
    }

    fun onEvent(callback: (Field, EventField) -> Unit) {
        callbacks.add(callback)
    }

    fun open() {
        if (closed) {
            opened = true
            if (mined) {
                callbacks.forEach { it(this, EventField.EXPLOSION) }
            } else {
                callbacks.forEach { it(this, EventField.OPEN) }
                near.filter { it.closed && it.safe && it.isNearSafe }.forEach { it.open() }
            }
        }
    }

    fun updateMark() {
        if (closed) {
            marked = !unmarked
            val event = if (marked) EventField.MARK else EventField.UNMARK
            callbacks.forEach { it(this, event) }
        }
    }

    fun mine() {
        mined = true
    }

    fun restart() {
        opened = false
        mined = false
        marked = false

        callbacks.forEach { it(this, EventField.RESTART) }
    }
}