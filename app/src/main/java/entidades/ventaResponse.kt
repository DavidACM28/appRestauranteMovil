package entidades

import com.google.gson.annotations.SerializedName

data class ventaResponse(
    @SerializedName("idVenta") var idVenta:Int,
    @SerializedName("pedido") var pedidoResponse: pedidoResponse? = null,
    @SerializedName("tipoPago") var tipoPago: tipoPago? = null,
    @SerializedName("tipoComprobante") var tipoComprobante: tipoComprobante? = null
)
