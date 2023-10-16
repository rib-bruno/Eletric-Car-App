package com.example.eletriccarapp.presentation

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.data.CarFactory
import com.example.eletriccarapp.presentation.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class CarFragment : Fragment() {

    lateinit var fabCalcular: FloatingActionButton
    lateinit var listaCarros: RecyclerView

    //a view ainda tá sendo desenhada
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupList()
        setupListeners()
    }

    //não estamos na activity, passar view como parâmetro pra suar o find
    fun setupView(view: View) {
        view.apply {
            fabCalcular = findViewById(R.id.fab_calcular)
            listaCarros = findViewById(R.id.rv_lista_carros)
        }
    }

    fun setupList() {
        val adapter = CarAdapter(CarFactory.list)
        //falando pro layout que ele tá trabalhando com uma lista e que ele vai suar o manager como recurso.
        //no xml
        listaCarros.adapter = adapter
    }

    fun setupListeners() {
        fabCalcular.setOnClickListener {
            //não conseguimos usar o this pra acessar o contexto num fragment
            startActivity(Intent(context, CalcularAutonomiaActivity::class.java))
        }

    }

    inner class MyTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask", "Iniciando...")
        }

        override fun doInBackground(vararg url: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val urlBase = URL(url[0])
                //abrindo conexão com a internet
                urlConnection = urlBase.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 60000
                urlConnection.readTimeout = 60000

                //dados que são trafegados pela internet
                var inString = streamToString(urlConnection.inputStream)
                publishProgress(inString)

            } catch (ex: Exception) {
                Log.e("Erro", "Error ao realizar processamento...")

            } finally {
                if (urlConnection != null) {
                    urlConnection?.disconnect()
                }
            }
            return " "

        }

        //quando os dados estão sendo atualizados
        override fun onProgressUpdate(vararg values: String?) {
            try{
                //só vai pegar se for não nulo
                var json: JSONObject
                values[0]?.let {
                    json = JSONObject(it)
                }

            } catch (ex: Exception) {

            }
        }

        fun streamToString(inputStream: InputStream): String {
            val bufferReader = BufferedReader(InputStreamReader(inputStream))

            var line: String
            var result = ""

            try {
                do {
                    line = bufferReader.readLine()
                    line?.let {
                        result += line
                    }
                } while (line != null)
            } catch (ex: Exception) {
                Log.e("Erro", "Erro ao parcelar Stream")
            }

            return result

        }

    }

}