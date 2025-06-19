package entidades

import com.google.gson.annotations.SerializedName

data class detalle(@SerializedName("pedido") var pedido:pedidoResponse,
                   @SerializedName("producto") var producto: producto,
                   @SerializedName("estadoPedido") var estadoPedido: estadoPedido,
                   @SerializedName("precioUnidad") var precioUnidad: Double,
                   @SerializedName("observacionDetalle") var observacionDetalle: String)
