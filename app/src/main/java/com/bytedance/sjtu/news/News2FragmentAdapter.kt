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

class News2FragmentAdapter(
    private val mContext: Context,
    private val newsList: MutableList<NewsBean.News>
    ): RecyclerView.Adapter<News2FragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_news_5_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {  //绑定数据
        holder.title.text = newsList[position].title
        holder.passtime.text = newsList[position].passtime
        Glide.with(mContext)
            .load(newsList[position].image)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .placeholder(R.drawable.ic_loading_gif)
            .error(R.drawable.ic_loading_error)
            .into(holder.image)
        holder.itemView.setOnClickListener {
            Toast.makeText(mContext, "You click item $position", Toast.LENGTH_SHORT).show()
            skipDetailActivity(newsList[position].path,
//                newsList[position].title
                "wdw"
            )  //新闻网页的url和title
            holder.title.setTextColor(Color.parseColor("#999999"))
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val image: ImageView = itemView.findViewById(R.id.iv_image)
        val passtime: TextView = itemView.findViewById(R.id.tv_passtime)
    }

    private fun skipDetailActivity(newsUrl: String, newsTitle: String) {
        val intent = Intent(mContext, NewsDetailActivity::class.java)
        intent.putExtra("newsUrl", newsUrl)
        intent.putExtra("newsTitle", newsTitle)
        startActivity(mContext, intent,null)
    }
}