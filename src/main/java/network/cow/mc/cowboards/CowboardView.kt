package network.cow.mc.cowboards

import net.kyori.adventure.text.Component
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import java.util.UUID

/**
 * @author Tobias Büser
 */
class CowboardView(override val name: String, override val order: Int,
                   title: Component, private val objective: Objective) : ScoreboardView {

    private val id = UUID.randomUUID().toString().take(8)

    private val pages = mutableListOf<CowboardPage>()
    private var currentPage: CowboardPage = CowboardPage(name, order, title, this)

    init {
        objective.displayName(title)
    }

    fun slotItInBitch() {
        objective.displaySlot = DisplaySlot.SIDEBAR
    }

    override fun reloadTitle() {
        objective.displayName(this.currentPage.title)
    }

    override fun reloadLine(line: ScoreboardLine) {
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

        if (team.prefix() != line.text) {
            team.prefix(line.text)
        }

        val entry = this.objective.getScore(line.entryName)
        if (entry.score != line.score) {
            entry.score = line.score
        }
    }

    override fun setTitle(text: Component) = this.currentPage.setTitle(text)
    override fun setLine(order: Int, text: Component) = this.currentPage.setLine(order, text)
    override fun setLine(name: String, text: Component) = this.currentPage.setLine(name, text)

    override fun getLines() = this.currentPage.getLines()
    override fun swapLines(lineFirst: ScoreboardLine, lineSecond: ScoreboardLine) = this.currentPage.swapLines(lineFirst, lineSecond)
    override fun shiftLine(line: ScoreboardLine, orderTo: Int) = this.currentPage.shiftLine(line, orderTo)

}
