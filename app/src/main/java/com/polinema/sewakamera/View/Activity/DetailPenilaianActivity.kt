package com.polinema.sewakamera.View.Activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Adapter.ProdukTerbaruAdapter
import com.polinema.sewakamera.Adapter.RatingDetailAdapter
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Produk
import com.polinema.sewakamera.Model.Rating
import com.polinema.sewakamera.R
import com.polinema.sewakamera.databinding.ActivityDetailPenilaianBinding
import org.json.JSONArray
import org.json.JSONException

class DetailPenilaianActivity : AppCompatActivity() {

    private lateinit var b : ActivityDetailPenilaianBinding
    var productIndex: Int = 0

    lateinit var newProductAdapter: RatingDetailAdapter
    lateinit var ratingData: ArrayList<Rating>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailPenilaianBinding.inflate(layoutInflater)
        setContentView(b.root)

        productIndex = intent.getIntExtra("id_produk",0)

        ratingData = arrayListOf()
        getDetailPenilaian()

        b.produkRecycleDetailPenilaian.layoutManager = LinearLayoutManager(this)
        newProductAdapter = RatingDetailAdapter(this, ratingData)
        b.produkRecycleDetailPenilaian.adapter = newProductAdapter

        b.backDetailPenilaianContainer.setOnClickListener{
            finish()
        }

    }

    private fun getDetailPenilaian() {
        val url = Connection()
        val url_fix = "${url.get_detail_rating}/${productIndex}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    ratingData.clear()

                    if(jsonArray.length() < 1){
                        b.produkRecycleDetailPenilaian.visibility = View.GONE
                        b.dataKosongDetailPenilaian.visibility = View.VISIBLE
                    }else{
                        b.produkRecycleDetailPenilaian.visibility = View.VISIBLE
                        b.dataKosongDetailPenilaian.visibility = View.GONE
                    }
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val produk = Rating(
                            id = jsonObject.getInt("id"),
                            nama_user = jsonObject.getString("nama_user"),
                            image = jsonObject.getString("gambar_url"),
                            rating = jsonObject.getString("rating").toFloat(),
                            deskripsi = jsonObject.getString("deskripsi")
                        )
                        ratingData.add(produk)
                    }
                    newProductAdapter.notifyDataSetChanged()
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