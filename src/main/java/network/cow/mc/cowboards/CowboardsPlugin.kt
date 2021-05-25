package network.cow.mc.cowboards

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Tobias Büser
 */
class CowboardsPlugin : JavaPlugin(), Listener {

    override fun onEnable() {
        // unregister all old objectives and teams
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        scoreboard.getObjectivesByCriteria(Cowboard.CRITERIA_NAME).forEach {
            it.unregister()
        }
        scoreboard.teams.filter { it.name.startsWith(Cowboard.TEAM_PREFIX) }.forEach { it.unregister() }
        scoreboard.entries.filter { it.startsWith("§") }.forEach { scoreboard.resetScores(it) }
    }

}


