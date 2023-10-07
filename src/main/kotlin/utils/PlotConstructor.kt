package utils

import com.github.kotlintelegrambot.entities.TelegramFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.kotlinx.kandy.dsl.continuous
import org.jetbrains.kotlinx.kandy.dsl.invoke
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.ir.Plot
import org.jetbrains.kotlinx.kandy.letsplot.export.save
import org.jetbrains.kotlinx.kandy.letsplot.export.toBufferedImage
import org.jetbrains.kotlinx.kandy.letsplot.layers.bars
import org.jetbrains.kotlinx.kandy.letsplot.layout
import org.jetbrains.kotlinx.kandy.letsplot.x
import org.jetbrains.kotlinx.kandy.letsplot.y
import org.jetbrains.kotlinx.kandy.util.color.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


class PlotConstructor {
    fun getPlotImageAsFile(timeIntervals: List<String>, values: List<String>): TelegramFile {
        val simpleDataset = mapOf(
            "time" to listOf(0, 1, 2, 4, 5, 7, 8, 9),
            "values" to listOf(12.0, 14.2, 15.1, 15.9, 17.9, 15.6, 14.2, 24.3)
        )

        val plot = plot(simpleDataset) {
            x("time"<Int>())

            y("values"<Double>()) {
                scale = continuous(0.0..25.5)
            }

            bars {
                fillColor("values"<Double>()) {
                    scale = continuous(range = Color.YELLOW..Color.ORANGE)
                }
                borderLine.width = 0.0
            }

            layout {
                title = "Simple plot with lets-plot"
                caption = "See `examples` section for more\n complicated and interesting examples!"
            }
        }

        return plotToTelegramFile(plot)
    }

    private fun plotToTelegramFile(
        plot: Plot
    ): TelegramFile {
        val pathToFile = plot.save(filename = "plot.jpeg")
        print("FILE PATH $pathToFile")
        val file = File(pathToFile)
        return TelegramFile.ByFile(file)
    }
}