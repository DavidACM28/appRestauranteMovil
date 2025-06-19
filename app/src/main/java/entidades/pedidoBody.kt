package entidades

import com.google.gson.annotations.SerializedName

data class pedidoBody(@SerializedName("trabajador") var trabajador: trabajador? = null,
                      @SerializedName("estadoPedido") var estadoPedido: estadoPedido? = null,
                      @SerializedName("tipoPedido") var tipoPedido: String? = null,
                      @SerializedName("fechaPedido") var fechaPedido: String? = null,
                      @SerializedName("mesaPedido") var mesaPedido: Int? = null,
                      @SerializedName("totalPedido") var totalPedido: Double? = null)
