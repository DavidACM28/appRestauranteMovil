package entidadesDNIRUC

import com.google.gson.annotations.SerializedName

data class dniSuccess(
    @SerializedName("success") val success: Boolean,
    @SerializedName("dni") val dni: String,
    @SerializedName("nombres") val nombres: String,
    @SerializedName("apellidoPaterno") val apellidoPaterno: String,
    @SerializedName("apellidoMaterno") val apellidoMaterno: String,
    @SerializedName("codVerifica") val codVerifica: String,
    @SerializedName("codVerificaLetra") val codVerificaLetra: String
)