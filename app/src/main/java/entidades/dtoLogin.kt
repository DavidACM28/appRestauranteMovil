package entidades

import com.google.gson.annotations.SerializedName

data class dtoLogin(@SerializedName("username") var usuario: String,
                    @SerializedName("password") var password: String)
