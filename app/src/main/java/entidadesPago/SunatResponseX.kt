package entidadesPago

import com.google.gson.annotations.SerializedName

data class SunatResponseX(
    @SerializedName("cdrResponse") val cdrResponse: CdrResponseX,
    @SerializedName("cdrZip") val cdrZip: String,
    @SerializedName("error") val error: ErrorX,
    @SerializedName("success") val success: Boolean
)