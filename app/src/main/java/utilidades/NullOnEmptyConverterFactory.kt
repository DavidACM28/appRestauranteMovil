package utilidades

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class NullOnEmptyConverterFactory(
    private val delegate: Converter.Factory
) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val delegateConverter = delegate.responseBodyConverter(type, annotations, retrofit)
        return Converter<ResponseBody, Any?> { body ->
            if (body.contentLength() == 0L) {
                null // evita el EOFException devolviendo null
            } else {
                delegateConverter?.convert(body)
            }
        }
    }
}
