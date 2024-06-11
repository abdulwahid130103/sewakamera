package com.polinema.sewakamera.View.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.polinema.sewakamera.Adapter.CoverProdukAdapter
import com.polinema.sewakamera.Adapter.ProdukTerbaruAdapter
import com.polinema.sewakamera.Adapter.RekomendasiAdapter
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Produk
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Auth.LoginActivity
import com.polinema.sewakamera.databinding.ActivityDetailProdukBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale

class DetailProdukActivity : AppCompatActivity() {

    lateinit var b : ActivityDetailProdukBinding
    var productIndex: Int = 0
    lateinit var ProductFrom: String

    lateinit var newProductAdapter: ProdukTerbaruAdapter
    lateinit var newProduct: ArrayList<Produk>

    private lateinit var rekomendasiData:ArrayList<Produk>

    private lateinit var rekomendasiDataAdapter: RekomendasiAdapter

    lateinit var helper : DataHelper

    lateinit var pName: String
    var qua: Int = 1
    var pPrice: Int = 0
    lateinit var pPid: String
    lateinit var pImage: String


    lateinit var cardNumber: String
    private lateinit var Session: SessionSewa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailProdukBinding.inflate(layoutInflater)
        setContentView(b.root)

        productIndex = intent.getIntExtra("ProductIndex",0)
        ProductFrom = intent.getStringExtra("ProductFrom").toString()

        newProduct = arrayListOf()
        rekomendasiData = arrayListOf()
        Session = SessionSewa(this)

        helper = DataHelper(this)

        getDetailProduk()
        Session = SessionSewa(this)
        b.backIvProfileFrag.setOnClickListener {
            onBackPressed()
        }

        b.rekomendasiRecycleViewDetailProduk.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        b.rekomendasiRecycleViewDetailProduk.setHasFixedSize(true)
        rekomendasiDataAdapter = RekomendasiAdapter(rekomendasiData,this )
        b.rekomendasiRecycleViewDetailProduk.adapter = rekomendasiDataAdapter
        b.addToCartProductDetailsPage.setOnClickListener {

            val bottomSheetDialod = BottomSheetDialog(
                this, R.style.BottomSheetDialogTheme
            )

            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.fragment_add_to_bag,
                findViewById<ConstraintLayout>(R.id.bottomSheet)
            )

            bottomSheetView.findViewById<View>(R.id.addToCart_BottomSheet).setOnClickListener {

                pPrice *= bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom).text.toString()
                    .toInt()

                if(Session.getLogin()){
                    insertKeranjang()
                    bottomSheetDialod.dismiss()
                }else{
                    bottomSheetDialod.dismiss()
                    helper.toast_custom_alert(this, "notifikasi","Anda harus login untuk memasukkan produk ke keranjang")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }

            bottomSheetView.findViewById<LinearLayout>(R.id.minusLayout).setOnClickListener {
                if(bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom).text.toString()
                        .toInt() > 1){
                    qua--
                    bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom).setText(qua.toString())
                }
            }

            bottomSheetView.findViewById<LinearLayout>(R.id.plusLayout).setOnClickListener {
                if(bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom).text.toString()
                        .toInt() < 10){
                    qua++
                    bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom).setText(qua.toString())
                }
            }

            bottomSheetDialod.setContentView(bottomSheetView)
            bottomSheetDialod.show()
        }
  
        b.penilaianDetailProdukContainer.setOnClickListener{
            val intent = Intent(this , DetailPenilaianActivity::class.java)
            intent.putExtra("id_produk", productIndex)
            startActivity(intent)
        }

        b.mitraProdukContainer.setOnClickListener{
            val intent = Intent(this , DetailMitraActivity::class.java)
            intent.putExtra("data_mitra", b.idMitraDetailProduk.text.toString())
            startActivity(intent)
        }

        b.lihatSemuaDetailProduk.setOnClickListener{
            val intent = Intent(this , ProdukListActivity::class.java)
            startActivity(intent)
        }

    }

    fun insertKeranjang(){
        val url = Connection()

        val request = object : StringRequest(
            Method.POST,url.url_insert_keranjang, Response.Listener { response -> //HERE
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                val message = jsonObject.getString("message")

                if(success.toBoolean() == true){
                    helper.toast_custom_alert(this, "success",message)
                }else{
                    Toast.makeText(this,"gagal insert keranjang",Toast.LENGTH_SHORT).show()
                }

            },
            Response.ErrorListener { error -> //HERE
                Toast.makeText(this,"Tidak dapat terhubung ke server",
                    Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String,String>()
                hm.put("product_id",productIndex.toString())
                hm.put("user_id",Session.getUserId().toString())
                hm.put("qty",qua.toString())
                hm.put("price",pPrice.toString())
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun getDetailProduk() {
        val url = Connection()
        val url_detail =  url.url_get_detail_produk + "/${productIndex}"

        val stringRequest = object : StringRequest(
            Method.GET, url_detail,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        Glide.with(applicationContext)
                            .load(jsonObject.getString("gambar_url"))
                            .into(b.productImageProductDetailsPage)
                        b.productNameProductDetailsPage.text = jsonObject.getString("nama_produk").toString()
                        b.productPriceProductDetailsPage.text = helper.formatRupiah(jsonObject.getInt("harga")).toString() + "/hari"
                        b.productBrandProductDetailsPage.text = jsonObject.getString("id_category").toString()
                        b.productDesProductDetailsPage.text = jsonObject.getString("deskripsi").toString()
                        b.namaMitraDetailProduk.text = jsonObject.getString("nama_mitra").toString()
                        b.idMitraDetailProduk.text = jsonObject.getInt("id_mitra").toString()
                        b.productRatingSingleProduct.rating = jsonObject.getString("rating").toFloat()

                        pName = jsonObject.getString("nama_produk").toString()
                        pPrice = jsonObject.getInt("harga")
                        pPid =  jsonObject.getString("id").toString()
                        pImage = jsonObject.getString("gambar_url")
                        getRekomendasiProduk()
//                        Toast.makeText(this,jsonObject.getString("nama_produk").toString(),Toast.LENGTH_SHORT).show()
                    }
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

    private fun getRekomendasiProduk(){
        val url = Connection()

        val stringRequest = object : StringRequest(
            Method.GET, url.get_rekomendasi_produk,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    rekomendasiData.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val produk = Produk(
                            id = jsonObject.getInt("id"),
                            id_mitra = jsonObject.getInt("id_mitra"),
                            id_category = jsonObject.getString("id_category"),
                            nama_produk = jsonObject.getString("nama_produk"),
                            image = jsonObject.getString("gambar_url"),
                            type = jsonObject.getString("type"),
                            harga = jsonObject.getInt("harga"),
                            stok = jsonObject.getInt("stok"),
                            deskripsi = jsonObject.getString("deskripsi"),
                            rating = jsonObject.getString("rating").toFloat(),
                        )
                        rekomendasiData.add(produk)
                    }
                    rekomendasiDataAdapter.notifyDataSetChanged()
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