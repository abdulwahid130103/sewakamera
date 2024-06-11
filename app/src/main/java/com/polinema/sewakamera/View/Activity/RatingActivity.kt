package com.polinema.sewakamera.View.Activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Adapter.RatingAdapter
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.ProdukRating
import com.polinema.sewakamera.R
import com.polinema.sewakamera.databinding.ActivityRatingBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class RatingActivity : AppCompatActivity() {
    private lateinit var b : ActivityRatingBinding
    var id_transaksi = 0
    private lateinit var Session: SessionSewa
    private lateinit var ratingData :ArrayList<ProdukRating>
    var helper = DataHelper(this)

    private lateinit var ratingDataAdapter: RatingAdapter

    var id_user_asli = 0
    var id_transaksi_asli = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(b.root)

        Session = SessionSewa(this)
        ratingData = arrayListOf()
        id_transaksi = intent.getStringExtra("transaksi_id").toString().toInt()

        getProdukRating(Session.getUserId().toString().toInt(),id_transaksi)

        b.recycleRatingBar.layoutManager = LinearLayoutManager(this)
        ratingDataAdapter =  RatingAdapter(this, ratingData )
        b.recycleRatingBar.adapter = ratingDataAdapter

        b.btnKirimPenilaian.setOnClickListener{
            var allDataFilled = true
            for (i in 0 until ratingDataAdapter.itemCount) {
                val ratingAndDeskripsi = ratingDataAdapter.getRatingAndDescription(i)
                val rating = ratingAndDeskripsi.first
                val deskripsi = ratingAndDeskripsi.second
                if (rating == 0f || deskripsi.isEmpty()) {
                    allDataFilled = false
                    break
                }
            }

            if (allDataFilled) {
                val data = ratingDataAdapter.getData()


                val positiveAction = DialogInterface.OnClickListener { dialog, which ->
                    insertRating(
                        id_user_asli,
                        id_transaksi_asli,
                        data,
                    )
                }
                helper.showConfirmationDialog(
                    title = "Konfirmasi",
                    message = "Apakah anda sudah yakin melakukan transaksi?",
                    positiveText = "Ya",
                    negativeText = "Tidak",
                    positiveAction = positiveAction,
                    negativeAction = { dialog, which -> dialog.dismiss() }
                )
            } else {
                helper.toast_custom_alert(this, "notifikasi", "Harap isi semua rating dan deskripsi.")
            }
        }

        b.backHistoryContainer.setOnClickListener{
           finish()
        }
    }

    private fun insertRating(id_user_ins: Int, id_transaksi_ins:Int, data_ins: ArrayList<HashMap<String, String>>){

        val url = Connection().create_data_rating

        val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val success = jsonObject.getBoolean("success")
                val message = jsonObject.getString("message")

                if (success) {
                    helper.toast_custom_alert(this, "success", message)
                    val intent = Intent(this , PesananActivity::class.java)
                    startActivity(intent)
                } else {
                    helper.toast_custom_alert(this, "error", message)
                }
            } catch (e: JSONException) {
                Toast.makeText(this, "Error parsing JSON response", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this, "Tidak dapat terhubung ke server: ${error.message}", Toast.LENGTH_LONG).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    put("id_user", id_user_ins.toString())
                    put("id_transaksi", id_transaksi_ins.toString())
                    put("data",JSONArray(data_ins.map {
                        JSONObject().apply {
                            put("id_produk", it.getValue("id"))
                            put("rating", it.getValue("rating"))
                            put("deskripsi", it.getValue("deskripsi"))
                        }
                    }).toString())
                }
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    private fun getProdukRating(
        id : Int,
        id_transaksi : Int,
    ){
        val url = Connection()
        val url_fix = "${url.get_produk_rating}?id=${id}&transaction_id=${id_transaksi}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    ratingData.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        id_user_asli = jsonObject.getInt("id_user")
                        id_transaksi_asli = jsonObject.getInt("id")

                        val dataProduk = jsonObject.getJSONArray("product")
                        for (i in 0 until dataProduk.length()) {
                            val jsonObjectProduk = dataProduk.getJSONObject(i)
                            val bok = ProdukRating(
                                id = jsonObjectProduk.getInt("id_produk"),
                                nama_produk = jsonObjectProduk.getString("nama_produk"),
                                image = jsonObjectProduk.getString("image"),
                                harga = jsonObjectProduk.getInt("sub_total")
                            )
                            ratingData.add(bok)
                        }
                    }
                    ratingDataAdapter.notifyDataSetChanged()
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