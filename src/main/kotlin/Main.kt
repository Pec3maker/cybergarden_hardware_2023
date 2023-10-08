import bot.CybergardenGen2Bot
import data.remote.RetrofitClient
import data.remote.RetrofitType
import data.remote.repository.ZimbabweRepository

fun main() {
    val bot = CybergardenGen2Bot(
        ZimbabweRepository(
            zimbabweApi = RetrofitClient.getZimbabweApi(RetrofitClient.getRetrofit(RetrofitType.ZIMBABWE))
        )
    ).createBot()

    bot.startPolling()
}