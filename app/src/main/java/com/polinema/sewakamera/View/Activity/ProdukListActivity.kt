package com.polinema.sewakamera.View.Activity

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Adapter.ProdukListAdapter
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Produk
import com.polinema.sewakamera.R
import com.polinema.sewakamera.databinding.ActivityProdukListBinding
import org.json.JSONArray
import org.json.JSONException

class ProdukListActivity : AppCompatActivity() {
    lateinit var b : ActivityProdukListBinding
    private lateinit var produkListData:ArrayList<Produk>
    private lateinit var produkListAdapter: ProdukListAdapter

    private lateinit var urutanOptions : ArrayAdapter<String>
    var daftarUrutan = mutableListOf("Semua", "Terbaru", "Terlama")
    var filterUrutanValue =""
    var filterKategoriValue =""


    lateinit var kategoriAdapter : ArrayAdapter<String>
    var daftarKategori = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b =ActivityProdukListBinding.inflate(layoutInflater)
        setContentView(b.root)

        produkListData = arrayListOf()

        getCategorySpinner()

        kategoriAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, daftarKategori)
        b.filterKategori.adapter = kategoriAdapter
        b.filterKategori.onItemSelectedListener = itemSelectedKategori

        urutanOptions = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, daftarUrutan)
        b.filterUrutan.adapter = urutanOptions
        b.filterUrutan.onItemSelectedListener = itemSelected


        val numberOfColumns = 2
        val spacingInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
        b.produkRecycleList.addItemDecoration(GridSpacingItemDecoration(numberOfColumns, spacingInPixels, true))

        b.produkRecycleList.layoutManager = GridLayoutManager(this, numberOfColumns)
        produkListAdapter = ProdukListAdapter(this, produkListData )
        b.produkRecycleList.adapter = produkListAdapter



        b.searchViewPL
            .getEditText()
            .setOnEditorActionListener { v, actionId, event ->
                val searchText = b.searchViewPL.getText().toString()
                getProdukList("search",searchText)
                b.searchViewPL.hide()

                false
            }

        b.backButton.setOnClickListener{
            finish()
        }
    }


    private fun getProdukList(type:String,message:String) {
        val url = Connection()
        var url_fix = "${url.get_list_produk}?type=$type&message=$message"

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    produkListData.clear()

                    if(jsonArray.length() <= 0){
                        b.produkRecycleList.visibility = View.GONE
                        b.dataKosongProdukList.visibility = View.VISIBLE
                    }else{
                        b.produkRecycleList.visibility = View.VISIBLE
                        b.dataKosongProdukList.visibility = View.GONE
                    }
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
                        produkListData.add(produk)
                    }
                    produkListAdapter.notifyDataSetChanged()
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

    val itemSelected = object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {
            b.filterUrutan.setSelection(0)
            filterUrutanValue = daftarUrutan.get(0)
        }
        override fun onItemSelected(parent: AdapterView<*>?,
                                    view: View?, position: Int, id: Long) {
            filterUrutanValue = daftarUrutan.get(position)
            getProdukList("filter", filterUrutanValue.toLowerCase())
        }
    }
    val itemSelectedKategori = object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {
            b.filterKategori.setSelection(0)
            filterKategoriValue = daftarKategori.get(0)
        }
        override fun onItemSelected(parent: AdapterView<*>?,
                                    view: View?, position: Int, id: Long) {
            filterKategoriValue = daftarKategori.get(position)
            getProdukList("filter_categori", filterKategoriValue)
        }
    }

    fun getCategorySpinner(){

        val url = Connection()
        val request = StringRequest(
            Request.Method.GET,url.get_category_spinner, Response.Listener { response ->
                daftarKategori.clear()
                val jsonArray = JSONArray(response)
                for(x in 0 .. (jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(x)
                    daftarKategori.add(jsonObject.getString("name"))
                }
                kategoriAdapter.notifyDataSetChanged()
                cekDataMasuk()
            },
            Response.ErrorListener { error -> }
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
    private fun cekDataMasuk(){
        val dataSearch = intent.getStringExtra("data_search").toString()
        val dataFilter = intent.getStringExtra("data_filter").toString()
        val dataCategory = intent.getStringExtra("data_category").toString()

        if (dataSearch != null && dataSearch != "null") {
            getProdukList("search", dataSearch)
        } else if (dataFilter != null && dataFilter != "null") {
            if (dataFilter == "terbaru"){
                val terbaruPosition = daftarUrutan.indexOf("Terbaru")
                if (terbaruPosition != -1) {
                    b.filterUrutan.setSelection(terbaruPosition)
                }
            }else{
                val terbaruPosition = daftarUrutan.indexOf("Terlama")
                if (terbaruPosition != -1) {
                    b.filterUrutan.setSelection(terbaruPosition)
                }
            }
        }else if (dataCategory != null && dataCategory != "null") {
            val categoryPosition = daftarKategori.indexOf(dataCategory)
            if (categoryPosition != -1) {
                b.filterKategori.setSelection(categoryPosition)
            }
        } else {
            getProdukList("search", "")
        }
    }
}

class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}
