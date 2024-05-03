package com.polinema.sewakamera.Model

data class Keranjang(
    val id_keranjang: Int = 0,
    val id_mitra: Int = 0,
    val nama_mitra: String = "",
    val image_mitra: String = "",
    val produk: MutableList<SubKeranjang> = ArrayList(),
    var isChecked: Boolean = false
)
