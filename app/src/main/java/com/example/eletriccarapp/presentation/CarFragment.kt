package com.example.eletriccarapp.presentation

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
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
    lateinit var noInternetImage: ImageView
    lateinit var noInternetText : TextView

    var carrosArray: ArrayList<Carro> = ArrayList()

    //a view ainda tá sendo desenhada
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false)
    }

    //só vai passar uma vez pro usuário
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        //setupList()
        setupListeners()


    }

    override fun onResume() {
        super.onResume()
        //check se tem internet - chamar servico
        if(checkForInternet(context)) {
            callService()
        } else {
            emptyState()
        }
    }

    fun emptyState() {
        progress.isVisible = false
        listaCarros.isVisible = false
        noInternetImage.isVisible = true
        noInternetText.isVisible = true
    }

    //não estamos na activity, passar view como parâmetro pra usar o find/referencia no layout
    fun setupView(view: View) {
        view.apply {
            fabCalcular = findViewById(R.id.fab_calcular)
            listaCarros = findViewById(R.id.rv_lista_carros)
            progress = findViewById((R.id.pb_loader))
            noInternetImage = findViewById(R.id.iv_empty_state)
            noInternetText = findViewById(R.id.tv_no_wifi)

        }
    }

    fun setupList() {
        val carroAdapter = CarAdapter(carrosArray)
         listaCarros.apply {
             isVisible = true
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
        progress.isVisible = true
        MyTask().execute(urlBase)


    }

    fun checkForInternet (context: Context?) : Boolean {
        //serviço de conectividade
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //recuperar aqui - se tem internet ativa
            val network = connectivityManager.activeNetwork ?: return false

            //se tem capacidade de conexão
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            //quando não for android M
            @Suppress("DEPRECATION")
            val networkInfo =  connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }

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

                progress.isVisible = false
                noInternetImage.isVisible = false
                noInternetText.isVisible = false
                //quando tiver populado/percorrer as informações, listar os dados com as informações que tiver disponível
                setupList()
            } catch (ex: Exception) {
                Log.e("Erro ->", ex.message.toString())

            }
        }

    }

}