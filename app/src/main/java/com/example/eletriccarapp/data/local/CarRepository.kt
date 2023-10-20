package com.example.eletriccarapp.data.local

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_BATERIA
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_CAR_ID
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_POTENCIA
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_PRECO
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_RECARGA
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO
import com.example.eletriccarapp.domain.Carro
import retrofit2.http.PUT

//injetando
class CarRepository(private val context: Context) {

    fun save(carro: Carro): Boolean {
        var isSaved = false
        try {
            //pega o contexto em que a gente está
            val dbHelper = CarsDbHelper(context)
            //abra uma forma de escrever nele
            val db = dbHelper.writableDatabase

            //gravar os dados na hora que usuario clicar em favorito
            val values = ContentValues().apply {
                put(COLUMN_NAME_CAR_ID, carro.id)
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
    fun findCarById(id: Int): Carro {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.readableDatabase


        //LISTAGEM das colunas a serem exibidas no resultado da Query
        val colums = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_CAR_ID,
            COLUMN_NAME_PRECO,
            COLUMN_NAME_BATERIA,
            COLUMN_NAME_POTENCIA,
            COLUMN_NAME_RECARGA,
            COLUMN_NAME_URL_PHOTO
        )

        //interrogação é o que a gente vai informar depois
        val filter = "$COLUMN_NAME_CAR_ID = ?"
        val filterValues = arrayOf(id.toString())
        //retorno de cursor
        val cursor = db.query(
            CarrosContract.CarEntry.TABLE_NAME, //NOME DA TABELA
            colums, //colunas a serem exibidas
            filter, // where (filtro)
            filterValues, //valor do where, substituindo o parâmetro interroação (?)
            null,
            null,
            null
        )

        var itemId: Long = 0
        var preco = ""
        var bateria = ""
        var potencia = ""
        var recarga = ""
        var urlPhoto = ""

        //cursor enquanto tiver valor pro próximo
        with(cursor) {
            //enquanto tiver cursor -> os registros
            while (moveToNext()) {
                //ids que estão sendo persistidos - id que está gravado
                itemId = getLong(getColumnIndexOrThrow(COLUMN_NAME_CAR_ID))
                Log.d("ID -> ", itemId.toString())

                preco = getString(getColumnIndexOrThrow(COLUMN_NAME_PRECO))
                Log.d("preco -> ", preco)

                bateria = getString(getColumnIndexOrThrow(COLUMN_NAME_BATERIA))
                Log.d("bateria -> ", bateria)

                potencia = getString(getColumnIndexOrThrow(COLUMN_NAME_POTENCIA))
                Log.d("potência -> ", potencia)

                recarga = getString(getColumnIndexOrThrow(COLUMN_NAME_RECARGA))
                Log.d("recarga -> ", recarga)

                urlPhoto = getString(getColumnIndexOrThrow(COLUMN_NAME_URL_PHOTO))
                Log.d("urlPhoto -> ", urlPhoto)
            }
        }
        cursor.close()
        return Carro(
            id = itemId.toInt(),
            preco = preco,
            bateria = bateria,
            potencia = potencia,
            recarga = recarga,
            urlPhoto = urlPhoto,
            isFavorite = true
        )
    }

    fun saveIfNotExist(carro: Carro) {
        //primeiro, ir no banco de dados
        val car = findCarById(carro.id)
        if (car.id == ID_WHEN_NO_CAR) {
            save(carro)
        }
    }

    fun getAll(): List<Carro> {

        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.readableDatabase


        //LISTAGEM das colunas a serem exibidas no resultado da Query
        val colums = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_CAR_ID,
            COLUMN_NAME_PRECO,
            COLUMN_NAME_BATERIA,
            COLUMN_NAME_POTENCIA,
            COLUMN_NAME_RECARGA,
            COLUMN_NAME_URL_PHOTO
        )

        //retorno de cursor
        val cursor = db.query(
            CarrosContract.CarEntry.TABLE_NAME, //NOME DA TABELA
            colums, //colunas a serem exibidas
            null, // where (filtro)
            null, //valor do where, substituindo o parâmetro interroação (?)
            null,
            null,
            null
        )

        val carros = mutableListOf<Carro>()

        //cursor enquanto tiver valor pro próximo
        with(cursor) {
            //enquanto tiver cursor -> os registros
            while (moveToNext()) {
                //ids que estão sendo persistidos - id que está gravado
                val itemId = getLong(getColumnIndexOrThrow(COLUMN_NAME_CAR_ID))
                Log.d("ID -> ", itemId.toString())

                val preco = getString(getColumnIndexOrThrow(COLUMN_NAME_PRECO))
                Log.d("preco -> ", preco)

                val bateria = getString(getColumnIndexOrThrow(COLUMN_NAME_BATERIA))
                Log.d("bateria -> ", bateria)

                val potencia = getString(getColumnIndexOrThrow(COLUMN_NAME_POTENCIA))
                Log.d("potência -> ", potencia)

                val recarga = getString(getColumnIndexOrThrow(COLUMN_NAME_RECARGA))
                Log.d("recarga -> ", recarga)

                val urlPhoto = getString(getColumnIndexOrThrow(COLUMN_NAME_URL_PHOTO))
                Log.d("urlPhoto -> ", urlPhoto)

                carros.add(
                    Carro(
                        id = itemId.toInt(),
                        preco = preco,
                        bateria = bateria,
                        potencia = potencia,
                        recarga = recarga,
                        urlPhoto = urlPhoto,
                        isFavorite = true
                    )
                )

            }
        }
        cursor.close()
        return carros
    }

    companion object {
        const val ID_WHEN_NO_CAR = 0
    }


}