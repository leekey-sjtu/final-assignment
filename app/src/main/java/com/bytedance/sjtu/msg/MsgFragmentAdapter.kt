package com.bytedance.sjtu.msg

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R
import com.bytedance.sjtu.news.NewsDetailActivity
import com.bytedance.sjtu.shop.MenuActivity

class MsgFragmentAdapter(
    private val mContext: Context,
    private val List: MutableList<String>
): RecyclerView.Adapter<MsgFragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_msg_fragment_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {  //绑定数据
        holder.name.text = "hahaha"
        holder.itemView.setOnClickListener {
            Toast.makeText(mContext, "You click item $position", Toast.LENGTH_SHORT).show()
            skipActivity()
        }
    }

    override fun getItemCount(): Int {
        return List.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.tvName)

    }

    private fun skipActivity() {
        val intent = Intent(mContext, ChatActivity::class.java)
//        intent.putExtra("name", name)
        startActivity(mContext, intent,null)
    }

}