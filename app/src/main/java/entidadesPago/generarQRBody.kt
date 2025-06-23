package entidadesPago

import com.google.gson.annotations.SerializedName

data class generarQRBody(
    @SerializedName("ruc") val ruc: String,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("serie") val serie: String,
    @SerializedName("numero") val numero: String,
    @SerializedName("emision") val emision: String,
    @SerializedName("igv") val igv: Double,
    @SerializedName("total") val total: Double,
    @SerializedName("clienteTipo") val clienteTipo: String,
    @SerializedName("clienteNumero") val clienteNumero: String
)