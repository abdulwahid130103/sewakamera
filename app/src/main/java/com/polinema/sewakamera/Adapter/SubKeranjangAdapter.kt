package com.polinema.sewakamera.Adapter

import KeranjangAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.SubKeranjang
import com.polinema.sewakamera.R
import org.json.JSONException

class SubKeranjangAdapter (
    val productList: MutableList<SubKeranjang>,
    private val parentCheckbox: CheckBox
    ) : RecyclerView.Adapter<SubKeranjangAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_cart_produk, parent, false)
        return ProductViewHolder(view)
    }

    private fun deleteItemFromCart(itemView: View,idProduct: Int) {
        val url = Connection()
        val url_delete = "${url.url_delete_keranjang}/${idProduct}"
        val request = JsonObjectRequest(
            Request.Method.DELETE, url_delete, null,
            Response.Listener { response ->
                try {
                    val success = response.getBoolean("success")
                    val message = response.getString("message")
                    if (success) {
                        productList.removeAll { it.id_product == idProduct }
                        notifyDataSetChanged()
                        Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(itemView.context, "Error parsing response", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(itemView.context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(itemView.context).add(request)
    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val produk = productList[position]

        holder.checkboxChild.isChecked = produk.isChecked
        holder.checkboxChild.setOnCheckedChangeListener { _, isChecked ->
            produk.isChecked = isChecked
            updateParentCheckStatus()
        }

        Glide.with(holder.itemView.context)
            .load(produk.image)
            .into(holder.imageProdukKeranjang)

        holder.namaProdukKeranjang.text = produk.nama_produk
        holder.idprodukKeranjang.text = produk.id_product.toString()
        holder.hargaProdukKeranjang.text = produk.harga.toString()
        holder.quantityTvCart.text = produk.qty.toString()

        holder.produkDelete.setOnClickListener {
            val idProduct = productList[position].id_product
            deleteItemFromCart(holder.itemView, idProduct)
        }

    }
    private fun updateParentCheckStatus() {
        val allChecked = productList.all { it.isChecked }
        parentCheckbox.isChecked = allChecked
    }

    fun updateCheckStatus(checked: Boolean) {
        productList.forEach { it.isChecked = checked }
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return productList.size
    }
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idprodukKeranjang: TextView = itemView.findViewById(R.id.idprodukKeranjang)
        val imageProdukKeranjang: ImageView = itemView.findViewById(R.id.imageProdukKeranjang)
        val checkboxChild: CheckBox = itemView.findViewById(R.id.checkbox_child)
        val namaProdukKeranjang: TextView = itemView.findViewById(R.id.namaProdukKeranjang)
        val hargaProdukKeranjang: TextView = itemView.findViewById(R.id.hargaProdukKeranjang)
        val quantityTvCart: TextView = itemView.findViewById(R.id.quantityTvCart)
        val produkDelete: ImageView = itemView.findViewById(R.id.produkDelete)
    }
}