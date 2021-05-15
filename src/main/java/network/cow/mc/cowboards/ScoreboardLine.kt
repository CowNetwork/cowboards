package network.cow.mc.cowboards

import net.kyori.adventure.text.Component

/**
 * @author Tobias Büser
 */
data class ScoreboardLine(var name: String, var text: Component, var order: Int, var entryName: String) {

    val score: Int; get() = Cowboard.MAX_LINES - order

    infix fun swapWith(other: ScoreboardLine) {
        this.name = other.name.also { other.name = this.name }
        this.text = other.text.also { other.text = this.text }
    }

}

data class ScoreboardPage(val lines: MutableList<ScoreboardLine>)
