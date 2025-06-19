package entidadesPago

import com.google.gson.annotations.SerializedName

data class loginBoletaResponse(
    @SerializedName("token") val token: String
)
