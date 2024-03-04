package com.example.unifood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleTextViewIntro.text = "Unifood"
        subtitleTextViewIntro.text = "It's all here"
        loginButtonIntro.text = "Login"
        signUpButtonIntro.text = "Sign Up"
    }
}
