package entidades

import com.google.gson.annotations.SerializedName

data class loginResponse(@SerializedName("accessToken") var token: String,
                         @SerializedName("tokenType") var tokenType: String,
                         @SerializedName("trabajador") var trabajador: trabajador)
