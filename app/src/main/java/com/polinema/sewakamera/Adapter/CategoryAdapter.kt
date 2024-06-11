package com.polinema.sewakamera.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.polinema.sewakamera.Model.Category
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.ProdukListActivity

class CategoryAdapter (var ctx: Context, private val categoryList: ArrayList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val categoryView = LayoutInflater.from(parent.context).inflate(R.layout.category_single,parent,false)
        return ViewHolder(categoryView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val item: Category = categoryList[position]

        holder.categoryName_CateSingle.text = item.name
        holder.category_id_menu.text = item.id.toString()

        Glide.with(ctx)
            .load(item.image_category)
            .into(holder.categoryImage_CateSingle)

        holder.itemView.setOnClickListener {
            val intent = Intent(ctx, ProdukListActivity::class.java)
            intent.putExtra("data_category", item.name)
            ctx.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val categoryImage_CateSingle: ImageView = itemView.findViewById(R.id.categoryImage_CateSingle)
        val categoryName_CateSingle: TextView = itemView.findViewById(R.id.categoryName_CateSingle)
        val category_id_menu: TextView = itemView.findViewById(R.id.category_id_menu)

    }

}