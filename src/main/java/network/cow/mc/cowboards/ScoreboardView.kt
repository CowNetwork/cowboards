package network.cow.mc.cowboards

import net.kyori.adventure.text.Component

/**
 * @author Tobias Büser
 */
interface ScoreboardView : ScoreboardViewPage {

    fun setTitle(text: Component)
    fun setTitle(text: String) = setTitle(Component.text(text))

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

    fun setLine(order: Int, text: Component)
    fun setLine(order: Int, text: String) = setLine(order, Component.text(text))

    fun setLine(name: String, text: Component)
    fun setLine(name: String, text: String) = setLine(name, Component.text(text))

    fun getLineOrNull(name: String): ScoreboardLine?
    fun getLine(name: String): ScoreboardLine {
        return getLineOrNull(name) ?: throw IllegalArgumentException("Could not find line with name $name")
    }

    fun getLineOrNull(order: Int): ScoreboardLine?
    fun getLine(order: Int): ScoreboardLine {
        return getLineOrNull(order) ?: throw IllegalArgumentException("Could not find line with order $order")
    }

    fun getLines(): List<ScoreboardLine>

    fun swapLines(orderFirst: Int, orderSecond: Int)
    fun swapLines(nameFirst: String, orderSecond: Int)
    fun swapLines(orderFirst: Int, nameSecond: String)
    fun swapLines(nameFirst: String, nameSecond: String)

    fun shiftLine(orderFrom: Int, orderTo: Int)
    fun shiftLine(nameFrom: String, orderTo: Int)

}
