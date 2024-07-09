package com.polinema.sewakamera.View.Fragment

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
import com.polinema.sewakamera.Adapter.DisewaAdapter
import com.polinema.sewakamera.Adapter.TolakAdapter
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Disewa
import com.polinema.sewakamera.Model.Tolak
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.PesananActivity
import com.polinema.sewakamera.databinding.FragmentDipinjamBinding
import com.polinema.sewakamera.databinding.FragmentTolakBinding
import org.json.JSONArray
import org.json.JSONException

class TolakFragment : Fragment() {


    private lateinit var b : FragmentTolakBinding
    private lateinit var thisParent : PesananActivity
    private lateinit var v : View
    private lateinit var tolakData :ArrayList<Tolak>

    private lateinit var tolakDataAdapter: TolakAdapter
    private lateinit var Session: SessionSewa
    private var data_cek= "kosong"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as PesananActivity
        b = FragmentTolakBinding.inflate(layoutInflater)
        v = b.root

        Session = SessionSewa(thisParent)
        tolakData = arrayListOf()

        getBookingData()



        b.tolakRecycleView.layoutManager = LinearLayoutManager(context)
        tolakDataAdapter = TolakAdapter(activity as Context, tolakData )
        b.tolakRecycleView.adapter = tolakDataAdapter
        return v
    }

    private fun cekData(){
        if (data_cek == "kosong"){
            b.dataKosongtolak.visibility = View.VISIBLE
            b.tolakRecycleView.visibility = View.GONE
        }else{
            b.dataKosongtolak.visibility = View.GONE
            b.tolakRecycleView.visibility = View.VISIBLE
        }
    }
    private fun getBookingData() {
        val url = Connection()
        var url_fix = "${url.get_tolak_transaksi}/${Session.getUserId()}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    tolakData.clear()
                    if(jsonArray.length() > 0){
                        data_cek = "ada"
                    }
                    cekData()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val bok = Tolak(
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
                        tolakData.add(bok)
                    }
                    tolakDataAdapter.notifyDataSetChanged()
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