package com.polinema.sewakamera.View.Fragment

import KeranjangAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Keranjang
import com.polinema.sewakamera.Model.SubKeranjang
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.DeatilTransaksiActivity
import com.polinema.sewakamera.View.Activity.HomeActivity
import com.polinema.sewakamera.View.Auth.LoginActivity
import com.polinema.sewakamera.databinding.FragmentKeranjangBinding
import org.json.JSONArray
import org.json.JSONException

class KeranjangFragment : Fragment() {

    private lateinit var b : FragmentKeranjangBinding
    private lateinit var thisParent : HomeActivity
    private lateinit var v : View

    private lateinit var Session: SessionSewa
    lateinit var cartAdapter: KeranjangAdapter
    lateinit var animationView: LottieAnimationView
    lateinit var totalPriceBagFrag: TextView
    lateinit var itemCart: ArrayList<Keranjang>
    var checkedProducts=  ArrayList<HashMap<String, Any>>()
    lateinit var dataHelper : DataHelper
    var sum:Int = 0

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            totalCheckedData()
            handler.postDelayed(this, 1000)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as HomeActivity
        b = FragmentKeranjangBinding.inflate(layoutInflater)
        v = b.root

        Session = SessionSewa(thisParent)
        dataHelper = DataHelper(thisParent)
        itemCart = arrayListOf()


        cekLogin()



        return v
    }


//    override fun onResume() {
//        super.onResume()
//        val session = SessionSewa(thisParent)
//        session.updateLastActiveTime()
//
//        if (!session.getLogin()) {
//            dataHelper.toast_custom_alert(thisParent, "notifikasi","Anda harus login untuk melihat keranjang")
//            val intent = Intent(thisParent, LoginActivity::class.java)
//            startActivity(intent)
//        }
//    }

    override fun onStart() {
        cekLogin()
        super.onStart()
    }

    fun cekLogin(){
        if (!Session.getLogin()) {
            dataHelper.toast_custom_alert(thisParent, "notifikasi","Anda harus login untuk melihat keranjang")
            val intent = Intent(thisParent, LoginActivity::class.java)
            startActivity(intent)
        }else{
            getCoverProduk()
            b.cartRecView.layoutManager = LinearLayoutManager(context)
            cartAdapter = KeranjangAdapter(activity as Context, itemCart)
            b.cartRecView.adapter = cartAdapter

            b.checkOutBagPage.setOnClickListener{
                val checkedProducts = cartAdapter.getCheckedProductsFromSubKeranjang()

                if(checkedProducts.isNotEmpty()){
                    val mitraIds = checkedProducts.map { it["id_mitra"] }.toSet()
                    if (mitraIds.size == 1) {
                        val intent = Intent(thisParent, DeatilTransaksiActivity::class.java)
                        intent.putExtra("data", checkedProducts)
                        startActivity(intent)
                    } else {
                        dataHelper.toast_custom_alert(thisParent, "notifikasi","Pilih produk dalam 1 toko yang sama !")
                    }
                }else{
                    dataHelper.toast_custom_alert(thisParent, "notifikasi","Pilih terlebih dahulu produk nya !")
                }


            }

            handler.postDelayed(runnable, 1000)
        }
    }


    private fun totalCheckedData(){
        this.checkedProducts = cartAdapter.getCheckedProductsFromSubKeranjang()
        var totalPrice = 0
        var fmRp = DataHelper(thisParent)

        for (product in this.checkedProducts) {
            totalPrice += product["qty"].toString().toInt() * product["harga"].toString().toInt()
        }
        b.totalPriceBagFrag.setText(fmRp.formatRupiah(totalPrice))
    }



    private fun getCoverProduk() {
        val url = Connection()
        var id_user = Session.getUserId().toString().toInt()
        val url_fix = "${url.url_get_keranjang}/${id_user}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    itemCart.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val jsonArrayProduk = jsonObject.getJSONArray("produk")
                        val produkList = mutableListOf<SubKeranjang>()
                        for (j in 0 until jsonArrayProduk.length()) {
                            val jsonObjectProduk = jsonArrayProduk.getJSONObject(j)
                            val produk = SubKeranjang(
                                jsonObjectProduk.getInt("id"),
                                jsonObjectProduk.getInt("id_mitra"),
                                jsonObjectProduk.getInt("id_product"),
                                jsonObjectProduk.getString("nama_produk"),
                                jsonObjectProduk.getString("gambar_url"),
                                jsonObjectProduk.getString("type"),
                                jsonObjectProduk.getInt("harga"),
                                jsonObjectProduk.getInt("qty"),
                                jsonObjectProduk.getString("deskripsi"),
                            )
                            produkList.add(produk)
                        }
                        val dt = Keranjang (
                            id_keranjang = jsonObject.getInt("id"),
                            id_mitra = jsonObject.getInt("id_mitra"),
                            nama_mitra = jsonObject.getString("nama_mitra"),
                            image_mitra = jsonObject.getString("image_mitra"),
                            produk = produkList,
                        )
                        itemCart.add(dt)
                    }
//                    Log.d("data keranjang" ,itemCart.toString())
                    cartAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    Toast.makeText(thisParent, getString(R.string.error_message_data), Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    thisParent, getString(R.string.error_message_server), Toast.LENGTH_LONG
                ).show()
            }) {}

        val queue = Volley.newRequestQueue(thisParent)
        queue.add(stringRequest)
    }




}