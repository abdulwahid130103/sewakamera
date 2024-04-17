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
import org.json.JSONArray
import kotlin.math.log

class KeranjangAdapter(
    private val ctx: Context,
    private val keranjangList: ArrayList<Keranjang>
) : RecyclerView.Adapter<KeranjangAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val cartView = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_single, parent, false)
        return CartViewHolder(cartView)
    }
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val keranjang: Keranjang = keranjangList[position]

        Glide.with(ctx)
            .load(keranjang.image_mitra)
            .into(holder.imageMitraKeranjang)

        holder.namaMitraKeranjang.text = keranjang.nama_mitra
        holder.subKeranjangCart = ArrayList()
        holder.subKeranjangCart.clear()
        holder.subKeranjangCart.addAll(keranjang.produk.map { fs ->
            SubKeranjang(
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
        holder.subKeranjangAdapter = SubKeranjangAdapter(holder.subKeranjangCart, holder.checkboxParent)
        holder.subKeranjangAdapter.notifyDataSetChanged()
        holder.recyclerViewProduk.adapter = holder.subKeranjangAdapter
    }



    override fun getItemCount(): Int {
        return keranjangList.size
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageMitraKeranjang: ImageView = itemView.findViewById(R.id.imageMitraKeranjang)
        val namaMitraKeranjang: TextView = itemView.findViewById(R.id.namaMitraKeranjang)
        val recyclerViewProduk: RecyclerView = itemView.findViewById(R.id.recyclerViewProdukSubKer)
        val checkboxParent: CheckBox = itemView.findViewById(R.id.checkbox_parent)

        lateinit var subKeranjangAdapter: SubKeranjangAdapter
        lateinit var subKeranjangCart : ArrayList<SubKeranjang>
    }
}
