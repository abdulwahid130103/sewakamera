package com.polinema.sewakamera.View.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.midtrans.sdk.uikit.api.model.CreditCard
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.CustomerDetails
import com.midtrans.sdk.uikit.api.model.Expiry
import com.midtrans.sdk.uikit.api.model.ItemDetails
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.polinema.sewakamera.Adapter.TerverifikasiAdapter
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Booking
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Terverifikasi
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.AfterPaymentActivity
import com.polinema.sewakamera.View.Activity.PesananActivity
import com.polinema.sewakamera.databinding.FragmentTerverifikasiBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale
import java.util.UUID

class TerverifikasiFragment : Fragment() {


    private lateinit var b : FragmentTerverifikasiBinding
    private lateinit var thisParent : PesananActivity
    private lateinit var v : View

    private lateinit var verifData :ArrayList<Terverifikasi>
    private lateinit var Session: SessionSewa

    private lateinit var verifDataAdapter: TerverifikasiAdapter

    var idTransaksiLastPayment = 0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as PesananActivity
        b = FragmentTerverifikasiBinding.inflate(layoutInflater)
        v = b.root
        Session = SessionSewa(thisParent)
        verifData = arrayListOf()

        getVerifData()

        context?.let {
            UiKitApi.Builder()
                .withMerchantClientKey("SB-Mid-client-GOMEsW6TYo8zRTJJ")
                .withContext(it)
                .withMerchantUrl("http://192.168.0.132/sewakameranew/sewakameranew/public/api/")
                .enableLog(true)
                .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
                .build()
        }
        setLocaleNew("id")


        b.verifRecycleView.layoutManager = LinearLayoutManager(context)
        verifDataAdapter = TerverifikasiAdapter(activity as Context, verifData).apply {
            payClickListener = object : TerverifikasiAdapter.OnItemPayClickListener {
                override fun onPayClick(user_id: Int, id_transaksi: Int,total_all_produk: Int) {
                    getDetailBayar(user_id,id_transaksi,total_all_produk)
                }
            }
        }

        b.verifRecycleView.adapter = verifDataAdapter


        return v
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result?.resultCode == AppCompatActivity.RESULT_OK) {
            result.data?.let {
                val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                checkPaymentStatus(transactionResult?.transactionId.toString())
            }
        }else {
            Log.d("PaymentResult", "Failed or Cancelled")
        }
    }
    private fun getVerifData() {
        val url = Connection()
        val url_fix = "${url.get_terverifikasi_transaksi}/${Session.getUserId()}"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    verifData.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

                        val bok = Terverifikasi(
                            id = jsonObject.getInt("id"),
                            id_user = jsonObject.getInt("id_user"),
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
                        verifData.add(bok)
                    }
                    verifDataAdapter.notifyDataSetChanged()
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

    private fun checkPaymentStatus(orderId: String) {
        val url = "https://api.sandbox.midtrans.com/v2/$orderId/status"
        val requestQueue = Volley.newRequestQueue(thisParent)

        val stringRequest = object : StringRequest(Method.GET, url,
            Response.Listener<String> { response ->
                val data = JSONObject(response)
                Log.d("PaymentStatus", "Response: ${response.toString()}")
                handlePaymentResponse(
                    data.getString("transaction_id"),
                    data.getString("transaction_status"),
                    data.getString("transaction_time"),
                    data.getString("payment_type"),
                    data.getString("expiry_time"),
                )
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

    private fun handlePaymentResponse(
        transaction_id : String,
        transaction_status : String,
        transaction_time : String,
        payment_type : String,
        expiry_time : String,
    ) {
        var st_bayar = "bayar"
        val url = Connection()

        if(transaction_status == "settlement"){
            st_bayar = "lunas"
        }
        val url_fix = "${url.get_update_transaksi_status}?id_transaksi=${idTransaksiLastPayment}&transaction_id=$transaction_id&transaction_status=$transaction_status&transaction_time=$transaction_time&payment_type=$payment_type&expiry_time=$expiry_time&status=$st_bayar"
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

        if(st_bayar == "lunas"){
            val intent = Intent(thisParent, AfterPaymentActivity::class.java)
            startActivity(intent)
        }

    }


    fun getDetailBayar(id_user: Int, id_transaksi: Int,total_all_produk: Int) {

        val url = Connection()
        val finalUrl = "${url.get_detail_transaksi_bayar}?id_user=$id_user&id_transaksi=$id_transaksi"
        val itemDetails = ArrayList<ItemDetails>()

        idTransaksiLastPayment = id_transaksi

        val stringRequest = object : StringRequest(
            Method.GET, finalUrl,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    Log.d("dataPusat1",jsonArray.toString())
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val products = jsonObject.getJSONArray("product")
                        for (j in 0 until products.length()) {
                            val productObject = products.getJSONObject(j)
                            val detail = ItemDetails(productObject.getInt("id").toString(), productObject.getInt("harga").toDouble(), productObject.getInt("jumlah").toString().toInt(), productObject.getString("nama_produk"))
                            itemDetails.add(detail)
                        }
                        UiKitApi.getDefaultInstance().startPaymentUiFlow(
                            thisParent,
                            launcher,
                            SnapTransactionDetail(UUID.randomUUID().toString(),
                                total_all_produk.toDouble(), "IDR"),
                            CustomerDetails(
                                jsonObject.getInt("id").toString(),
                                jsonObject.getString("name"),
                                "",
                                jsonObject.getString("email"),
                                jsonObject.getString("phone_number"),
                                null,
                                null
                            ),
                            itemDetails,
                            CreditCard(false, null, null, null, null, null, null, null, null, null),
                            "customerIdentifier"+System.currentTimeMillis().toString(),
                            null, null, null,
                            Expiry(
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault()).format(
                                    Date()
                                ), Expiry.UNIT_DAY, 1),
                        )
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

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }



}