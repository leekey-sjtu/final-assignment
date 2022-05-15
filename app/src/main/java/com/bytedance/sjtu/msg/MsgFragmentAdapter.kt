package com.bytedance.sjtu.msg

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R

class MsgFragmentAdapter(
    private val mContext: Context,
    private val List: MutableList<String>
): RecyclerView.Adapter<MsgFragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_msg_fragment_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {  //绑定数据
        holder.name.text = List[position]
        holder.itemView.setOnClickListener {
            skipActivity(List[position])
        }
    }

    override fun getItemCount(): Int {
        return List.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.tvName)

    }

    private fun skipActivity(name: String) {
        val intent = Intent(mContext, ChatActivity::class.java)
        intent.putExtra("name", name)
        startActivity(mContext, intent,null)
    }

}