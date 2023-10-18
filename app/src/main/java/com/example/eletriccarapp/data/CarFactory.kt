package com.example.eletriccarapp.data

import com.example.eletriccarapp.domain.Carro

object CarFactory {

    val list = listOf(
        Carro(
            id = 1,
            preco = "R$ 3000.000,00",
            bateria = "300 kWh",
            potencia = "200cV",
            recarga = "30 min",
            urlPhoto = "www.google.com.br",
            isFavorite = false
        ),
        Carro(
            id = 2,
            preco = "R$ 2000.000,00",
            bateria = "200 kWh",
            potencia = "150cV",
            recarga = "40 min",
            urlPhoto = "www.google.com.br",
            isFavorite = false
        )
    )
}