package com.polinema.sewakamera.View.Activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Adapter.DetailHistoryAdapter
import com.polinema.sewakamera.Adapter.DisewaAdapter
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.DetailHistory
import com.polinema.sewakamera.Model.Disewa
import com.polinema.sewakamera.R
import com.polinema.sewakamera.databinding.ActivityDetailHistoryTransaksiBinding
import org.json.JSONArray
import org.json.JSONException

class DetailHistoryTransaksiActivity : AppCompatActivity() {

    private lateinit var b : ActivityDetailHistoryTransaksiBinding

    private lateinit var detailHistoryData :ArrayList<DetailHistory>

    private lateinit var detailHistoryDataAdapter: DetailHistoryAdapter
    private lateinit var Session: SessionSewa
    private lateinit var helper : DataHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailHistoryTransaksiBinding.inflate(layoutInflater)
        setContentView(b.root)

        val transaksi_id = intent.getStringExtra("transaksi_id").toString().toInt()

        Session = SessionSewa(this)
        detailHistoryData = arrayListOf()

        helper = DataHelper(this)

        getDetailhistory(transaksi_id)

        b.copyNumberDetailHistory.setOnClickListener{
            val clipboard : ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip : ClipData = ClipData.newPlainText("Edit Text",b.viaNumberHistory.text.toString())
            clipboard.setPrimaryClip(clip)
        }

        b.historyRecycleview.layoutManager = LinearLayoutManager(this)
        detailHistoryDataAdapter = DetailHistoryAdapter(this, detailHistoryData )
        b.historyRecycleview.adapter = detailHistoryDataAdapter

