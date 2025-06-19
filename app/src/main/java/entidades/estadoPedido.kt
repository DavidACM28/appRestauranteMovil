package entidades

import com.google.gson.annotations.SerializedName

data class estadoPedido(@SerializedName("idEstadoPedido") var idEstadoPedido: Int,
                        @SerializedName("descripcionEstadoPedido") var descripcionEstadoPedido: String)
