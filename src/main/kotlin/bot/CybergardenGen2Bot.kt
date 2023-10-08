package bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButtonPollType
import com.github.kotlintelegrambot.entities.polls.PollType
import com.github.kotlintelegrambot.logging.LogLevel
import com.github.kotlintelegrambot.webhook
import data.remote.repository.ZimbabweRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

private const val BOT_TOKEN = "6435714209:AAFkCqVy66vBviPLG6XR3bahePn6ajXw0Zk"
private const val TIMEOUT_TIME = 30

class CybergardenGen2Bot(private val zimbabweRepository: ZimbabweRepository) {
    private var isAutoMetricsMode = false

    fun createBot(): Bot {
        return bot {
            token = BOT_TOKEN
            timeout = TIMEOUT_TIME
            logLevel = LogLevel.Network.Body

            dispatch {
                setUpCommands()
                setUpCallbacks()
            }


        }
    }
    private fun Dispatcher.setUpCallbacks() {
        callbackQuery("back_to_sensors_list") {

        }
    }

    private fun Dispatcher.setUpCommands() {
        command("start") {
            CoroutineScope(Dispatchers.IO).launch {
                zimbabweRepository.auth()
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "Привет! Я бот, отслеживающий несколько датчиков погоды.\n Смотри список доступых команд"
                )
            }
        }

        command("sensor_graphs") {
            CoroutineScope(Dispatchers.IO).launch {
                val graphs = zimbabweRepository.getGraphs()

                graphs.forEach { graphModel ->
                    val telegramFile = TelegramFile.ByByteArray(zimbabweRepository.getGraph(graphModel))
                    bot.sendPhoto(
                        chatId = ChatId.fromId(message.chat.id),
                        photo = telegramFile,
                        caption = graphModel.name
                    )
                }
            }
        }

        command("stations") {
            CoroutineScope(Dispatchers.IO).launch {
                val stations = zimbabweRepository.getStationsList()

                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "Список станций:"
                )

                stations.forEach {
                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        text = "Станция ${it.name}\n" +
                                "ID ${it.id}"
                    )
                }
            }
        }

        command("last_metrics") {
            CoroutineScope(Dispatchers.IO).launch {
                val sensors = zimbabweRepository.getSensorsList()

                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "Последние показания датчиков:"
                )

                sensors.forEach {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyydd  HH:mm:ss.SSS", Locale.getDefault())
                    val formattedDate = dateFormat.format(it.time)

                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        text = "Датчик ${it.type.name}\n" +
                                "Последние показания ${if(it.value.isBlank()) "0" else it.value } ${it.units}\n" +
                                "Время последнего измерения $formattedDate"
                    )
                }
            }
        }

        command("real_time_metrics") {
            CoroutineScope(Dispatchers.IO).launch {
                isAutoMetricsMode = !isAutoMetricsMode

                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "Вы ${if(isAutoMetricsMode) "включили" else "отключили" } режим отслеживания станции в реальном времени"
                )
            }
        }
    }
}