        b.backHistoryContainer.setOnClickListener{
            finish()
        }
    }

    private fun getDetailhistory(transaction_id : Int) {
        val url = Connection()
        var url_fix = "${url.get_detail_history}?id=${Session.getUserId()}&transaction_id=${transaction_id}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    detailHistoryData.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val status = jsonObject.getString("status").toString()
                        showHideText(status)
                        val id_transaksi = jsonObject.getInt("id").toString()
                        val id_user = jsonObject.getInt("id_user").toString()
                        val tgl_booking = jsonObject.getString("tgl_booking").toString()
                        val tgl_pinjam = jsonObject.getString("tgl_pinjam").toString()
                        val tgl_tenggat = jsonObject.getString("tgl_tenggat").toString()
                        val tgl_terverifikasi = jsonObject.getString("tgl_terverifikasi").toString()
                        val tgl_terima = jsonObject.getString("tgl_terima").toString()
                        val tgl_transaksi = jsonObject.getString("tgl_transaksi").toString()
                        val tgl_selesai = jsonObject.getString("tgl_selesai").toString()
                        val metode_pembayaran = jsonObject.getString("metode_pembayaran").toString()
                        val status_bayar = jsonObject.getString("status_bayar").toString()
                        val total_harga = jsonObject.getInt("total_harga").toString()
                        val tanggal_expire = jsonObject.getString("tanggal_expire").toString()
                        val transaction_id = jsonObject.getString("transaction_id").toString()
                        val va_number = jsonObject.getString("va_number").toString()

                        b.statusDetailHistory.setText(status)
                        b.tanggalbookinghistory.setText(tgl_booking)
                        b.tanggalditerimahistory.setText(tgl_terima)
                        b.tanggalpembayaranhistory.setText(tgl_transaksi)
                        b.tanggalPengembalianHistory.setText(tgl_selesai)
                        b.metodePembayaranDetailHistory.setText(metode_pembayaran)
                        b.totalHargaHistory.setText(helper.formatRupiah(total_harga.toInt()))
                        b.viaNumberHistory.setText(va_number)
                        b.nopesananhistoryhasil.setText(transaction_id)
                        b.tanggalExpiredDetailHistory.setText(tanggal_expire)
                        b.tanggalpinjambooking.setText(tgl_pinjam)
                        b.tanggaltenggatbooking.setText(tgl_tenggat)
                        b.tanggalterverifikasibooking.setText(tgl_terverifikasi)

                        val dataProduk = jsonObject.getJSONArray("product")
                        for (i in 0 until dataProduk.length()) {
                            val jsonObjectProduk = dataProduk.getJSONObject(i)
                            val bok = DetailHistory(
                                id = jsonObjectProduk.getInt("id"),
                                nama_produk = jsonObjectProduk.getString("nama_produk"),
                                image = jsonObjectProduk.getString("image"),
                                harga = jsonObjectProduk.getInt("sub_total"),
                                jumlah = jsonObjectProduk.getInt("jumlah")
                            )
                            detailHistoryData.add(bok)
                        }
                    }
                    detailHistoryDataAdapter.notifyDataSetChanged()
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

    private fun showHideText(status : String){

        if (status == "booking"){
            b.viaNumberHistoryContainer.visibility = View.GONE
            b.metodePembayaranHistoryContainer.visibility = View.GONE
            b.nopesananhistoryhasilContainer.visibility = View.GONE
            b.tanggalditerimahistoryContainer.visibility = View.GONE
            b.tanggalpinjambookingcontainer.visibility = View.GONE
            b.tanggaltenggatbookingcontainer.visibility = View.GONE
            b.tanggalterverifikasibookingcontainer.visibility = View.GONE
            b.tanggalpembayaranhistoryContainer.visibility = View.GONE
            b.tanggalPengembalianHistoryContainer.visibility = View.GONE
            b.tanggalExpiredDetailHistoryContainer.visibility = View.GONE
            b.btnCreateInvoice.visibility = View.GONE
        }else if (status == "terverifikasi"){
            b.metodePembayaranHistoryContainer.visibility = View.GONE
            b.nopesananhistoryhasilContainer.visibility = View.GONE
            b.tanggalditerimahistoryContainer.visibility = View.GONE
            b.tanggalpinjambookingcontainer.visibility = View.GONE
            b.tanggaltenggatbookingcontainer.visibility = View.GONE
            b.tanggalterverifikasibookingcontainer.visibility = View.GONE
            b.tanggalpembayaranhistoryContainer.visibility = View.GONE
            b.tanggalPengembalianHistoryContainer.visibility = View.GONE
            b.tanggalExpiredDetailHistoryContainer.visibility = View.GONE
            b.btnCreateInvoice.visibility = View.GONE
        }else if (status == "bayar"){
            b.metodePembayaranHistoryContainer.visibility = View.GONE
            b.nopesananhistoryhasilContainer.visibility = View.GONE
            b.tanggalditerimahistoryContainer.visibility = View.GONE
            b.tanggalpinjambookingcontainer.visibility = View.GONE
            b.tanggaltenggatbookingcontainer.visibility = View.GONE
            b.tanggalterverifikasibookingcontainer.visibility = View.GONE
            b.tanggalpembayaranhistoryContainer.visibility = View.GONE
            b.tanggalPengembalianHistoryContainer.visibility = View.GONE
            b.tanggalExpiredDetailHistoryContainer.visibility = View.GONE
            b.btnCreateInvoice.visibility = View.GONE
        }else if (status == "lunas"){
            b.viaNumberHistoryContainer.visibility = View.GONE
            b.metodePembayaranHistoryContainer.visibility = View.GONE
            b.nopesananhistoryhasilContainer.visibility = View.GONE
            b.tanggalditerimahistoryContainer.visibility = View.GONE
            b.tanggalpinjambookingcontainer.visibility = View.GONE
            b.tanggaltenggatbookingcontainer.visibility = View.GONE
            b.tanggalterverifikasibookingcontainer.visibility = View.GONE
            b.tanggalpembayaranhistoryContainer.visibility = View.GONE
            b.tanggalPengembalianHistoryContainer.visibility = View.GONE
            b.tanggalExpiredDetailHistoryContainer.visibility = View.GONE
            b.btnCreateInvoice.visibility = View.GONE
        }else if (status == "dipinjam"){
            b.viaNumberHistoryContainer.visibility = View.GONE
            b.metodePembayaranHistoryContainer.visibility = View.GONE
            b.nopesananhistoryhasilContainer.visibility = View.GONE
            b.tanggalditerimahistoryContainer.visibility = View.GONE
            b.tanggalpinjambookingcontainer.visibility = View.VISIBLE
            b.tanggaltenggatbookingcontainer.visibility = View.GONE
            b.tanggalterverifikasibookingcontainer.visibility = View.GONE
            b.tanggalpembayaranhistoryContainer.visibility = View.GONE
            b.tanggalPengembalianHistoryContainer.visibility = View.GONE
            b.tanggalExpiredDetailHistoryContainer.visibility = View.GONE
            b.btnCreateInvoice.visibility = View.GONE
        }else if (status == "kadaluarsa"){
            b.viaNumberHistoryContainer.visibility = View.GONE
            b.metodePembayaranHistoryContainer.visibility = View.GONE
            b.nopesananhistoryhasilContainer.visibility = View.GONE
            b.tanggalditerimahistoryContainer.visibility = View.GONE
            b.tanggalpinjambookingcontainer.visibility = View.GONE
            b.tanggaltenggatbookingcontainer.visibility = View.GONE
            b.tanggalterverifikasibookingcontainer.visibility = View.GONE
            b.tanggalpembayaranhistoryContainer.visibility = View.GONE
            b.tanggalPengembalianHistoryContainer.visibility = View.GONE
            b.tanggalExpiredDetailHistoryContainer.visibility = View.GONE
            b.btnCreateInvoice.visibility = View.GONE
        }else if (status == "selesai"){
            b.viaNumberHistoryContainer.visibility = View.GONE
            b.metodePembayaranHistoryContainer.visibility = View.GONE
            b.nopesananhistoryhasilContainer.visibility = View.GONE
            b.tanggalditerimahistoryContainer.visibility = View.GONE
            b.tanggalpinjambookingcontainer.visibility = View.VISIBLE
            b.tanggaltenggatbookingcontainer.visibility = View.GONE
            b.tanggalterverifikasibookingcontainer.visibility = View.GONE
            b.tanggalpembayaranhistoryContainer.visibility = View.GONE
            b.tanggalPengembalianHistoryContainer.visibility = View.VISIBLE
            b.tanggalExpiredDetailHistoryContainer.visibility = View.GONE
            b.btnCreateInvoice.visibility = View.GONE
        }else if(status == "tolak"){
            b.viaNumberHistoryContainer.visibility = View.GONE
            b.metodePembayaranHistoryContainer.visibility = View.GONE
            b.nopesananhistoryhasilContainer.visibility = View.GONE
            b.tanggalditerimahistoryContainer.visibility = View.GONE
            b.tanggalpinjambookingcontainer.visibility = View.GONE
            b.tanggaltenggatbookingcontainer.visibility = View.GONE
            b.tanggalterverifikasibookingcontainer.visibility = View.GONE
            b.tanggalpembayaranhistoryContainer.visibility = View.GONE
            b.tanggalPengembalianHistoryContainer.visibility = View.GONE
            b.tanggalExpiredDetailHistoryContainer.visibility = View.GONE
            b.btnCreateInvoice.visibility = View.GONE
        }
    }
}