package com.bytedance.sjtu.shop

import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R
import com.bytedance.sjtu.shop.Utils.Utils
import java.util.*

class DemoAdapter : RecyclerView.Adapter<DemoAdapter.TextViewHolder>() {

    private val itemList = mutableListOf<String>()
    private val imgList = intArrayOf(R.drawable.table, R.drawable.apple, R.drawable.cake,
        R.drawable.wearclothes, R.drawable.kiwifruit, R.drawable.scarf)
    private val priceList = arrayOf("2000", "5", "200", "800", "15", "360")
    private val filterList = mutableListOf<String>()
    private var map = TreeMap<String, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        encapsulation()
        return TextViewHolder(v)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.bind(itemList[position], imgList[position], priceList[position])
        holder.setListener(itemList[position])
    }

    override fun getItemCount(): Int =itemList.size

    fun updatedData(list: List<String>){
        itemList.clear()
        itemList.addAll(list)
        //更新列表
        notifyDataSetChanged()
    }
    fun setFilter(filter: String?){
        if (filter?.isNotEmpty()==true){
            val result = itemList.filter{it.contains(filter)}

            filterList.clear()
            filterList.addAll(result)
        }else{
            filterList.clear()
            filterList.addAll(itemList)
        }
        notifyDataSetChanged()
    }

    private fun encapsulation(){

        for(i in 1..6){
            map["第${Utils.format(i)}件商品"]="NO.${Utils.format(i)}"
        }
    }

    val rank = (1..3).random()

    inner class TextViewHolder(view: View) : RecyclerView.ViewHolder(view){


        private val tvItem = view.findViewById<TextView>(R.id.shop_item_tv)
        private val iv = view.findViewById<ImageView>(R.id.iv)
        private val tvPrice = view.findViewById<TextView>(R.id.price)
        private val item = view.findViewById<RelativeLayout>(R.id.item)

        //
        fun bind(text: String, resId: Int, price: String){
            tvItem.text=text
            iv.setImageResource(resId)
            tvPrice.text=price
            initMapBank()
        }

        fun setListener(id: String){
            item.setOnClickListener {
                showDetails(id)
            }
        }

        var mapBank = MapBank()

        private fun initMapBank(){
            mapBank.encapsulation(rank)
        }

        private fun showDetails(id: String){

            var message: String? = mapBank.getMap()[id]
            val finalPrice = (message!![3]-'0')*(priceList[id[2]-'0'-1].toInt())/10
            message+=",折扣后的价格为${finalPrice}"

            val builder = AlertDialog.Builder(itemView.context)
            builder
                .setTitle("商品信息")
                .setMessage(message)
                .setPositiveButton(
                    "返回",
                    object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            Log.d("TAG", "onClick:返回")
                        }
                    })
                .show()
        }

    }
}