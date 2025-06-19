package utilidades

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor:Interceptor {

    private lateinit var token:String

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
        return chain.proceed(request)
    }

    fun inicializarToken(token:String){
        this.token = token
    }

}