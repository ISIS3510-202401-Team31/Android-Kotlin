package com.example.unifood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signUpLinkTextView: TextView = findViewById(R.id.signUpLinkTextView)
        val loginButton: Button = findViewById(R.id.loginButton)

        signUpLinkTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            // Aquí puedes poner el código para realizar la autenticación del usuario
        }
    }
}
