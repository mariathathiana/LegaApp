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
                Lega("sannicasio", R.string.lega_name_sannicasio, R.string.lega_date_sannicasio, R.drawable.ic_sannicasio)
            )
        }

        fun getById(id: String) : Lega {
            return getAll().first { it.id == id }
        }
    }

}