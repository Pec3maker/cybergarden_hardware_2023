package bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.*
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.logging.LogLevel
import data.remote.repository.ZimbabweRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.PlotConstructor
import java.util.*


private const val BOT_TOKEN = "6435714209:AAFkCqVy66vBviPLG6XR3bahePn6ajXw0Zk"
private const val TIMEOUT_TIME = 30

class CybergardenBot(private val zimbabweRepository: ZimbabweRepository) {
    private var _chatId: ChatId.Id? = null
    private val chatId: ChatId.Id
            get() = requireNotNull(_chatId)


    private var observedSensorId: UUID? = null

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
        callbackQuery("get_plot") {
            val file = PlotConstructor().getPlotImageAsFile(
                timeIntervals = listOf(),
                values = listOf()
            )

            bot.sendPhoto(
                chatId = chatId,
                photo = file
            )
        }

        callbackQuery("back_to_sensors_list") {
            showSensors(bot)
        }
    }

    private fun Dispatcher.setUpCommands() {
        command("start") {
            _chatId = ChatId.fromId(message.chat.id)
            bot.sendMessage(
                chatId = chatId,
                text = "Привет! Я бот, отслеживающий несколько датчиков.\n Смотри список доступых команд"
            )
        }

        command("sensors") {
            showSensors(bot)
        }

        command("filter_sensors_by_id") {
            CoroutineScope(Dispatchers.IO).launch {
                val sensors = zimbabweRepository.getSensorsList()
                val deviceIds = args

                deviceIds.forEach { argId ->
                    val isSensorValid = sensors.map{ sensor -> sensor.id }.contains(argId)
                    val connectedString = if(isSensorValid) { "валидный, подключен  \uD83D\uDFE2" } else { "невалидный \uD83D\uDD34" }
                    bot.sendMessage(
                        chatId = chatId,
                        text = "Датчик с ID $argId $connectedString"
                    )
                }
            }
        }

        command("filter_sensors_by_type") {
            val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = "землетрясения",
                        callbackData = "get_plot"
                    ),
                    InlineKeyboardButton.CallbackData(
                        text = "влажность",
                        callbackData = "get_plot"
                    ),
                    InlineKeyboardButton.CallbackData(
                        text = "температура",
                        callbackData = "get_plot"
                    )
                ),
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = "убрать фильтры",
                        callbackData = "get_plot"
                    )
                )
            )

            bot.sendMessage(
                chatId = chatId,
                text = "Выберите тип отслеживаемых датчиковол",
                replyMarkup = inlineKeyboardMarkup
            )
        }

        command("observe_sensor") {
            val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = "Статистика",
                        callbackData = "get_plot"
                    )
                ),
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = "Отчет по работе датчика",
                        callbackData = "get_csv"
                    )
                ),
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = "<< Список датчиков",
                        callbackData = "back_to_sensors_list"
                    )
                )
            )

            bot.sendMessage(
                chatId = chatId,
                text = "Датчик <Имя>",
                replyMarkup = inlineKeyboardMarkup
            )
        }
    }

    private fun showSensors(bot: Bot) {
        CoroutineScope(Dispatchers.IO).launch {
            val sensors = zimbabweRepository.getSensorsList()

            bot.sendMessage(
                chatId = chatId,
                text = "Список доступных датчиков (${sensors.count()}):"
            )

            sensors.forEach { sensorData ->
                bot.sendMessage(
                    chatId = chatId,
                    text = sensorData.getDescription()
                )
            }
        }
    }
}