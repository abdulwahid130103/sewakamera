package com.polinema.sewakamera.View.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Adapter.CategoryAdapter
import com.polinema.sewakamera.Adapter.CoverProdukAdapter
import com.polinema.sewakamera.Model.Category
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Produk
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.HomeActivity
import com.polinema.sewakamera.databinding.FragmentProfileBinding
import com.polinema.sewakamera.databinding.FragmentShopBinding
import org.json.JSONArray
import org.json.JSONException

class ShopFragment : Fragment() {
    private lateinit var b : FragmentShopBinding
    private lateinit var thisParent : HomeActivity
    private lateinit var v : View

    private lateinit var coverProduk:ArrayList<Produk>
    private lateinit var coverProdukAdapter: CoverProdukAdapter


    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var cateList:ArrayList<Category>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as HomeActivity
        b = FragmentShopBinding.inflate(layoutInflater)
        v = b.root

        coverProduk = arrayListOf()
        cateList = arrayListOf()

        getCoverProduk()
        getCategory()
        b.coverRecViewShopFrag.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        b.coverRecViewShopFrag.setHasFixedSize(true)
        coverProdukAdapter = CoverProdukAdapter(activity as Context, coverProduk )
        b.coverRecViewShopFrag.adapter = coverProdukAdapter

        b.categoriesRecView.layoutManager = GridLayoutManager(context,2,LinearLayoutManager.VERTICAL,false)
        b.categoriesRecView.setHasFixedSize(true)
        categoryAdapter = CategoryAdapter(activity as Context, cateList )
        b.categoriesRecView.adapter = categoryAdapter

        return v
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

    private fun getCategory() {
        val url = Connection()

        val stringRequest = object : StringRequest(
            Method.GET, url.url_get_category,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    cateList.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val category = Category(
                            id = jsonObject.getInt("id"),
                            name = jsonObject.getString("name"),
                            image_category = jsonObject.getString("gambar_url")
                        )
                        cateList.add(category)
                    }
                    categoryAdapter.notifyDataSetChanged()
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