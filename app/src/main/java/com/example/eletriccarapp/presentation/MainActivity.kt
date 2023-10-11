package com.example.eletriccarapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.data.CarFactory
import com.example.eletriccarapp.presentation.adapter.CarAdapter

class MainActivity : AppCompatActivity() {

    lateinit var btnCalcular: Button
    lateinit var listaCarros: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupListeners()
        setupList()
    }

    fun setupView() {
        btnCalcular = findViewById(R.id.btn_calcular)
        listaCarros = findViewById(R.id.rv_lista_carros)
    }

    fun setupList() {
        val adapter = CarAdapter(CarFactory.list)
        //falando pro layout que ele tá trabalhando com uma lista e que ele vai suar o manager como recurso.
        //no xml
        listaCarros.adapter = adapter
    }


    fun setupListeners() {
        btnCalcular.setOnClickListener {
            // calcular()
            startActivity(Intent(this, CalcularAutonomiaActivity::class.java))
        }

    }

}