package com.example.eletriccarapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R

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
        var dados = arrayOf(
            "Cupcacke",
            "Donut",
            "Froyo",
            "Gingerbread",
            "Honeycomb",
            "Ice Cream Sandwich",
            "Jelly Bean"
        )
       // val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dados)
      //  lista.adapter = adapter
    }


    fun setupListeners() {
        btnCalcular.setOnClickListener {
            // calcular()
            startActivity(Intent(this, CalcularAutonomiaActivity::class.java))
        }

    }

}