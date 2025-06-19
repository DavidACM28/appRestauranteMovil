package com.example.ellimonysucausa

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ellimonysucausa.databinding.ActivityLoginBinding
import entidades.dtoLogin
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utilidades.APIService


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences:SharedPreferences
    private lateinit var editor:SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        binding.btnLogin.setOnClickListener(){
            login(binding.etUsername.text.toString(), binding.etPassword.text.toString())
        }
        val savelogin: Boolean = sharedPreferences.getBoolean("savelogin", false)
        if (savelogin && validarTiempo()){
            login(sharedPreferences.getString("username", "")!!, sharedPreferences.getString("password", "")!!)
        }
    }

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }

    private fun login(usuario: String, password: String){
        val dtoLogin = dtoLogin(usuario, password)

        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val call = getRetrofit().create(APIService::class.java).login(dtoLogin, "api/auth/login")
            val body = call.body()
            runOnUiThread {
                if (call.isSuccessful){
                    editor.putBoolean("savelogin", true)
                    editor.putString("username", usuario)
                    editor.putString("password", password)
                    editor.putLong("horalogin", System.currentTimeMillis())
                    editor.putLong("tiempolimite", 43200000)
                    editor.commit()
                    val trabajador = body?.trabajador
                    val token = body!!.token
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.putExtra("trabajador", trabajador)
                    intent.putExtra("token", token)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun validarTiempo():Boolean{
        val tiempolimite = sharedPreferences.getLong("tiempolimite", 0)
        val tiempologeado = System.currentTimeMillis() - sharedPreferences.getLong("horalogin", 0)
        if(tiempologeado<=tiempolimite)
            return true
        borrarDatos()
        return false
    }

    private fun borrarDatos(){
        editor.remove("savelogin")
        editor.remove("username")
        editor.remove("password")
        editor.remove("horalogin")
        editor.remove("tiempolimite")
            .apply()
        Toast.makeText(this, "Tu sesión ha expirado", Toast.LENGTH_SHORT).show()
    }
}
