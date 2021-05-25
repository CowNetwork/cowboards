# cowboards

Library for better scoreboards in Minecraft

# Quick start

```kotlin
val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
val objective = objective = scoreboard.registerNewObjective("id", Cowboard.CRITERIA_NAME, Component.empty())

val view = CowboardView("test_view", 0, Component.text("Cow Network", TextColor.color(254, 205, 211)), objective)
view.setLine(1, Component.text("Kills:"))
view.setLine("kills", Component.text("0", NamedTextColor.RED))
view.setLine(3, Component.empty())
view.setLine(4, Component.text("Jumps:"))
view.setLine("jumps", Component.text("0", NamedTextColor.GREEN))
view.setLine(6, Component.empty())
view.setLine(7, Component.text("Distanz gesprintet:"))
view.setLine("sprinted", Component.text("0", NamedTextColor.YELLOW))
view.setLine(9, Component.empty())

// set to the sidebar displayslot
view.slotItInBitch()
```

To update, just use:

```kotlin
view.setLine("kills", Component.text("1", NamedTextColor.RED))
```
