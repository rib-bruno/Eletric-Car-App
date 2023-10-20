package com.example.eletriccarapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.data.local.CarRepository
import com.example.eletriccarapp.domain.Carro
import com.example.eletriccarapp.presentation.adapter.CarAdapter

class FavoriteFragment: Fragment() {

    lateinit var listaCarrosFavoritos : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        //na hora que a tela for criada, leia meus carros
        setupList()
    }

    private fun getCarsOnLocalDb(): List<Carro> {
        val repository = CarRepository(requireContext())
        val carList = repository.getAll()
        return carList
    }

    fun setupView(view: View) {
        view.apply {
            listaCarrosFavoritos = findViewById(R.id.rv_lista_carros_favorite)
        }
    }

    fun setupList() {
        val cars = getCarsOnLocalDb()
        val carroAdapter = CarAdapter(cars)
        listaCarrosFavoritos.apply {
            isVisible = true
            //falando pro layout que ele tá trabalhando com uma lista e que ele vai usar o manager como recurso.
            //no xml
            adapter = carroAdapter
        }
        carroAdapter.carItemLister = { carro ->
         //   val isSaved = CarRepository(requireContext()).saveIfNotExist(carro)
            //@TODO IMPLEMENTAR O DELETE NO BANCO DE DADOS -NO CAR REPOSITORY, QUERY
        }
    }


}