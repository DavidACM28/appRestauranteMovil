package com.example.ellimonysucausa

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Configuración del logo (opcional)
        val logo = findViewById<ImageView>(R.id.logo)
        logo?.setImageResource(R.drawable.logolimonysucausa)

        // Handler para el retraso
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToLogin()
        }, SPLASH_DELAY)
    }
    private fun navigateToLogin() {
        try {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Evita que el usuario pueda volver atrás con el botón back
        } catch (e: Exception) {
            // Manejo de errores por si LoginActivity no existe
            e.printStackTrace()
            // Podrías abrir otra actividad como fallback
            // startActivity(Intent(this, MainActivity::class.java))
        }
    }
    override fun onDestroy() {
        // Limpiar el Handler para evitar memory leaks
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}

