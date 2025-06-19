package entidadesPago

import com.google.gson.annotations.SerializedName

data class FormaPago(
    @SerializedName("moneda") val moneda: String,
    @SerializedName("tipo") val tipo: String
)