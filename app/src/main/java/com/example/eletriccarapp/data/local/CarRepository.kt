package com.example.eletriccarapp.data.local

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_BATERIA
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_POTENCIA
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_PRECO
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_RECARGA
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO
import com.example.eletriccarapp.domain.Carro

//injetando
class CarRepository(private val context: Context) {

    fun save(carro: Carro): Boolean? {
        var isSaved = false
        try {

            findCarById(1)

            //pega o contexto em que a gente está
            val dbHelper = CarsDbHelper(context)
            //abra uma forma de escrever nele
            val db = dbHelper.writableDatabase

            //gravar os dados na hora que usuario clicar em favorito
            val values = ContentValues().apply {
                put(COLUMN_NAME_PRECO, carro.preco)
                put(COLUMN_NAME_BATERIA, carro.bateria)
                put(COLUMN_NAME_POTENCIA, carro.potencia)
                put(COLUMN_NAME_RECARGA, carro.recarga)
                put(COLUMN_NAME_URL_PHOTO, carro.urlPhoto)
            }
            //conseguir salvar as informações no banco de dados

            val inserted = db?.insert(CarrosContract.CarEntry.TABLE_NAME, null, values)

            if (inserted != null) {
                isSaved = true

            }
        } catch (ex: Exception) {
            ex.message?.let { Log.e("Erro ao inserir -> ", it) }
        }

        return isSaved
    }

    //buscar o carro pelo id
    fun findCarById(id: Int) {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.readableDatabase


        //LISTAGEM das colunas a serem exibidas no resultado da Query
        val colums = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_PRECO,
            COLUMN_NAME_BATERIA,
            COLUMN_NAME_POTENCIA,
            COLUMN_NAME_RECARGA,
            COLUMN_NAME_URL_PHOTO
        )

        //interrogação é o que a gente vai informar depois
        val filter = "${BaseColumns._ID} = ?"
        val filterValues = arrayOf(id.toString())
        val cursor = db.query(
            CarrosContract.CarEntry.TABLE_NAME, //NOME DA TABELA
            colums,
            filter,
            filterValues,
            null,
            null,
            null
        )
    }
}