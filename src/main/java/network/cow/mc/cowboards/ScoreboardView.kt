package network.cow.mc.cowboards

import net.kyori.adventure.text.Component

/**
 * @author Tobias Büser
 */
interface ScoreboardView : ScoreboardViewPage {

    /*fun addPage(page: ScoreboardViewPage)

    fun getPage(id: String): ScoreboardViewPage?
    fun getPage(order: Int): ScoreboardViewPage?

    fun getCurrentPage(): ScoreboardViewPage

    fun setCurrentPage(id: String)
    fun setCurrentPage(order: Int)*/

}

interface ScoreboardViewPage {

    val name: String
    val order: Int

    fun setTitle(text: Component)
    fun setTitle(text: String) = setTitle(Component.text(text))

    fun reloadTitle()

    fun setLine(order: Int, text: Component)
    fun setLine(order: Int, text: String) = setLine(order, Component.text(text))

    fun addLine(text: Component) = this.setLine(this.getLines().size, text)
    fun addLine(text: String) = this.addLine(Component.text(text))

    fun setLine(name: String, text: Component)
    fun setLine(name: String, text: String) = setLine(name, Component.text(text))

    fun reloadLine(line: ScoreboardLine)
    fun reloadLines() = this.getLines().forEach { this.reloadLine(it) }

    fun getLineOrNull(name: String) = this.getLines().firstOrNull { it.name == name }
    fun getLine(name: String): ScoreboardLine {
        return getLineOrNull(name) ?: throw IllegalArgumentException("Could not find line with name $name")
    }

    fun getLineOrNull(order: Int) = this.getLines().getOrNull(order)
    fun getLine(order: Int): ScoreboardLine {
        return getLineOrNull(order) ?: throw IllegalArgumentException("Could not find line with order $order")
    }

    fun getLines(): List<ScoreboardLine>

    fun swapLines(lineFirst: ScoreboardLine, lineSecond: ScoreboardLine)
    fun swapLines(orderFirst: Int, orderSecond: Int) = this.swapLines(this.getLine(orderFirst), this.getLine(orderSecond))
    fun swapLines(nameFirst: String, orderSecond: Int) = this.swapLines(this.getLine(nameFirst), this.getLine(orderSecond))
    fun swapLines(orderFirst: Int, nameSecond: String) = this.swapLines(this.getLine(orderFirst), this.getLine(nameSecond))
    fun swapLines(nameFirst: String, nameSecond: String) = this.swapLines(this.getLine(nameFirst), this.getLine(nameSecond))

    fun shiftLine(line: ScoreboardLine, orderTo: Int)
    fun shiftLine(orderFrom: Int, orderTo: Int) = this.shiftLine(this.getLine(orderFrom), orderTo)
    fun shiftLine(nameFrom: String, orderTo: Int) = this.shiftLine(this.getLine(nameFrom), orderTo)

}
