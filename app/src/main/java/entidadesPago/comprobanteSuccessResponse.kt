package entidadesPago

import com.google.gson.annotations.SerializedName

data class comprobanteSuccessResponse(
    @SerializedName("xml") val xml: String,
    @SerializedName("hash") val hash: String,
    @SerializedName("sunatResponse") val sunatResponse: SunatResponseX
)