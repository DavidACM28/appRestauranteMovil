package entidades

import com.google.gson.annotations.SerializedName

data class tipoPago(
    @SerializedName("idTipoPago") var idTipoPago:Int? = null,
    @SerializedName("descripcionTipoPago") var descripcionTipoPago:String? = null
)
