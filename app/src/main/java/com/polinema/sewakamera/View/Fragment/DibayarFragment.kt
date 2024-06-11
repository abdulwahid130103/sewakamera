package com.polinema.sewakamera.View.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Adapter.BookingAdapter
import com.polinema.sewakamera.Adapter.DibayarAdapter
import com.polinema.sewakamera.Adapter.LunasAdapter
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Booking
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Dibayar
import com.polinema.sewakamera.Model.Lunas
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.PesananActivity
import com.polinema.sewakamera.databinding.FragmentDibayarBinding
import com.polinema.sewakamera.databinding.FragmentLunasBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DibayarFragment : Fragment() {

    private lateinit var b : FragmentDibayarBinding
    private lateinit var thisParent : PesananActivity
    private lateinit var v : View

    private lateinit var dibayarData :ArrayList<Dibayar>

    private lateinit var dibayarDataAdapter: DibayarAdapter
    private lateinit var Session: SessionSewa
    private var data_cek= "kosong"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as PesananActivity
        b = FragmentDibayarBinding.inflate(layoutInflater)
        v = b.root


        Session = SessionSewa(thisParent)
        dibayarData = arrayListOf()

        getDataBayarCeking()
        getDibayarData()



        b.dibayarRecycleView.layoutManager = LinearLayoutManager(context)
        dibayarDataAdapter = DibayarAdapter(activity as Context, dibayarData )
        b.dibayarRecycleView.adapter = dibayarDataAdapter


        return v
    }

    private fun cekData(){
        if (data_cek == "kosong"){
            b.dataKosongBayar.visibility = View.VISIBLE
            b.dibayarRecycleView.visibility = View.GONE
        }else{
            b.dataKosongBayar.visibility = View.GONE
            b.dibayarRecycleView.visibility = View.VISIBLE
        }
    }
    private fun getDibayarData() {
        val url = Connection()
        var url_fix = "${url.get_dibayar_transaksi}/${Session.getUserId()}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    dibayarData.clear()
                    if(jsonArray.length() > 0){
                        data_cek = "ada"
                    }
                    cekData()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val bok = Dibayar(
                            id = jsonObject.getInt("id"),
                            nama_mitra = jsonObject.getString("nama_mitra"),
                            status = jsonObject.getString("status"),
                            image = jsonObject.getString("image"),
                            nama_produk = jsonObject.getString("nama_produk"),
                            harga = jsonObject.getInt("harga"),
                            jumlah = jsonObject.getInt("jumlah"),
                            sub_harga = jsonObject.getInt("sub_harga"),
                            jumlah_all_produk = jsonObject.getInt("jumlah_all_produk"),
                            total_all_produk = jsonObject.getInt("total_all_produk"),
                        )
                        dibayarData.add(bok)
                    }
                    dibayarDataAdapter.notifyDataSetChanged()
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


    private fun updateExpiredData(transaksi_id: Int,status: String,status_bayar : String){
        val url = Connection()
        val url_fix = "${url.get_update_status_expired}?id_transaksi=${transaksi_id}&status=$status&status_bayar=$status_bayar"
        val request = object : StringRequest(
            Method.POST,url_fix, Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val success = jsonObject.getBoolean("success")
                val message = jsonObject.getString("message")
                Toast.makeText(thisParent,message.toString(),Toast.LENGTH_LONG).show()

            },
            Response.ErrorListener { error ->
                Toast.makeText(thisParent,"Tidak dapat terhubung ke server",
                    Toast.LENGTH_LONG).show()
            }){}
        val queue = Volley.newRequestQueue(thisParent)
        queue.add(request)
    }

    private fun getExpiredData(transaksi_id: Int,orderId: String) {
        val url = "https://api.sandbox.midtrans.com/v2/$orderId/status/b2b"
        val requestQueue = Volley.newRequestQueue(thisParent)

        val stringRequest = object : StringRequest(Method.GET, url,
            Response.Listener<String> { response ->
                val data = JSONObject(response)
                val transactions = data.getJSONArray("transactions")
                for (i in 0 until transactions.length()) {
                    val jsonObject = transactions.getJSONObject(i)
                    val statusData = jsonObject.getString("transaction_status")
                    if(statusData == "expire"){
                        updateExpiredData(transaksi_id,"kadaluarsa",statusData.toString())
                    }else if(statusData == "settlement"){
                        updateExpiredData(transaksi_id,"lunas",statusData.toString())
                    }
                }

            },
            Response.ErrorListener { error ->
                Log.e("PaymentStatus", "Error: ${error.message}")
                Toast.makeText(thisParent, "Failed to get payment status", Toast.LENGTH_LONG).show()
            }) {
            override fun getHeaders(): Map<String, String> = hashMapOf(
                "Accept" to "application/json",
                "Authorization" to "Basic U0ItTWlkLXNlcnZlci1kNlk4R0RLc1NranFwXzBXMGtJdWpZRFE6"
            )
        }

        requestQueue.add(stringRequest)
    }

    private fun getDataBayarCeking() {
        val url = Connection()
        var url_fix = "${url.get_cek_expired}/${Session.getUserId()}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        getExpiredData(jsonObject.getInt("id"),jsonObject.getString("order_id"))
                    }
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