package com.example.ellimonysucausa

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ellimonysucausa.databinding.ActivityProfileBinding
import entidades.trabajador

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor:SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE)
        val trabajador = intent.getSerializableExtra("trabajador", trabajador::class.java)
        val token = intent.getStringExtra("token")
        editor = sharedPreferences.edit()
        binding.tvNombreTrabajador.setText(trabajador!!.nombreTrabajador)
        binding.tvUsuarioTrabajador.setText(trabajador.usuarioTrabajador)
        binding.tvApellidoTrabajador.setText(trabajador.apellidoTrabajador)
        binding.btnCerrarSesion.setOnClickListener(){
            borrarDatos()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnHome.setOnClickListener(){
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("trabajador", trabajador)
            startActivity(intent)
        }
        binding.btnCategories.setOnClickListener(){
            val intent = Intent(this, CategoriesActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("trabajador", trabajador)
            startActivity(intent)
        }
        binding.btnCart.setOnClickListener(){
            val intent = Intent(this, CarritoActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("trabajador", trabajador)
            startActivity(intent)
        }
    }
    private fun borrarDatos(){
        editor.remove("savelogin")
        editor.remove("username")
        editor.remove("password")
        editor.remove("horalogin")
        editor.remove("tiempolimite")
            .apply()
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }
}