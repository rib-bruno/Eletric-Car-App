package com.example.eletriccarapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.eletriccarapp.R

class MainActivity : AppCompatActivity() {

    lateinit var btnCalcular: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupListeners()
    }

    fun setupView() {
        btnCalcular = findViewById(R.id.btn_calcular)
    }
    fun setupListeners() {
        btnCalcular.setOnClickListener {
            // calcular()
            startActivity(Intent(this, CalcularAutonomiaActivity::class.java))
        }
    }

}