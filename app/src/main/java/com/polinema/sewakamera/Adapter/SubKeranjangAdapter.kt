    package com.polinema.sewakamera.Adapter

    import KeranjangAdapter
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.CheckBox
    import android.widget.ImageView
    import android.widget.LinearLayout
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
    import com.polinema.sewakamera.View.Activity.HomeActivity
    import com.polinema.sewakamera.View.Fragment.KeranjangFragment
    import org.json.JSONException
    import org.json.JSONObject

    class SubKeranjangAdapter (
        val productList: MutableList<SubKeranjang>,
        private val parentCheckbox: CheckBox,
        private val keranjangAdapter: KeranjangAdapter,
        private val id_mitra : Int
    ) : RecyclerView.Adapter<SubKeranjangAdapter.ProductViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_cart_produk, parent, false)
            return ProductViewHolder(view)
        }

        private fun getCheckedProducts(): List<SubKeranjang> {
            return productList.filter { it.isChecked }
        }

        private fun sendCheckedProductsToKeranjangAdapter() {
            keranjangAdapter.updateCheckedProducts(id_mitra,getCheckedProducts())
        }

        private fun deleteItemFromCart(itemView: View,idKeranjang: Int) {
            val url = Connection()
            val url_delete = "${url.url_delete_keranjang}/${idKeranjang}"
            val request = JsonObjectRequest(
                Request.Method.DELETE, url_delete, null,
                Response.Listener { response ->
                    try {
                        val success = response.getBoolean("success")
                        val message = response.getString("message")
                        if (success) {
                            productList.removeAll { it.id_keranjang == idKeranjang }
                            notifyDataSetChanged()
                            (itemView.context as? HomeActivity)?.navigateToKeranjangFragment()
//                            Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
                            Log.d("hapus keranjang",message)
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

        private fun updateQuantityInCart(itemView: View,idKeranjang: Int, newQty: Int) {
            val url = Connection()
            val urlUpdate = "${url.url_update_qty_keranjang}/$idKeranjang"

            val params = JSONObject()
            params.put("qty", newQty)

            val request = JsonObjectRequest(
                Request.Method.PUT, urlUpdate, params,
                Response.Listener { response ->
                    try {
                        val success = response.getBoolean("success")
                        val message = response.getString("message")
                        if (success) {
                           Log.d("update qty",message)
//                            notifyDataSetChanged()
//                            keranjangAdapter.refreshAdapter()
                            (itemView.context as? HomeActivity)?.navigateToKeranjangFragment()
                        } else {
//                            Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
                            Log.d("update qty",message)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
//                        Toast.makeText(itemView.context, "Error parsing response", Toast.LENGTH_SHORT).show()
                        Log.d("update qty","errorr datanya")
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
//                    Toast.makeText(itemView.context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    Log.d("update qty","Error: ${error.message}")
                }
            )

            Volley.newRequestQueue(itemView.context).add(request)
        }


        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val produk = productList[position]


            holder.checkboxChild.isChecked = produk.isChecked
            holder.checkboxChild.setOnCheckedChangeListener { _, isChecked ->
                produk.isChecked = isChecked
                keranjangAdapter.dtTotalHarga = 1000
                updateParentCheckStatus()
                this.sendCheckedProductsToKeranjangAdapter()
            }

            Glide.with(holder.itemView.context)
                .load(produk.image)
                .into(holder.imageProdukKeranjang)

            holder.idSubKeranjang.text = produk.id_keranjang.toString()
            holder.namaProdukKeranjang.text = produk.nama_produk
            holder.idprodukKeranjang.text = produk.id_product.toString()
            holder.hargaProdukKeranjang.text = produk.harga.toString()
            holder.quantityTvCart.text = produk.qty.toString()

            holder.produkDelete.setOnClickListener {
                val idKeranjang = productList[position].id_keranjang
                deleteItemFromCart(holder.itemView, idKeranjang)
            }

            holder.minusLayout.setOnClickListener{
                val qty = holder.quantityTvCart.text.toString().toInt()
                if (qty > 1){
                    val qty2= qty -1
                    holder.quantityTvCart.setText(qty2.toString())
                    updateQuantityInCart(holder.itemView,holder.idSubKeranjang.text.toString().toInt(), qty2)
                }
            }
            holder.plusLayout.setOnClickListener{
                val qty = holder.quantityTvCart.text.toString().toInt() + 1
                holder.quantityTvCart.setText(qty.toString())
                updateQuantityInCart(holder.itemView,holder.idSubKeranjang.text.toString().toInt(), qty)
            }
        }


        private fun updateParentCheckStatus() {
            val allChecked = productList.all { it.isChecked }
            parentCheckbox.isChecked = allChecked
        }

        fun updateCheckStatus(checked: Boolean) {
//            val allChecked = productList.all { it.isChecked }
            val data = productList

            var datasChecked = mutableListOf<Any>()

            data.forEach { datas ->
                datasChecked.add(datas.isChecked)
            }

            val allSame = datasChecked.all { it == datasChecked[0] }
            if(allSame){
                productList.forEach { it.isChecked = checked }
                notifyDataSetChanged()
            }else{
                if(checked){
                    productList.forEach { it.isChecked = checked }
                    notifyDataSetChanged()
                }
            }
        }
        override fun getItemCount(): Int {
            return productList.size
        }
        class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val idSubKeranjang: TextView = itemView.findViewById(R.id.idSubKeranjang)
            val idprodukKeranjang: TextView = itemView.findViewById(R.id.idprodukKeranjang)
            val imageProdukKeranjang: ImageView = itemView.findViewById(R.id.imageProdukKeranjang)
            val checkboxChild: CheckBox = itemView.findViewById(R.id.checkbox_child)
            val namaProdukKeranjang: TextView = itemView.findViewById(R.id.namaProdukKeranjang)
            val hargaProdukKeranjang: TextView = itemView.findViewById(R.id.hargaProdukKeranjang)
            val quantityTvCart: TextView = itemView.findViewById(R.id.quantityTvCart)
            val produkDelete: ImageView = itemView.findViewById(R.id.produkDelete)
            val minusLayout: LinearLayout = itemView.findViewById(R.id.minusLayout)
            val plusLayout: LinearLayout = itemView.findViewById(R.id.plusLayout)

        }
    }