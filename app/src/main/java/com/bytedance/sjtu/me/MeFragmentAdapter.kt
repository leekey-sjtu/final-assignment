package com.bytedance.sjtu.me

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R
import com.bytedance.sjtu.shop.MenuActivity

class MeFragmentAdapter(
    private val mContext: Context,
    private val toolList: MutableList<String>
): RecyclerView.Adapter<MeFragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_me_fragment_tools_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {  //绑定数据
        holder.tvToolName.text = toolList[position]
        when (position) {
            0 -> { holder.imgIcon.setImageResource(R.drawable.ic_video) }
            1 -> { holder.imgIcon.setImageResource(R.drawable.ic_shop) }
            2 -> { holder.imgIcon.setImageResource(R.drawable.ic_cart) }
            3 -> { holder.imgIcon.setImageResource(R.drawable.ic_express) }
            4 -> { holder.imgIcon.setImageResource(R.drawable.ic_homepage) }
            5 -> { holder.imgIcon.setImageResource(R.drawable.ic_tool_box) }
        }
        holder.itemView.setOnTouchListener(object : View.OnTouchListener {
            var lastX = 0f
            var lastY = 0f
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        cardAnimator1(holder)
                        lastX = event.x
                        lastY = event.y
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        cardAnimator2(holder)
                    }
                    MotionEvent.ACTION_UP -> {
                        cardAnimator2(holder)
                        if (event.x - lastX < 20 && event.y - lastY < 20) {  //识别click
                            skipActivity(toolList[position])  //跳转activity
                        }
                    }
                }
                return true
            }
        })
    }

    private fun cardAnimator1(holder: ViewHolder) {
        holder.viewShadow.alpha = 0.1f
        val animator1 = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 1f, 0.95f)
        val animator2 = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 1f, 0.95f)
        val animSet = AnimatorSet()
        animSet.duration = 100
        animSet.play(animator1).with(animator2)//.with(animator3).with(animator4)
        animSet.start()
    }

    private fun cardAnimator2(holder: ViewHolder) {
        val animator1 = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0.95f, 1f)
        val animator2 = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 0.95f, 1f)
        val animator5 = ObjectAnimator.ofFloat(holder.viewShadow, "alpha", 0.1f, 0f)
        val animSet = AnimatorSet()
        animSet.duration = 100
        animSet.play(animator1).with(animator2).with(animator5)//.with(animator3).with(animator4)
        animSet.start()
    }

    override fun getItemCount(): Int {
        return toolList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvToolName: TextView = itemView.findViewById(R.id.tvToolName)
        val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
        val imgCover: ImageView = itemView.findViewById(R.id.imgCover)
        val viewShadow: View = itemView.findViewById(R.id.viewShadow)  //卡片灰色遮罩
    }

    private fun skipActivity(tool: String) {
        when (tool) {
            "我的作品" -> {
                //
            }
            "我的商城" -> {
                startActivity(mContext, Intent(mContext, MenuActivity::class.java),null)
            }
            "我的购物车" -> {
                startActivity(mContext, Intent(mContext, MenuActivity::class.java),null)
            }
            "我的快递信息" -> {
                startActivity(mContext, Intent(mContext, MenuActivity::class.java),null)
            }
            "我的个人主页" -> {
                //
            }
            "更多工具 →" -> {
                //
            }
        }
    }

}