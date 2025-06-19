package entidades

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class tipoTrabajador(@SerializedName("idTipoTrabajador") var idtipoTrabajador: Int,
                          @SerializedName("descripcionTipoTrabajador") var descripciontipoTrabajador: String):Serializable
