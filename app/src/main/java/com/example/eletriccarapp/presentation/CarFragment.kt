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
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.data.CarFactory
import com.example.eletriccarapp.domain.Carro
import com.example.eletriccarapp.presentation.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class CarFragment : Fragment() {

    lateinit var fabCalcular: FloatingActionButton
    lateinit var listaCarros: RecyclerView
    lateinit var progress: ProgressBar

    var carrosArray: ArrayList<Carro> = ArrayList()

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
        //setupList()
        setupListeners()
        callService()
    }

    //não estamos na activity, passar view como parâmetro pra suar o find
    fun setupView(view: View) {
        view.apply {
            fabCalcular = findViewById(R.id.fab_calcular)
            listaCarros = findViewById(R.id.rv_lista_carros)
            progress = findViewById((R.id.pb_loader))
        }
    }

    fun setupList() {
        val carroAdapter = CarAdapter(carrosArray)
         listaCarros.apply {
             visibility = View.VISIBLE
             //falando pro layout que ele tá trabalhando com uma lista e que ele vai usar o manager como recurso.
             //no xml
             adapter = carroAdapter
         }
    }

    fun setupListeners() {
        fabCalcular.setOnClickListener {
            //não conseguimos usar o this pra acessar o contexto num fragment
            startActivity(Intent(context, CalcularAutonomiaActivity::class.java))
            //MyTask().execute("https://github.com/igorbag/cars-api/blob/main/cars.json")
        }
    }
    fun callService() {
        val urlBase = "https://igorbag.github.io/cars-api/cars.json"
        MyTask().execute(urlBase)


    }

    inner class MyTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask", "Iniciando...")
            progress.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg url: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val urlBase = URL(url[0])
                //abrindo conexão com a internet
                urlConnection = urlBase.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 60000
                urlConnection.readTimeout = 60000
                //meu endpoint aceite esse tipo de informação
                urlConnection.setRequestProperty(
                    "Accept",
                    "application/json"
                )
                //verificar se o serviço está respondendo ok
                val responseCode = urlConnection.responseCode

                if(responseCode == HttpURLConnection.HTTP_OK) {

                    //dados que são trafegados pela internet
                    var response = urlConnection.inputStream.bufferedReader().use { it.readText() }
                    publishProgress(response)
                } else {
                    Log.d("Erro", "Serviço indisponível no momento!")
                }
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
            try {
                val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray

                //percorrer de zero até a quantidade da lista que tem no json
                for (i in 0 until jsonArray.length()) {
                    val id = jsonArray.getJSONObject(i).getString("id")
                    Log.d("ID ->", id)

                    val preco = jsonArray.getJSONObject(i).getString("preco")
                    Log.d("Preco ->", preco)

                    val bateria = jsonArray.getJSONObject(i).getString("bateria")
                    Log.d("Bateria ->", bateria)

                    val potencia = jsonArray.getJSONObject(i).getString("potencia")
                    Log.d("Potencia ->", preco)

                    val recarga = jsonArray.getJSONObject(i).getString("recarga")
                    Log.d("Recarga ->", recarga)

                    val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")
                    Log.d("urlPhoto ->", urlPhoto)

                    val model = Carro(
                        id = id.toInt(),
                        preco = preco,
                        bateria = bateria,
                        potencia = potencia,
                        recarga = recarga,
                        urlPhoto = urlPhoto
                    )
                    carrosArray.add(model)
                }

                progress.visibility = View.GONE
                //quando tiver populado/percorrer as informações, listar os dados com as informações que tiver disponível
                setupList()
            } catch (ex: Exception) {
                Log.e("Erro ->", ex.message.toString())

            }
        }

    }

}