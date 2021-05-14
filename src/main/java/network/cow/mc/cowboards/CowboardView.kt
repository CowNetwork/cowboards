package network.cow.mc.cowboards

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import java.util.UUID

/**
 * @author Tobias Büser
 */
class CowboardView(override val name: String, override val order: Int,
                   private var title: Component, private val objective: Objective) : ScoreboardView {

    private val id = UUID.randomUUID().toString().take(8)
    private val lines = mutableListOf<ScoreboardLine>()

    init {
        objective.displayName(title)
    }

    fun slotItInBitch() {
        objective.displaySlot = DisplaySlot.SIDEBAR
    }

    override fun setTitle(text: Component) {
        this.title = text

        objective.displayName(text)
    }

    override fun setLine(order: Int, text: Component) {
        var line = this.getLineOrNull(order)
        if (line != null) {
            line.text = text
            this.setLineToObjective(line)
            return
        }

        // set line at position `order`
        val cappedOrder = order.coerceAtMost(Cowboard.MAX_LINES).coerceAtLeast(0)
        if (cappedOrder > lines.size) {
            // fill in blank lines until this order
            for (i in lines.size until cappedOrder) {
                line = ScoreboardLine(lines.size.toString(), Component.empty(), Cowboard.MAX_LINES - lines.size, lines.size, randomEntryName())
                this.lines.add(line)
                this.setLineToObjective(line)
            }
        }

        // append it at the end
        line = ScoreboardLine(name, text, Cowboard.MAX_LINES - lines.size, lines.size, randomEntryName())
        this.lines.add(line)
        this.setLineToObjective(line)
    }

    override fun setLine(name: String, text: Component) {
        var line = this.getLineOrNull(name)
        if (line != null) {
            line.text = text
            this.setLineToObjective(line)
            return
        }

        // append line at the end
        line = ScoreboardLine(name, text, Cowboard.MAX_LINES - lines.size, lines.size, randomEntryName())
        this.lines.add(line)
        this.setLineToObjective(line)
    }

    private fun setLineToObjective(line: ScoreboardLine) {
        val scoreboard = this.objective.scoreboard!!

        // prepare a team
        val teamName = "${Cowboard.TEAM_PREFIX}${id}_${line.order}"
        var team = scoreboard.getTeam(teamName)
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName)
        }

        if (!team.hasEntry(line.entryName)) {
            team.addEntry(line.entryName)
        }
        team.prefix(line.text)

        this.objective.getScore(line.entryName).score = line.score
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

    override fun getLineOrNull(name: String) = lines.firstOrNull { it.name == name }
    override fun getLineOrNull(order: Int) = lines.getOrNull(order)
    override fun getLines() = lines

    override fun swapLines(orderFirst: Int, orderSecond: Int) = this.swapLines(this.getLine(orderFirst), this.getLine(orderSecond))
    override fun swapLines(nameFirst: String, orderSecond: Int) = this.swapLines(this.getLine(nameFirst), this.getLine(orderSecond))
    override fun swapLines(orderFirst: Int, nameSecond: String) = this.swapLines(this.getLine(orderFirst), this.getLine(nameSecond))
    override fun swapLines(nameFirst: String, nameSecond: String) = this.swapLines(this.getLine(nameFirst), this.getLine(nameSecond))

    private fun swapLines(lineFirst: ScoreboardLine, lineSecond: ScoreboardLine) {
        val scoreFirst = lineFirst.score
        val scoreSecond = lineSecond.score

        // change scores
        lineFirst.score = scoreSecond
        lineSecond.score = scoreFirst

        val orderFirst = lineFirst.order
        val orderSecond = lineSecond.order

        lines[orderFirst] = lineSecond
        lines[orderSecond] = lineFirst

        lineFirst.order = orderSecond
        lineSecond.order = orderFirst

        this.setLineToObjective(lineFirst)
        this.setLineToObjective(lineSecond)
    }

    override fun shiftLine(orderFrom: Int, orderTo: Int) {
        TODO("Not yet implemented")
    }

    override fun shiftLine(nameFrom: String, orderTo: Int) {
        TODO("Not yet implemented")
    }

}
