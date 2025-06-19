package entidades

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class producto(@SerializedName("idProducto") var idProducto:Int? = null,
                    @SerializedName("categoria") var categoria:categoria? = null,
                    @SerializedName("nombreProducto") var nombreProducto:String? = null,
                    @SerializedName("precioProducto") var precioProducto:Double? = null,
                    @SerializedName("estadoProdcuto") var estadoProducto:Boolean? = null):Serializable
