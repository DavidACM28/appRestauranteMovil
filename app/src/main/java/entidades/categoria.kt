package entidades

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class categoria(@SerializedName("idCategoria") var idCategoria:Int,
                     @SerializedName("nombreCategoria") var nombreCategoria:String,
                     @SerializedName("estadoCategoria") var estadocategoria:Boolean):Serializable
