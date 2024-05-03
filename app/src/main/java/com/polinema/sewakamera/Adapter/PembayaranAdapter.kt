package com.polinema.sewakamera.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.polinema.sewakamera.Model.Pembayaran
import com.polinema.sewakamera.R

class PembayaranAdapter (private var dataList: List<Pembayaran>):
    RecyclerView.Adapter<PembayaranAdapter.HolderData>() {
    class HolderData(v: View) : RecyclerView.ViewHolder(v) {
        val name = v.findViewById<TextView>(R.id.item_name)
        val id = v.findViewById<TextView>(R.id.item_id)
        val price = v.findViewById<TextView>(R.id.item_price)
        val check = v.findViewById<CheckBox>(R.id.item_check)
    }
    private val checkedItems = mutableSetOf<Pembayaran>()

    fun getCheckedItems(): Set<Pembayaran> {
        return checkedItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderData {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_pembayaran, parent, false)
        return HolderData(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderData, position: Int) {
        val data = dataList.get(position)
        holder.name.text = data.name
        holder.id.text = data.id
        holder.price.text = data.price
        holder.check.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedItems.add(data)
            } else {
                checkedItems.remove(data)
            }
        }
    }

    fun setData(newDataList: List<Pembayaran>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}