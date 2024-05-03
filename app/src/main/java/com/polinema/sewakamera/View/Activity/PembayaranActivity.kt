package com.polinema.sewakamera.View.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.midtrans.sdk.uikit.api.model.CreditCard
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.CustomerDetails
import com.midtrans.sdk.uikit.api.model.Expiry
import com.midtrans.sdk.uikit.api.model.ItemDetails
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.polinema.sewakamera.Adapter.PembayaranAdapter
import com.polinema.sewakamera.Model.Pembayaran
import com.polinema.sewakamera.R
import com.polinema.sewakamera.databinding.ActivityPembayaranBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PembayaranActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPembayaranBinding
    private lateinit var adapter: PembayaranAdapter
    private var dataList: List<Pembayaran> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        adapter = PembayaranAdapter(dataList)
        binding.rvItems.layoutManager = LinearLayoutManager(this)
        val newDataList = mutableListOf<Pembayaran>(
            Pembayaran("Cable 1", "ID1", "100000", false),
            Pembayaran("Cable 2", "ID2", "200000", false),
            Pembayaran("Cable 3", "ID3", "300000", false),
            Pembayaran("Cable 4", "ID4", "400000", false),
            Pembayaran("Cable 5", "ID5", "500000", false),
            Pembayaran("Cable 6", "ID6", "600000", false),
            Pembayaran("Cable 7", "ID7", "700000", false)
        )
        binding.rvItems.adapter = adapter
        adapter.setData(newDataList)

        UiKitApi.Builder()
            .withMerchantClientKey("SB-Mid-client-GOMEsW6TYo8zRTJJ")
            .withContext(applicationContext)
            .withMerchantUrl("http://192.168.1.12/sewakameranew/sewakameranew/public/api/")
            .enableLog(true)
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .build()
        setLocaleNew("id")

        Log.d("louncher nya",launcher.toString())
        binding.btnBayar.setOnClickListener {
            onTransactionButtonClick(it)
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result?.resultCode == RESULT_OK) {
            result.data?.let {
                val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                Log.d("louncher nya",transactionResult.toString())
                Log.d("louncher nya",transactionResult?.transactionId.toString())
                Log.d("louncher nya","masuk")
                Toast.makeText(this,"${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
            }
        }else {
            Log.d("PaymentResult", "Failed or Cancelled")
        }
    }

    private fun onTransactionButtonClick(view: View) {
        val checkedItems = adapter.getCheckedItems()
        var amount = 0.0

        val itemDetails = ArrayList<ItemDetails>()

        for (item in checkedItems) {
            val name = item.name
            val id = item.id
            val price = item.price.toDouble()

            amount += price

            val detail = ItemDetails(id, price, 1, name)
            itemDetails.add(detail)
        }

        UiKitApi.getDefaultInstance().startPaymentUiFlow(
            this@PembayaranActivity,
            launcher,
            SnapTransactionDetail(UUID.randomUUID().toString(), amount, "IDR"),
            CustomerDetails(
                "budi-6789",
                "Budi",
                "Utomo",
                "budi@utomo.com",
                "0213213123",
                null,
                null
            ), // Customer Details
            itemDetails,
            CreditCard(false, null, null, null, null, null, null, null, null, null),
            "customerIdentifier"+System.currentTimeMillis().toString(),
            null, null, null,
            Expiry(SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault()).format(Date()), Expiry.UNIT_DAY, 1),
        )
    }

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }
}