package com.polinema.sewakamera.View.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Adapter.DibayarAdapter
import com.polinema.sewakamera.Adapter.ExpiredAdapter
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Dibayar
import com.polinema.sewakamera.Model.Expired
import com.polinema.sewakamera.Model.Lunas
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.PesananActivity
import com.polinema.sewakamera.databinding.FragmentDibayarBinding
import com.polinema.sewakamera.databinding.FragmentExpiredBinding
import org.json.JSONArray
import org.json.JSONException


class ExpiredFragment : Fragment() {

    private lateinit var b : FragmentExpiredBinding
    private lateinit var thisParent : PesananActivity
    private lateinit var v : View

    private lateinit var expiredData :ArrayList<Expired>

    private lateinit var expiredDataAdapter: ExpiredAdapter
    private lateinit var Session: SessionSewa
    private var data_cek= "kosong"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as PesananActivity
        b = FragmentExpiredBinding.inflate(layoutInflater)
        v = b.root


        Session = SessionSewa(thisParent)
        expiredData = arrayListOf()

        getExpiredData()



        b.expiredRecycleView.layoutManager = LinearLayoutManager(context)
        expiredDataAdapter = ExpiredAdapter(activity as Context, expiredData )
        b.expiredRecycleView.adapter = expiredDataAdapter

        return v
    }


    private fun cekData(){
        if (data_cek == "kosong"){
            b.dataKosongExpired.visibility = View.VISIBLE
            b.expiredRecycleView.visibility = View.GONE
        }else{
            b.dataKosongExpired.visibility = View.GONE
            b.expiredRecycleView.visibility = View.VISIBLE
        }
    }
    private fun getExpiredData() {
        val url = Connection()
        var url_fix = "${url.get_expired_transaksi}/${Session.getUserId()}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    expiredData.clear()
                    if(jsonArray.length() > 0){
                        data_cek = "ada"
                    }
                    cekData()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val bok = Expired(
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
                        expiredData.add(bok)
                    }
                    expiredDataAdapter.notifyDataSetChanged()
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