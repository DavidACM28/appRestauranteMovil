package com.example.ellimonysucausa

import Adapters.MesaAdapter
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.ellimonysucausa.databinding.ActivityHomeBinding
import entidades.trabajador

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE)

        val trabajador = intent.getSerializableExtra("trabajador", trabajador::class.java)
        val token = intent.getStringExtra("token")
        // 1. Mostrar nombre de usuario
        binding.tvGreeting.text = "Bienvenido ${trabajador?.nombreTrabajador}"

        // 2. Configurar Grid de Mesas
        val mesas = listOf("Mesa 1", "Mesa 2", "Mesa 3", "Mesa 4", "Mesa 5", "Mesa 6","Mesa 7","Mesa 8", "Mesa 9", "Mesa 10", "Mesa 11", "Mesa 12")
        binding.gridMesas.adapter = MesaAdapter(this, mesas)

        binding.gridMesas.setOnItemClickListener { adapterView, view, position, l ->
            intent = Intent(this, CategoriesActivity::class.java)
            intent.putExtra("trabajador", trabajador)
            intent.putExtra("token", token)
            intent.putExtra("mesa", position+1)
            startActivity(intent)
        }

        // 3. Bottom Navigation Actions
        findViewById<ImageButton>(R.id.btnUser).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnCategories).setOnClickListener {
            intent = Intent(this, CategoriesActivity::class.java)
            intent.putExtra("trabajador", trabajador)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            // Ya estamos en Home, no hacemos nada o recargamos
        }

        findViewById<ImageButton>(R.id.btnCart).setOnClickListener {
            intent = Intent(this, CarritoActivity::class.java)
            intent.putExtra("trabajador", trabajador)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }

}
