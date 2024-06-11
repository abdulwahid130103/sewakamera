package com.polinema.sewakamera.View.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Adapter.DetailMitraAdapter
import com.polinema.sewakamera.Adapter.ProdukListAdapter
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Produk
import com.polinema.sewakamera.R
import com.polinema.sewakamera.databinding.ActivityDetailMitraBinding
import com.polinema.sewakamera.databinding.ActivityProdukListBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DetailMitraActivity : AppCompatActivity() {

    lateinit var b : ActivityDetailMitraBinding
    private lateinit var detailMitraData:ArrayList<Produk>
    private lateinit var detailMitraAdapter: DetailMitraAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailMitraBinding.inflate(layoutInflater)
        setContentView(b.root)

        detailMitraData = arrayListOf()
        val dataMitra = intent.getStringExtra("data_mitra").toString()
        getProdukList(dataMitra.toInt())


        val numberOfColumns = 2
        val spacingInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
        b.produkRecycleDetailMitra.addItemDecoration(GridSpacingItemDecoration(numberOfColumns, spacingInPixels, true))

        b.produkRecycleDetailMitra.layoutManager = GridLayoutManager(this, numberOfColumns)
        detailMitraAdapter = DetailMitraAdapter(this, detailMitraData )
        b.produkRecycleDetailMitra.adapter = detailMitraAdapter

        b.backDetailMitraContainer.setOnClickListener{
            finish()
        }

    }

    private fun getProdukList(id_mitra:Int) {
        val url = Connection()
        var url_fix = "${url.get_detail_mitra}/${id_mitra}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonObj = JSONObject(response)
                    val jsonArray = jsonObj.getJSONArray("produk")
                    val dataNamaMitra = jsonObj.getString("nama_mitra")
                    val dataTotalProduk = jsonObj.getString("total_products")
                    b.NamaMitraDetailMitra.setText(dataNamaMitra.toString())
                    b.totalProdukDetailMitra.setText("${dataTotalProduk} Produk")
                    detailMitraData.clear()

                    if (jsonArray.length() <= 0) {
                        b.produkRecycleDetailMitra.visibility = View.GONE
                        b.dataKosongDetailMitra.visibility = View.VISIBLE
                    } else {
                        b.produkRecycleDetailMitra.visibility = View.VISIBLE
                        b.dataKosongDetailMitra.visibility = View.GONE
                    }

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val produk = Produk(
                            id = jsonObject.getInt("id"),
                            id_mitra = jsonObject.getInt("id_mitra"),
                            id_category = jsonObject.getString("id_category"),
                            nama_produk = jsonObject.getString("nama_produk"),
                            image = jsonObject.getString("gambar_url"),  // Changed from 'image' to 'gambar_url'
                            type = jsonObject.getString("type"),
                            harga = jsonObject.getInt("harga"),
                            stok = jsonObject.getInt("stok"),
                            deskripsi = jsonObject.getString("deskripsi"),
                            rating = jsonObject.getString("rating").toFloat(),
                        )
                        detailMitraData.add(produk)
                    }
                    detailMitraAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    Toast.makeText(this, getString(R.string.error_message_data), Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    this, getString(R.string.error_message_server), Toast.LENGTH_LONG
                ).show()
            }) {}

        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }
}