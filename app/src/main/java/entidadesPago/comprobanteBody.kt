package entidadesPago

import com.google.gson.annotations.SerializedName

data class comprobanteBody(
    @SerializedName("ublVersion") val ublVersion: String,
    @SerializedName("tipoOperacion") val tipoOperacion: String,
    @SerializedName("tipoDoc") val tipoDoc: String,
    @SerializedName("serie") val serie: String,
    @SerializedName("correlativo") var correlativo: String,
    @SerializedName("fechaEmision") val fechaEmision: String,
    @SerializedName("formaPago") val formaPago: FormaPago,
    @SerializedName("tipoMoneda") val tipoMoneda: String,
    @SerializedName("client") val client: Client,
    @SerializedName("company") val company: Company,
    @SerializedName("mtoOperGravadas") val mtoOperGravadas: Double,
    @SerializedName("mtoIGV") val mtoIGV: Double,
    @SerializedName("valorVenta") val valorVenta: Double,
    @SerializedName("totalImpuestos") val totalImpuestos: Double,
    @SerializedName("subTotal") val subTotal: Double,
    @SerializedName("mtoImpVenta") val mtoImpVenta: Double,
    @SerializedName("details") val details: List<Detail>,
    @SerializedName("legends") val legends: List<Legend>
)