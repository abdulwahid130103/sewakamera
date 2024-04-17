package com.polinema.sewakamera.Model

data class SubKeranjang(
    val id_product : Int = 0,
    val nama_produk : String = "",
    val image : String = "",
    val type : String = "",
    val harga : Int = 0,
    val qty : Int = 0,
    val deskripsi : String = "",
    var isChecked: Boolean = false
)
