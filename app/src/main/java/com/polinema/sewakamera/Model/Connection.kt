package com.polinema.sewakamera.Model

class Connection {

    var url_root = "http://192.168.28.161/sewakameranew/sewakameranew/public/"
    var url_login = "${url_root}api/login"
    var url_cover_produk = "${url_root}api/get_produk"
    var url_cover_produk_terbaru = "${url_root}api/get_produk_terbaru"
    var url_cover_produk_terlaris = "${url_root}api/get_produk_terlaris"

    var url_get_category = "${url_root}api/get_category"
    var url_insert_keranjang = "${url_root}api/insert_keranjang"
    var url_get_detail_produk = "${url_root}api/get_detail_produk"
    var url_get_keranjang = "${url_root}api/get_keranjang"
    var url_get_mitra = "${url_root}api/get_mitra"
    var url_delete_keranjang = "${url_root}api/delete_keranjang"
}