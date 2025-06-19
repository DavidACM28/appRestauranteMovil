package entidadesPago

import com.google.gson.annotations.SerializedName

data class Legend(
    @SerializedName("code") val code: String,
    @SerializedName("value") val value: String
)