package com.example.legaapp.legaapp.legaapp.data

import com.example.legaapp.legaapp.legaapp.R

class Lega (
    val id: String,
    val name: Int,
    val sign: Int,
    val latitude: Double,
    val longitude: Double

) {
    companion object {
        fun getAll() : List<Lega> {
            return listOf(
                Lega("sannicasio", R.string.lega_name_sannicasio, R.drawable.ic_sannicasio, 40.336128, -3.775797),
                Lega("leganescentral", R.string.lega_name_leganescentral, R.drawable.ic_leganescentral, 40.328681, -3.771033),
                Lega("hospitalseveroochoa", R.string.lega_name_hospitalseveroochoa, R.drawable.ic_hospital, 40.321805, -3.768602),
                Lega("casadelreloj", R.string.lega_name_casadelreloj, R.drawable.ic_casadelreloj, 40.326654, -3.759461),
                Lega("julianbesteiro", R.string.lega_name_julianbesteiro, R.drawable.ic_julianbesteiro, 40.334691, -3.752399),
                Lega("elcarrascal",R.string.lega_name_elcarrascal, R.drawable.ic_elcarrascal, 40.336638, -3.739963)

            )
        }

        fun getById(id: String?) : Lega {
            return getAll().first { it.id == id }
        }
    }

}