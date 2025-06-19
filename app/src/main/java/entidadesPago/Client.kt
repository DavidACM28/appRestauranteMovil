package entidadesPago

import com.google.gson.annotations.SerializedName

data class Client(
    @SerializedName("tipoDoc") val tipoDoc: String,
    @SerializedName("numDoc") val numDoc: Long,
    @SerializedName("rznSocial") val rznSocial: String,
    @SerializedName("address") val address: Address
)