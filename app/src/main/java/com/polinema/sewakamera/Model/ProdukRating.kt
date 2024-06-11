package com.polinema.sewakamera.Model

data class ProdukRating(
    val id:Int = 0,
    val nama_produk:String = "",
    val image:String = "",
    val harga:Int = 0,
    var rating: Float = 0f,
    var deskripsi : String = ""
)
