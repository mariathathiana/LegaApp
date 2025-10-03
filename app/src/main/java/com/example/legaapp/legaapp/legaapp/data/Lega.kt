package com.example.legaapp.legaapp.legaapp.data

import com.example.legaapp.legaapp.legaapp.R

class Lega (
    val id: String,
    val name: Int,
    val dates: Int,
    val sign: Int

) {
    companion object {
        fun getAll() : List<Lega> {
            return listOf(
                Lega("sannicasio", R.string.lega_name_sannicasio, R.string.lega_date_sannicasio, R.drawable.ic_sannicasio),
                Lega("leganescentral", R.string.lega_name_leganescentral, R.string.lega_date_leganescentral, R.drawable.ic_leganescentral),
                Lega("hospitalseveroochoa", R.string.lega_name_hospitalseveroochoa, R.string.lega_date_hospitalseveroochoa, R.drawable.ic_hospital)

            )
        }

        fun getById(id: String) : Lega {
            return getAll().first { it.id == id }
        }
    }

}