package entidades

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class trabajador(@SerializedName("idTrabajador") var idTrabajador: Int? = null,
                      @SerializedName("tipoTrabajador") var tipoTrabajador: tipoTrabajador? = null,
                      @SerializedName("nombreTrabajador") var nombreTrabajador: String? = null,
                      @SerializedName("apellidoTrabajador") var apellidoTrabajador: String? = null,
                      @SerializedName("usuarioTrabajador") val usuarioTrabajador: String? = null,
                      @SerializedName("passwordTrabajador") var passwordTrabajador: String? = null,
                      @SerializedName("estadoTrabajador") var estadoTrabajador: Boolean? = null):Serializable
