package entidadesPago

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("direccion") val direccion: String,
    @SerializedName("provincia") val provincia: String,
    @SerializedName("departamento") val departamento: String,
    @SerializedName("distrito") val distrito: String,
    @SerializedName("ubigueo") val ubigueo: String
)