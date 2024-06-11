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
import com.polinema.sewakamera.Adapter.SelesaiAdapter
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Dibayar
import com.polinema.sewakamera.Model.Selesai
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.PesananActivity
import com.polinema.sewakamera.databinding.FragmentDibayarBinding
import com.polinema.sewakamera.databinding.FragmentSelesaiBinding
import org.json.JSONArray
import org.json.JSONException

class SelesaiFragment : Fragment() {

    private lateinit var b : FragmentSelesaiBinding
    private lateinit var thisParent : PesananActivity
    private lateinit var v : View
    private lateinit var selesaiData :ArrayList<Selesai>

    private lateinit var selesaiDataAdapter: SelesaiAdapter
    private lateinit var Session: SessionSewa
    private var data_cek= "kosong"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as PesananActivity
        b = FragmentSelesaiBinding.inflate(layoutInflater)
        v = b.root

        Session = SessionSewa(thisParent)
        selesaiData = arrayListOf()

        getBayarData()



        b.selesaiRecycleView.layoutManager = LinearLayoutManager(context)
        selesaiDataAdapter = SelesaiAdapter(activity as Context, selesaiData )
        b.selesaiRecycleView.adapter = selesaiDataAdapter
        return v
    }

    private fun cekData(){
        if (data_cek == "kosong"){
            b.dataKosongSelesai.visibility = View.VISIBLE
            b.selesaiRecycleView.visibility = View.GONE
        }else{
            b.dataKosongSelesai.visibility = View.GONE
            b.selesaiRecycleView.visibility = View.VISIBLE
        }
    }
    private fun getBayarData() {
        val url = Connection()
        var url_fix = "${url.get_selesai_transaksi}/${Session.getUserId()}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    selesaiData.clear()
                    if(jsonArray.length() > 0){
                        data_cek = "ada"
                    }
                    cekData()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val bok = Selesai(
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
                            isRating = jsonObject.getString("isRating").toBoolean()
                        )
                        selesaiData.add(bok)
                    }
                    selesaiDataAdapter.notifyDataSetChanged()
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