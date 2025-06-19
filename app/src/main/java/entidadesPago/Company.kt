package entidadesPago

import com.google.gson.annotations.SerializedName

data class Company(
    @SerializedName("ruc") val ruc: Long,
    @SerializedName("razonSocial") val razonSocial: String,
    @SerializedName("nombreComercial") val nombreComercial: String,
    @SerializedName("address") val address: Address,
)