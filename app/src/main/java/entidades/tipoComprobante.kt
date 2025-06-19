package entidades

import com.google.gson.annotations.SerializedName

data class tipoComprobante(
    @SerializedName("idTipoComprobante") var idTipoComprobante:Int? = null,
    @SerializedName("descripcionTipoComprobante") var descripcionTipoComprobante:String? = null
)
