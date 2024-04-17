package com.polinema.sewakamera.View.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Adapter.CategoryAdapter
import com.polinema.sewakamera.Adapter.CoverProdukAdapter
import com.polinema.sewakamera.Adapter.MitraAdapter
import com.polinema.sewakamera.Model.Category
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Mitra
import com.polinema.sewakamera.Model.Produk
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.HomeActivity
import com.polinema.sewakamera.databinding.FragmentMitraBinding
import org.json.JSONArray
import org.json.JSONException

class MitraFragment : Fragment() {

    private lateinit var b : FragmentMitraBinding
    private lateinit var thisParent : HomeActivity
    private lateinit var v : View

    private lateinit var mitraAdapter: MitraAdapter
    private lateinit var mitraList:ArrayList<Mitra>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as HomeActivity
        b = FragmentMitraBinding.inflate(layoutInflater)
        v = b.root

        mitraList = arrayListOf()

        getMitra()

        b.mitraListRecev.layoutManager = GridLayoutManager(context,2,
            LinearLayoutManager.VERTICAL,false)
        b.mitraListRecev.setHasFixedSize(true)
        mitraAdapter = MitraAdapter(activity as Context, mitraList )
        b.mitraListRecev.adapter = mitraAdapter


        return v
    }

    private fun getMitra() {
        val url = Connection()

        val stringRequest = object : StringRequest(
            Method.GET, url.url_get_mitra,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    mitraList.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val mitra = Mitra(
                            id = jsonObject.getInt("id"),
                            name = jsonObject.getString("name"),
                            image = jsonObject.getString("gambar_url")
                        )
                        mitraList.add(mitra)
                    }
                    mitraAdapter.notifyDataSetChanged()
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