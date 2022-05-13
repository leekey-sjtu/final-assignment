package com.bytedance.sjtu.news

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R

class News3FragmentAdapter(
    private val context: Context,
    private val hotList: MutableList<HotListBean.News>
    ): RecyclerView.Adapter<News3FragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_news_3_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {  //绑定数据
        holder.num.text = (position + 1).toString()
        if (position <= 2) {
            holder.num.setTextColor(Color.parseColor("#e44845"))
            holder.num.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
        }
        holder.title.text = hotList[position].title
        holder.clout.text = hotList[position].other
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "You click item $position", Toast.LENGTH_SHORT).show()
            skipDetailActivity(hotList[position].link, hotList[position].title)  //新闻网页的url和title
            holder.title.setTextColor(Color.parseColor("#999999"))
        }
    }

    override fun getItemCount(): Int {
        return hotList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val num: TextView = itemView.findViewById(R.id.tv_num)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val clout: TextView = itemView.findViewById(R.id.tv_clout)
    }

    private fun skipDetailActivity(newsUrl: String, newsTitle: String) {
        val intent = Intent(context, NewsDetailActivity::class.java)
        intent.putExtra("newsUrl", newsUrl)
        intent.putExtra("newsTitle", newsTitle)
        startActivity(context, intent,null)
    }
}