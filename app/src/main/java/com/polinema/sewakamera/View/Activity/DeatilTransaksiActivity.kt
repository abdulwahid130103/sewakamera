package com.polinema.sewakamera.View.Activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Adapter.DetailTransaksiAdapter
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.ProductData
import com.polinema.sewakamera.R
import com.polinema.sewakamera.databinding.ActivityDeatilTransaksiBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Locale

class DeatilTransaksiActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var b : ActivityDeatilTransaksiBinding
    var data = DataHelper(this)
    lateinit var products : ArrayList<HashMap<String, Any>>
    var subTotalHarga = 0
    var totalHarga = 0
    var idMitra = 0
    private lateinit var Session: SessionSewa
    lateinit var dialog : AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDeatilTransaksiBinding.inflate(layoutInflater)
        setContentView(b.root)

        Session = SessionSewa(this)
        products = (intent.getSerializableExtra("data") as? ArrayList<HashMap<String, Any>>)!!

        b.backDetailTransaksiContainer.setOnClickListener(this)
        b.jamSewaBooking.setOnClickListener(this)
        b.tanggalSewaBooking.setOnClickListener(this)
        b.btnAjukanPermintaan.setOnClickListener(this)

        dialog = AlertDialog.Builder(this)

        data.setDefaultTime(b.jamSewaBooking)
        data.setDefaultDate(b.tanggalSewaBooking)
        data.setDefaultHari(b.jumlahHariBooking)

        data.pantauInputan(b.jumlahHariBooking, Runnable { hitungSubTotal() })
        data.pantauInputan(b.tanggalSewaBooking, Runnable { hitungSubTotal() })
        data.pantauInputan(b.jamSewaBooking, Runnable { hitungSubTotal() })


        val adapter = DetailTransaksiAdapter(this,products ?: ArrayList())
        b.checkoutRecycle.layoutManager = LinearLayoutManager(this)
        b.checkoutRecycle.adapter = adapter
    }

    override fun onStart() {
        hitungSubTotal()
        super.onStart()
    }



    fun insertTransaksiBooking(
        idUser: String,
        status: String,
        tglpinjam: String,
        tglBooking: String,
        tglTenggat: String,
        idMitra: String,
        totalHarga: String,
        datas: List<ProductData>) {

        Log.d("data insert booking",JSONArray(datas.map {
            JSONObject().apply {
                put("id_produk", it.idProduk)
                put("jumlah", it.jumlah)
                put("sub_total", it.subTotal)
            }
        }).toString())
        val url = Connection().url_create_transaksi_booking

        val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val success = jsonObject.getBoolean("success")
                val message = jsonObject.getString("message")

                if (success) {
                    data.toast_custom_alert(this, "success", message)
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                Toast.makeText(this, "Error parsing JSON response", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this, "Tidak dapat terhubung ke server: ${error.message}", Toast.LENGTH_LONG).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    put("id_user", idUser)
                    put("status", status)
                    put("tgl_pinjam", tglpinjam)
                    put("tgl_booking", tglBooking)
                    put("tgl_tenggat", tglTenggat)
                    put("id_mitra", idMitra)
                    put("total_harga", totalHarga)
                    put("data", JSONArray(datas.map {
                        JSONObject().apply {
                            put("id_produk", it.idProduk)
                            put("jumlah", it.jumlah)
                            put("sub_total", it.subTotal)
                        }
                    }).toString())
                }
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }


    fun hitungSubTotal(){
        subTotalHarga = 0
        for (pd in products){
            subTotalHarga += pd["harga"].toString().toInt() * pd["qty"].toString().toInt()
        }
        b.subTotalHargaBookingHasil.setText(data.formatRupiah(subTotalHarga))
        totalHarga = (subTotalHarga * b.jumlahHariBooking.text.toString().toInt())
        b.totalHargaBookingHasil.setText(data.formatRupiah((subTotalHarga * b.jumlahHariBooking.text.toString().toInt())))

        val tgl_sewa = b.tanggalSewaBooking.text.toString()
        val jumlah_hari = b.jumlahHariBooking.text.toString()
        val endDate = data.calculateEndDate(tgl_sewa,jumlah_hari.toString().toInt())

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val formattedMonth = String.format("%02d", month)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        b.tanggalRangeSewaBookingHasil.text = "${tgl_sewa} / ${endDate}"
        b.tanggalMulaiBookingHasil.text = "${year}-${formattedMonth}-${day}"

    }


    private fun performBooking(){
        val productsData = getDataProduk()
        val currentDateTime = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        try {
            val datePart = LocalDate.parse(b.tanggalSewaBooking.text.toString(), dateFormatter)
            val formattedTime = b.jamSewaBooking.text.toString().replace(".", ":") + ":00"
            val timePart = LocalTime.parse(formattedTime, timeFormatter)
            val combinedDateTime = LocalDateTime.of(datePart, timePart)

            val currentDateTime = LocalDateTime.now()
            val formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            val jumlahHari = b.jumlahHariBooking.text.toString().toIntOrNull() ?: 0
            val tanggalTenggat = currentDateTime.plusDays(jumlahHari.toLong() + 1).withHour(7).withMinute(0).withSecond(0)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedTenggat = tanggalTenggat.format(formatter)
//            Toast.makeText(this, "masuk", Toast.LENGTH_SHORT).show()
//
//
//
            this.insertTransaksiBooking(
                idUser = Session.getUserId().toString(),
                status = "booking",
                tglpinjam = combinedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                tglBooking = formattedCurrentDateTime,
                tglTenggat = formattedTenggat,
                idMitra = idMitra.toString(),  // Ensure idMitra is set correctly elsewhere in your code
                totalHarga = totalHarga.toString(),
                datas = productsData
            )
        } catch (e: DateTimeParseException) {
            Toast.makeText(this, "Error in parsing date or time", Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.backDetailTransaksiContainer -> {
                finish()
            }
            R.id.jamSewaBooking -> {
                data.showTimePicker(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val formattedTime = String.format("%02d.%02d", hourOfDay, minute)
                    b.jamSewaBooking.setText(formattedTime)
                })
            }
            R.id.tanggalSewaBooking->{
                data.showDatePicker(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)

                    // Menggunakan SimpleDateFormat untuk format tanggal
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedCalendar.time)

                    b.tanggalSewaBooking.setText(formattedDate)
                })
            }
            R.id.btn_ajukan_permintaan-> {

                val positiveAction = DialogInterface.OnClickListener { dialog, which ->
                    performBooking()
                }
                data.showConfirmationDialog(
                    title = "Konfirmasi",
                    message = "Apakah anda sudah yakin melakukan transaksi?",
                    positiveText = "Ya",
                    negativeText = "Tidak",
                    positiveAction = positiveAction,
                    negativeAction = { dialog, which -> dialog.dismiss() }
                )
            }
        }
    }

    private fun getDataProduk(): List<ProductData> {
        val productList = intent.getSerializableExtra("data") as? ArrayList<HashMap<String, Any>> ?: ArrayList()

        for(pd in productList){
            idMitra = pd["id_mitra"].toString().toInt()
        }
        return productList.map { product ->
            ProductData(
                idProduk = product["id_product"].toString(),
                jumlah = product["qty"].toString().toInt(),
                subTotal = (product["harga"].toString().toInt()) * (product["qty"].toString().toInt())
            )
        }
    }

}