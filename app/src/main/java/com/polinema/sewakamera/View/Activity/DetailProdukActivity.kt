package com.polinema.sewakamera.View.Activity

import android.content.Context
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
import com.polinema.sewakamera.Adapter.ProdukTerbaruAdapter
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Produk
import com.polinema.sewakamera.R
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
        Session = SessionSewa(this)

        helper = DataHelper()

        getDetailProduk()
//        setProductData()
//        setRecData()
//
//        RecomRecView_ProductDetailsPage.layoutManager = LinearLayoutManager(
//            this,
//            LinearLayoutManager.HORIZONTAL, false
//        )
//        RecomRecView_ProductDetailsPage.setHasFixedSize(true)
//        newProductAdapter = ProductAdapter(newProduct, this)
//        RecomRecView_ProductDetailsPage.adapter = newProductAdapter

        b.backIvProfileFrag.setOnClickListener {
            onBackPressed()
        }

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
                Toast.makeText(this,pPrice.toString(),Toast.LENGTH_SHORT).show()
                insertKeranjang()
                bottomSheetDialod.dismiss()
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

    }

    private fun addProductToBag() {

//        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
//
//        cartViewModel.insert(ProductEntity(pName, qua, pPrice, pPid, pImage))
//        toast("Add to Bag Successfully")
    }

    fun insertKeranjang(){
        val url = Connection()

        val request = object : StringRequest(
            Method.POST,url.url_insert_keranjang, Response.Listener { response -> //HERE
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                val message = jsonObject.getString("message")

                if(success.toBoolean() == true){
                    Toast.makeText(this,message.toString(),Toast.LENGTH_SHORT).show()
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
                hm.put("user_id",Session.getIdUser().toString())
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

                        pName = jsonObject.getString("nama_produk").toString()
                        pPrice = jsonObject.getInt("harga")
                        pPid =  jsonObject.getString("id").toString()
                        pImage = jsonObject.getString("gambar_url")
                        Toast.makeText(this,jsonObject.getString("nama_produk").toString(),Toast.LENGTH_SHORT).show()
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
}