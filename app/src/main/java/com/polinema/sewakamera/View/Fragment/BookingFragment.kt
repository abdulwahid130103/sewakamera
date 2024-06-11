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
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Booking
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.PesananActivity
import com.polinema.sewakamera.databinding.FragmentBookingBinding
import org.json.JSONArray
import org.json.JSONException

class BookingFragment : Fragment() {

    private lateinit var b : FragmentBookingBinding
    private lateinit var thisParent : PesananActivity
    private lateinit var v : View

    private lateinit var bookingData :ArrayList<Booking>

    private lateinit var bookingDataAdapter: BookingAdapter
    private lateinit var Session: SessionSewa
    private var data_cek= "kosong"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as PesananActivity
        b =FragmentBookingBinding.inflate(layoutInflater)
        v = b.root

        Session = SessionSewa(thisParent)
        bookingData = arrayListOf()

        getBookingData()



        b.bookingRecycleView.layoutManager = LinearLayoutManager(context)
        bookingDataAdapter = BookingAdapter(activity as Context, bookingData )
        b.bookingRecycleView.adapter = bookingDataAdapter


        return v
    }

    private fun cekData(){
        if (data_cek == "kosong"){
            b.dataKosongBooking.visibility = View.VISIBLE
            b.bookingRecycleView.visibility = View.GONE
        }else{
            b.dataKosongBooking.visibility = View.GONE
            b.bookingRecycleView.visibility = View.VISIBLE
        }
    }
    private fun getBookingData() {
        val url = Connection()
        var url_fix = "${url.get_booking_transaksi}/${Session.getUserId()}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    bookingData.clear()
                    if(jsonArray.length() > 0){
                        data_cek = "ada"
                    }
                    cekData()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val bok = Booking(
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
                        bookingData.add(bok)
                    }
                    bookingDataAdapter.notifyDataSetChanged()
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