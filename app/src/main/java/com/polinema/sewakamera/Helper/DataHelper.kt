package com.polinema.sewakamera.Helper

import java.text.NumberFormat
import java.util.Locale

class DataHelper {

    fun formatRupiah(value: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(value.toLong())
    }
}