import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.polinema.sewakamera.Adapter.SubKeranjangAdapter
import com.polinema.sewakamera.Model.Keranjang
import com.polinema.sewakamera.Model.SubKeranjang
import com.polinema.sewakamera.R
class KeranjangAdapter(
    private val ctx: Context,
    private val keranjangList: ArrayList<Keranjang>
) : RecyclerView.Adapter<KeranjangAdapter.CartViewHolder>() {

    var checkedProducts=  ArrayList<HashMap<String, Any>>()
    val dataAllChecked = ArrayList<HashMap<String, Any>>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val cartView = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_single, parent, false)
        return CartViewHolder(cartView)
    }

    var dtTotalHarga = 30

    fun getCheckedProductsFromSubKeranjang(): ArrayList<HashMap<String, Any>> {
        return this.checkedProducts
    }

    fun updateCheckedProducts(id_mitra : Int,checkedProducts: List<SubKeranjang>) {
        if(checkedProducts.isEmpty()){
            dataAllChecked.removeIf { it["id_mitra"] == id_mitra }
        }else{
            dataAllChecked.removeIf { it["id_mitra"] == id_mitra }
            checkedProducts.forEach { datas ->
                val item = HashMap<String, Any>()
                item["id"] = datas.id_keranjang
                item["id_mitra"] = datas.id_mitra
                item["id_product"] = datas.id_product
                item["nama_produk"] = datas.nama_produk
                item["image"] = datas.image
                item["type"] = datas.type
                item["harga"] = datas.harga
                item["qty"] = datas.qty
                item["deskripsi"] = datas.deskripsi
                dataAllChecked.add(item)
            }
        }
        this.checkedProducts = dataAllChecked
    }

    fun refreshAdapter() {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val keranjang = keranjangList[position]

        Glide.with(ctx)
            .load(keranjang.image_mitra)
            .into(holder.imageMitraKeranjang)

        holder.idKeranjang.text = keranjang.id_keranjang.toString()
        holder.namaMitraKeranjang.text = keranjang.nama_mitra
        holder.totalHargaKeranjang.text = "10"
        holder.subKeranjangCart = ArrayList()
        holder.subKeranjangCart.clear()
        holder.subKeranjangCart.addAll(keranjang.produk.map { fs ->
            SubKeranjang(
                id_keranjang = fs.id_keranjang,
                id_mitra = fs.id_mitra,
                id_product = fs.id_product,
                nama_produk = fs.nama_produk,
                image = fs.image,
                type = fs.type,
                harga = fs.harga,
                qty = fs.qty,
                deskripsi = fs.deskripsi
            )
        })

        holder.checkboxParent.isChecked = keranjang.isChecked
        holder.checkboxParent.setOnCheckedChangeListener { _, isChecked ->
            keranjang.isChecked = isChecked
            holder.subKeranjangAdapter.updateCheckStatus(isChecked)
        }



        holder.recyclerViewProduk.layoutManager =  LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
        holder.subKeranjangAdapter = SubKeranjangAdapter(holder.subKeranjangCart, holder.checkboxParent,this,keranjang.id_mitra)
        holder.subKeranjangAdapter.notifyDataSetChanged()
        holder.recyclerViewProduk.adapter = holder.subKeranjangAdapter

//        this.dtTotalHarga =  holder.subKeranjangAdapter.calculateTotalPrice()
        Log.d("cek total di keranjang",dtTotalHarga.toString())

    }

    override fun getItemCount(): Int {
        return keranjangList.size
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageMitraKeranjang: ImageView = itemView.findViewById(R.id.imageMitraKeranjang)
        val namaMitraKeranjang: TextView = itemView.findViewById(R.id.namaMitraKeranjang)
        val totalHargaKeranjang: TextView = itemView.findViewById(R.id.totalHargaKeranjang)
        val idKeranjang: TextView = itemView.findViewById(R.id.idKeranjang)
        val recyclerViewProduk: RecyclerView = itemView.findViewById(R.id.recyclerViewProdukSubKer)
        val checkboxParent: CheckBox = itemView.findViewById(R.id.checkbox_parent)


        lateinit var subKeranjangAdapter: SubKeranjangAdapter
        lateinit var subKeranjangCart : ArrayList<SubKeranjang>
    }
}
