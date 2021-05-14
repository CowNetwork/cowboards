package network.cow.mc.cowboards

import net.kyori.adventure.text.Component

/**
 * @author Tobias Büser
 */
data class ScoreboardLine(val name: String, var text: Component, var score: Int,
                          var order: Int, var entryName: String)
