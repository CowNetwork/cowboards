package network.cow.mc.cowboards

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor

/**
 * @author Tobias Büser
 */
class CowboardPage(override val name: String, override val order: Int,
                   title: Component, private val view: CowboardView) : ScoreboardViewPage {

    private val lines = mutableListOf<ScoreboardLine>()
    var title = title; private set

    override fun setTitle(text: Component) {
        this.title = text
        this.reloadTitle()
    }

    override fun reloadTitle() {
        // delegate to view
        this.view.reloadTitle()
    }

    override fun setLine(order: Int, text: Component) {
        if (order !in 0..15) {
            throw IllegalArgumentException("order has to be in range 0 to 15.")
        }

        var line = this.getLineOrNull(order)
        if (line != null) {
            line.text = text
            this.reloadLine(line)
            return
        }

        // set line at position `order`
        val cappedOrder = order.coerceAtMost(Cowboard.MAX_LINES).coerceAtLeast(0)
        if (cappedOrder > lines.size) {
            // fill in blank lines until this order
            for (i in lines.size until cappedOrder) {
                line = ScoreboardLine(lines.size.toString(), Component.empty(), lines.size, randomEntryName())
                this.lines.add(line)
                this.reloadLine(line)
            }
        }

        // append it at the end
        line = ScoreboardLine(name, text, lines.size, randomEntryName())
        this.lines.add(line)
        this.reloadLine(line)
    }

    override fun setLine(name: String, text: Component) {
        var line = this.getLineOrNull(name)
        if (line != null) {
            line.text = text
            this.reloadLine(line)
            return
        }

        // append line at the end
        line = ScoreboardLine(name, text, lines.size, randomEntryName())
        this.lines.add(line)
        this.reloadLine(line)
    }

    override fun reloadLine(line: ScoreboardLine) {
        // delegate to view
        this.view.reloadLine(line)
    }

    override fun getLines() = lines

    override fun swapLines(lineFirst: ScoreboardLine, lineSecond: ScoreboardLine) {
        lineFirst swapWith lineSecond

        this.reloadLine(lineFirst)
        this.reloadLine(lineSecond)
    }

    override fun shiftLine(line: ScoreboardLine, orderTo: Int) {
        if (orderTo !in 0..15) {
            throw IllegalArgumentException("orderTo has to be in range 0 to 15.")
        }

        val cappedOrder = order.coerceAtMost(Cowboard.MAX_LINES).coerceAtLeast(0)
        if (cappedOrder > lines.size) {
            // fill in blank lines until this order
            for (i in lines.size until cappedOrder) {
                val blankLine = ScoreboardLine(lines.size.toString(), Component.empty(), lines.size, randomEntryName())
                this.lines.add(blankLine)
            }
        }

        var currentOrder = line.order
        val backwards = currentOrder > orderTo
        while (currentOrder != orderTo) {
            val newOrder = if (backwards) currentOrder - 1 else currentOrder + 1

            val currentLine = this.getLine(currentOrder)
            val nextLine = this.getLine(newOrder)
            currentLine swapWith nextLine
            currentOrder = newOrder
        }

        this.reloadLines()
    }

    private fun randomEntryName(): String {
        val numbers = (0..21).toList()

        var string = ""
        for (i in 1..20) {
            val index = numbers.random()

            string += ChatColor.values()[index]
        }
        return string
    }

}
