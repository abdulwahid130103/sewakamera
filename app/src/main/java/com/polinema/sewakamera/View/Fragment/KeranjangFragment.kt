package com.polinema.sewakamera.View.Fragment

import KeranjangAdapter
import android.content.Context
import android.os.Bundle
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
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Keranjang
import com.polinema.sewakamera.Model.SubKeranjang
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.HomeActivity
import com.polinema.sewakamera.databinding.FragmentKeranjangBinding
import org.json.JSONArray
import org.json.JSONException

class KeranjangFragment : Fragment() {

    private lateinit var b : FragmentKeranjangBinding
    private lateinit var thisParent : HomeActivity
    private lateinit var v : View


    lateinit var cartAdapter: KeranjangAdapter
    lateinit var animationView: LottieAnimationView
    lateinit var totalPriceBagFrag: TextView
    lateinit var itemCart: ArrayList<Keranjang>
    var sum:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as HomeActivity
        b = FragmentKeranjangBinding.inflate(layoutInflater)
        v = b.root

        itemCart = arrayListOf()

        getCoverProduk()


        b.cartRecView.layoutManager = LinearLayoutManager(context)
        cartAdapter = KeranjangAdapter(activity as Context, itemCart)
        b.cartRecView.adapter = cartAdapter
        return v
    }

    private fun getCoverProduk() {
        val url = Connection()

        val stringRequest = object : StringRequest(
            Method.GET, url.url_get_keranjang,
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