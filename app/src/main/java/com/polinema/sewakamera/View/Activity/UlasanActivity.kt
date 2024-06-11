package com.polinema.sewakamera.View.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Adapter.RatingDetailAdapter
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Rating
import com.polinema.sewakamera.R
import com.polinema.sewakamera.databinding.ActivityUlasanBinding
import org.json.JSONArray
import org.json.JSONException

class UlasanActivity : AppCompatActivity() {

    private lateinit var b : ActivityUlasanBinding
    lateinit var ulasanDataAdapter: RatingDetailAdapter
    lateinit var ulasanData: ArrayList<Rating>
    lateinit var session : SessionSewa
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityUlasanBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionSewa(this)
        ulasanData = arrayListOf()
        getDetailPenilaian()


        b.produkRecycleDetailUlasan.layoutManager = LinearLayoutManager(this)
        ulasanDataAdapter = RatingDetailAdapter(this, ulasanData)
        b.produkRecycleDetailUlasan.adapter = ulasanDataAdapter

        b.backDetailUlasanContainer.setOnClickListener{
            finish()
        }

    }

    private fun getDetailPenilaian() {
        val url = Connection()
        val url_fix = "${url.get_detail_ulasan}/${session.getUserId()}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    ulasanData.clear()

                    if(jsonArray.length() < 1){
                        b.produkRecycleDetailUlasan.visibility = View.GONE
                        b.dataKosongDetailUlasan.visibility = View.VISIBLE
                    }else{
                        b.produkRecycleDetailUlasan.visibility = View.VISIBLE
                        b.dataKosongDetailUlasan.visibility = View.GONE
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
                        ulasanData.add(produk)
                    }
                    ulasanDataAdapter.notifyDataSetChanged()
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