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

class ChatAdapter(
    private val mContext: Context,
    private val msgList: MutableList<Msg>
): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_chat_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {  //绑定数据
        if (msgList[position].msgType == Msg.SEND) {  //发出的消息
            holder.msgSend.text = msgList[position].msg
            holder.msgSend.visibility = View.VISIBLE
            holder.msgGet.visibility = View.GONE
        } else if (msgList[position].msgType == Msg.GET) {  //收到的消息
            holder.msgGet.text = msgList[position].msg
            holder.msgGet.visibility = View.VISIBLE
            holder.msgSend.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
//            Toast.makeText(mContext, "You click msg $position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val msgSend : TextView = itemView.findViewById(R.id.msgSend)
        val msgGet : TextView = itemView.findViewById(R.id.msgGet)
    }

}