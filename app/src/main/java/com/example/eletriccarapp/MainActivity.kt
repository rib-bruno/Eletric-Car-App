package com.example.eletriccarapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    lateinit var preco: EditText
    lateinit var calcular: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpView()
        setupListeners()
    }

    fun setUpView() {
        calcular = findViewById(R.id.btn_calcular)
        preco = findViewById(R.id.et_preco_kwh)
    }

    fun setupListeners() {
        calcular.setOnClickListener {
            val textoDigitado = preco.text.toString()
            Log.d("texto digitado", textoDigitado)
        }
    }
}