package data.remote

import okhttp3.Interceptor
import okhttp3.Response
import utils.GlobalTokenSaver

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()

        val request = GlobalTokenSaver.token?.let {
            oldRequest.newBuilder()
                .addHeader("Authorization", "Bearer ${GlobalTokenSaver.token}")
                .build()
        } ?: oldRequest

        return chain.proceed(request)
    }
}