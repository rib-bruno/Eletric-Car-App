package com.example.eletriccarapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.domain.Carro


//passar a listagem de carros
class CarAdapter(private val carros: List<Carro>, private val isFavoriteScreen :Boolean = false ) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    //callback para acessar no fragment
    var carItemLister : (Carro) -> Unit = {}

    //recuperar o layout criadocria uma nova view.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carro_item, parent, false)

        return ViewHolder(view)
    }

    //Pega o conteudo da view e troca pela informaçao de item de uma lista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.preco.text = carros[position].preco
        holder.bateria.text = carros[position].bateria
        holder.potencia.text = carros[position].potencia
        holder.recarga.text = carros[position].recarga

        //se for favorito de cara vai settar o star
        if(isFavoriteScreen) {
            holder.favorito.setImageResource(R.drawable.ic_star_selected)
        }

        holder.favorito.setOnClickListener {
            val carro = carros[position]
            carItemLister(carro)
            setupFavorite(carro, holder)
        }
    }

    private fun setupFavorite(
        carro: Carro,
        holder: ViewHolder
    ) {
            //primeira vez que clickar, ele terá que ficar o contrário do que foi setado(false), na segunda será contrário de true
            carro.isFavorite = !carro.isFavorite

            if (carro.isFavorite) {
                //se estiver farotio, ele estará com outro ícone
                holder.favorito.setImageResource(R.drawable.ic_star_selected)
            } else {
                holder.favorito.setImageResource(R.drawable.ic_star_2) }
    }

    //pega a quantidade de itens da lista
    override fun getItemCount(): Int = carros.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val preco: TextView
        val bateria: TextView
        val potencia: TextView
        val recarga: TextView
        val favorito : ImageView

        //dentro do escopo da view
        init {
            view.apply {
                preco = findViewById(R.id.tv_preco_value)
                bateria = findViewById(R.id.tv_preco_bateria_value)
                potencia = findViewById(R.id.tv_potencia_value)
                recarga = findViewById(R.id.tv_recarga_value)
                favorito = findViewById(R.id.iv_favorite)
            }
        }

    }
}

