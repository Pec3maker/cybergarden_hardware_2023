import bot.CybergardenBot
import data.remote.RetrofitClient
import data.remote.RetrofitType
import data.remote.repository.ZimbabweRepository

fun main() {
    val bot = CybergardenBot(
        ZimbabweRepository(
            zimbabweApi = RetrofitClient.getZimbabweApi(RetrofitClient.getRetrofit(RetrofitType.ZIMBABWE))
        )
    ).createBot()

    bot.startPolling()
}