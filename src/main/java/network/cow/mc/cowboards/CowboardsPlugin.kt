package network.cow.mc.cowboards

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import java.util.UUID

/**
 * @author Tobias Büser
 */
class CowboardsPlugin : JavaPlugin(), Listener {

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)

        // unregister all old objectives and teams
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        scoreboard.getObjectivesByCriteria(CowboardView.CRITERIA_NAME).forEach {
            it.unregister()
        }
        scoreboard.teams.filter { it.name.startsWith(CowboardView.TEAM_PREFIX) }.forEach { it.unregister() }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        val cowboard = CowboardView("Die Bibel", player.scoreboard)
        cowboard.addLine("Eine Zeile", 1)

        player.sendMessage(Component.text("Scoreboard set.", NamedTextColor.GREEN))
    }

}

private fun randomEntryName(length: Int): String {
    val numbers = (0..21).toList()

    var string = ""
    for (i in 1..length) {
        val index = numbers.random()

        string += ChatColor.values()[index]
    }
    return string
}

class CowboardView(title: Component, scoreboard: Scoreboard) {

    private val id = UUID.randomUUID().toString().take(8)
    private val objective: Objective
    private val lines = mutableListOf<Line>()

    companion object {
        const val TEAM_PREFIX = "cbv_"
        const val CRITERIA_NAME = "cowboardView"

        /**
         * Hard limit from client side rendering.
         */
        const val MAX_LINES = 15
    }

    init {
        var objective = scoreboard.getObjective(id)
        if (objective == null) {
            objective = scoreboard.registerNewObjective(id, CRITERIA_NAME, title)
        }

        objective.displaySlot = DisplaySlot.SIDEBAR
        objective.displayName(title)

        this.objective = objective
    }

    constructor(title: String, scoreboard: Scoreboard) : this(Component.text(title), scoreboard)

    fun addLine(text: Component, score: Int): CowboardView {
        if (lines.size >= MAX_LINES) {
            return this
        }

        val scoreboard = this.objective.scoreboard!!

        val teamName = "${TEAM_PREFIX}${id}_${lines.size}"
        var team = scoreboard.getTeam(teamName)
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName)
        }

        val entryName = randomEntryName(20)

        team.addEntry(entryName)
        team.prefix(text)

        this.objective.getScore(entryName).score = score

        val line = Line(text, score, team)
        this.lines.add(line)
        return this
    }

    fun addLine(text: String, score: Int) = addLine(Component.text(text), score)

    inner class Line(var text: Component, var score: Int, val team: Team)

}

