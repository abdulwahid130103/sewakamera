package com.polinema.sewakamera.Model

class Connection {

    var url_root = "http://192.168.0.132/sewakameranew/sewakameranew/public/"
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
    var url_update_qty_keranjang = "${url_root}api/update_qty_keranjang"
    var url_create_transaksi_booking = "${url_root}api/create_transaksi_booking"

    var get_booking_transaksi = "${url_root}api/get_booking_transaksi"
    var get_terverifikasi_transaksi = "${url_root}api/get_terverifikasi_transaksi"
    var get_detail_transaksi_bayar = "${url_root}api/get_detail_transaksi_bayar"
    var get_update_transaksi_status = "${url_root}api/get_update_transaksi_status"


}