package com.polinema.sewakamera.View.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.polinema.sewakamera.Adapter.CoverProdukAdapter
import com.polinema.sewakamera.Adapter.ProdukTerbaruAdapter
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Produk
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.HomeActivity
import com.polinema.sewakamera.databinding.FragmentHomeBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class HomeFragment : Fragment() {

    private lateinit var b : FragmentHomeBinding
    private lateinit var thisParent : HomeActivity
    private lateinit var v : View

    private lateinit var coverProduk:ArrayList<Produk>
    lateinit var terbaruProduk:ArrayList<Produk>

    lateinit var coverProdukAdapter: CoverProdukAdapter
    lateinit var terbaruProdukAdapter: ProdukTerbaruAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as HomeActivity
        b =FragmentHomeBinding.inflate(layoutInflater)
        v = b.root

        coverProduk = arrayListOf()
        terbaruProduk = arrayListOf()
        hideLayout()

        getCoverProduk()
        getProdukTerbaru()

        b.coverRecView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        b.coverRecView.setHasFixedSize(true)
        coverProdukAdapter = CoverProdukAdapter(activity as Context, coverProduk )
        b.coverRecView.adapter = coverProdukAdapter

        b.newRecView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        b.newRecView.setHasFixedSize(true)
        terbaruProdukAdapter = ProdukTerbaruAdapter(terbaruProduk, activity as Context )
        b.newRecView.adapter = terbaruProdukAdapter

        showLayout()



        return v
    }

    private fun hideLayout(){
        b.animationView.playAnimation()
        b.animationView.loop(true)
        b.coverRecView.visibility = View.GONE
        b.newLayout.visibility = View.GONE
        b.saleLayout.visibility = View.GONE
        b.animationView.visibility = View.VISIBLE
    }
    private fun showLayout(){
        b.animationView.pauseAnimation()
        b.animationView.visibility = View.GONE
        b.coverRecView.visibility = View.VISIBLE
        b.newLayout.visibility = View.VISIBLE
        b.saleLayout.visibility = View.VISIBLE
    }

    private fun getCoverProduk() {
        val url = Connection()

        val stringRequest = object : StringRequest(
            Method.GET, url.url_cover_produk,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    coverProduk.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val produk = Produk(
                            id = jsonObject.getInt("id"),
                            id_mitra = jsonObject.getInt("id_mitra"),
                            id_category = jsonObject.getString("id_category"),
                            nama_produk = jsonObject.getString("nama_produk"),
                            image = jsonObject.getString("gambar_url"),
                            type = jsonObject.getString("type"),
                            harga = jsonObject.getInt("harga"),
                            stok = jsonObject.getInt("stok"),
                            deskripsi = jsonObject.getString("deskripsi")
                        )
                        coverProduk.add(produk)
                    }
                    coverProdukAdapter.notifyDataSetChanged()
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

    private fun getProdukTerbaru() {
        val url = Connection()

        val stringRequest = object : StringRequest(
            Method.GET, url.url_cover_produk,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    terbaruProduk.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val produk = Produk(
                            id = jsonObject.getInt("id"),
                            id_mitra = jsonObject.getInt("id_mitra"),
                            id_category = jsonObject.getString("id_category"),
                            nama_produk = jsonObject.getString("nama_produk"),
                            image = jsonObject.getString("gambar_url"),
                            type = jsonObject.getString("type"),
                            harga = jsonObject.getInt("harga"),
                            stok = jsonObject.getInt("stok"),
                            deskripsi = jsonObject.getString("deskripsi")
                        )
                        terbaruProduk.add(produk)
                    }
                    terbaruProdukAdapter.notifyDataSetChanged()
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