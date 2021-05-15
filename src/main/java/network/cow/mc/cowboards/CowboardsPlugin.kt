package network.cow.mc.cowboards

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Statistic
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerStatisticIncrementEvent
import org.bukkit.plugin.java.JavaPlugin
import java.text.MessageFormat
import java.util.Calendar

/**
 * @author Tobias Büser
 */
class CowboardsPlugin : JavaPlugin(), Listener {

    private var view: CowboardView? = null

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)

        // unregister all old objectives and teams
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        scoreboard.getObjectivesByCriteria(Cowboard.CRITERIA_NAME).forEach {
            it.unregister()
        }
        scoreboard.teams.filter { it.name.startsWith(Cowboard.TEAM_PREFIX) }.forEach { it.unregister() }
        scoreboard.entries.filter { it.startsWith("§") }.forEach { scoreboard.resetScores(it) }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard

        var objective = scoreboard.getObjective("id")
        if (objective == null) {
            objective = scoreboard.registerNewObjective("id", Cowboard.CRITERIA_NAME, Component.empty())
        }

        if (view == null) {
            val view = CowboardView("test_view", 0,
                Component.text("Cow Network", TextColor.color(254, 205, 211))
                    .append(Component.text(" | ", NamedTextColor.GRAY))
                    .append(Component.text(getCurrentTime(), NamedTextColor.AQUA)), objective)
            view.setLine(1, Component.text("Kills:"))
            view.setLine("kills", Component.text(currentKills(player), NamedTextColor.RED))
            view.setLine(3, Component.empty())
            view.setLine(4, Component.text("Jumps:"))
            view.setLine("jumps", Component.text(currentJumps(player), NamedTextColor.GREEN))
            view.setLine(6, Component.empty())
            view.setLine(7, Component.text("Distanz gesprintet:"))
            view.setLine("sprinted", Component.text(currentSprinted(player), NamedTextColor.YELLOW))
            view.setLine(9, Component.empty())
            view.slotItInBitch()
            this.view = view
        }

        player.sendMessage(Component.text("Scoreboard set.", NamedTextColor.GREEN))

        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            view!!.setTitle(Component.text("Cow Network", TextColor.color(254, 205, 211))
                .append(Component.text(" | ", NamedTextColor.GRAY))
                .append(Component.text(getCurrentTime(), NamedTextColor.AQUA)))
        }, 20L, 20L)
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        if (view == null) {
            return
        }

        if (event.message == "shift") {
            view!!.shiftLine(2, 4)
        } else if (event.message == "shiftBack") {
            view!!.shiftLine(4, 2)
        } else {
            view!!.swapLines(1, 4)
            view!!.swapLines("kills", "jumps")
        }
    }

    @EventHandler
    fun onStatisticChange(event: PlayerStatisticIncrementEvent) {
        if (view == null) {
            return
        }
        val player = event.player

        view!!.setLine("kills", Component.text(currentKills(player), NamedTextColor.RED))
        view!!.setLine("jumps", Component.text(currentJumps(player), NamedTextColor.GREEN))
        view!!.setLine("sprinted", Component.text(currentSprinted(player), NamedTextColor.YELLOW))
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
        val minute = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
        val second = calendar.get(Calendar.SECOND).toString().padStart(2, '0')

        return "$hour:$minute:$second"
    }

    private fun currentJumps(player: Player) = MessageFormat.format("{0}", player.getStatistic(Statistic.JUMP))
    private fun currentKills(player: Player) = MessageFormat.format("{0}", player.getStatistic(Statistic.MOB_KILLS))
    private fun currentSprinted(player: Player) = MessageFormat.format("{0} cm", player.getStatistic(Statistic.SPRINT_ONE_CM))

}


