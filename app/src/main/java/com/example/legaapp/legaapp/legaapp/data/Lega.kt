package com.example.legaapp.legaapp.legaapp.data

import com.example.legaapp.legaapp.legaapp.R

class Lega (
    val id: String,
    val name: Int,
    val sign: Int

) {
    companion object {
        fun getAll() : List<Lega> {
            return listOf(
                Lega("sannicasio", R.string.lega_name_sannicasio, R.drawable.ic_sannicasio),
                Lega("leganescentral", R.string.lega_name_leganescentral, R.drawable.ic_leganescentral),
                Lega("hospitalseveroochoa", R.string.lega_name_hospitalseveroochoa, R.drawable.ic_hospital),
                Lega("casadelreloj", R.string.lega_name_casadelreloj, R.drawable.ic_casadelreloj),
                Lega("julianbesteiro", R.string.lega_name_julianbesteiro, R.drawable.ic_julianbesteiro)

            )
        }

        fun getById(id: String?) : Lega {
            return getAll().first { it.id == id }
        }
    }

}