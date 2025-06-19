package entidadesPago

import com.google.gson.annotations.SerializedName

data class Detail(
    @SerializedName("codProducto") val codProducto: String,
    @SerializedName("unidad") val unidad: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("cantidad") val cantidad: Double,
    @SerializedName("mtoValorUnitario") val mtoValorUnitario: Double,
    @SerializedName("mtoValorVenta") val mtoValorVenta: Double,
    @SerializedName("mtoBaseIgv") val mtoBaseIgv: Double,
    @SerializedName("porcentajeIgv") val porcentajeIgv: Double,
    @SerializedName("igv") val igv: Double,
    @SerializedName("tipAfeIgv") val tipAfeIgv: Int,
    @SerializedName("totalImpuestos") val totalImpuestos: Double,
    @SerializedName("mtoPrecioUnitario") val mtoPrecioUnitario: Double
)