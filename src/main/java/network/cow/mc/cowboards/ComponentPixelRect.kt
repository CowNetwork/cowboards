package network.cow.mc.cowboards

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import java.awt.image.BufferedImage

/**
 * @author Tobias Büser
 */
class ComponentPixelRect(private val colorHexCodes: List<List<Color?>>) {

    val components: List<Component>
    val height = colorHexCodes.size
    val width = colorHexCodes.maxOfOrNull { it.size } ?: 0

    companion object {
        const val BLOCK_CHAR = '█'
    }

    init {
        val componentList = mutableListOf<Component>()

        colorHexCodes.forEach { list ->
            val component = Component.text()
            list.forEach {
                if (it == null) {
                    component.append(Component.text(" "))
                } else {
                    val color = it.toTextColor()
                    component.append(Component.text(BLOCK_CHAR, color))
                }
            }
            componentList.add(component.build())
        }

        components = componentList.toList()
    }

    private fun addRight(other: List<List<Color?>>): ComponentPixelRect {
        if (other.size != this.height) throw IllegalArgumentException("The height of both rects need to be the same.")

        val mergedList = mutableListOf<List<Color?>>()
        colorHexCodes.forEachIndexed { index, it ->
            val newList = it.toMutableList()
            newList.addAll(other[index])

            mergedList.add(newList)
        }
        return ComponentPixelRect(mergedList)
    }

    operator fun plus(other: String): ComponentPixelRect {
        return addRight(List(this.height) { listOf(null) })
    }

    operator fun plus(other: ComponentPixelRect): ComponentPixelRect {
        return addRight(other.colorHexCodes)
    }

    operator fun times(times: Int): ComponentPixelRect {
        var rect = this
        val copy = ComponentPixelRect(this.colorHexCodes)
        for (i in 1 until times) {
            rect += copy
        }
        return rect
    }

}

private fun String.toColor(): Color {
    if (this.startsWith("#")) return this.drop(1).toColor()
    if (this.length < 6) {
        return Color.WHITE
    }

    return try {
        Color.fromRGB(
            Integer.valueOf(substring(0, 2), 16),
            Integer.valueOf(substring(2, 4), 16),
            Integer.valueOf(substring(4, 6), 16)
        )
    } catch (ex: Exception) {
        Color.WHITE
    }
}

private fun Color.toTextColor(): TextColor {
    return TextColor.color(this.red, this.green, this.blue)
}

private fun BufferedImage.toColorArray(): List<List<Color>> {
    val list = mutableListOf<List<Color>>()
    for (y in 0 until this.height.coerceAtMost(15)) {
        val yList = mutableListOf<Color>()

        for (x in 0 until this.width) {
            val rgba = this.getRGB(x, y)
            val red = rgba shr 16 and 255
            val green = rgba shr 8 and 255
            val blue = rgba and 255
            yList.add(Color.fromRGB(red, green, blue))
        }

        list.add(yList)
    }
    return list
}
