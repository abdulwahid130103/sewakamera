package com.polinema.sewakamera.Model

data class Produk(
    val id:Int = 0,
    val id_mitra:Int = 0,
    val id_category:String = "",
    val nama_produk:String = "",
    val image:String = "",
    val type: String = "",
    val harga:Int = 0,
    val stok:Int = 0,
    val deskripsi:String = ""
)
