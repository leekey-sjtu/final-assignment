package com.bytedance.sjtu.news

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bytedance.sjtu.R
import com.bytedance.sjtu.news.covidBean.StatisGradeCityDetail

class News4FragmentAdapter(
    private val mContext: Context,
    private val cityList: MutableList<StatisGradeCityDetail>
    ) : RecyclerView.Adapter<News4FragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_news_4_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {  //绑定数据
        holder.tvCity.text = cityList[position].city
        holder.tvProvince.text = cityList[position].province
        holder.tvConfirmAdd.text = cityList[position].confirmAdd.toString()  //新增确诊
        holder.tvNowConfirm.text = cityList[position].nowConfirm.toString()  //现有确诊
        holder.tvWzzAdd.text = cityList[position].wzz_add  //新增无症状
        if (holder.tvConfirmAdd.text != "0") {
            holder.tvConfirmAdd.setTextColor(Color.parseColor("#f44336"))
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCity: TextView = itemView.findViewById(R.id.tv_city)
        val tvProvince: TextView = itemView.findViewById(R.id.tv_province)
        val tvConfirmAdd: TextView = itemView.findViewById(R.id.tv_confirmAdd)
        val tvNowConfirm: TextView = itemView.findViewById(R.id.tv_nowConfirm)
        val tvWzzAdd: TextView = itemView.findViewById(R.id.tv_wzzAdd)
    }

}