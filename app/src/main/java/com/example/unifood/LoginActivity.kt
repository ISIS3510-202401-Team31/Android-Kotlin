package com.example.unifood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signUpLinkTextView: TextView = findViewById(R.id.signUpLinkTextView)
        val loginButton: Button = findViewById(R.id.loginButton)
        val emailEditText: EditText = findViewById(R.id.emailEditText)

        signUpLinkTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (email.endsWith("@uniandes.edu.co")) {
                Toast.makeText(this, "User authenticated successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid Uniandes email address", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

