package com.bytedance.sjtu.msg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R
import kotlin.random.Random

class ChatActivity : AppCompatActivity() {

    private val recyclerView : RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val editMsg : EditText by lazy { findViewById(R.id.editMsg) }
    private val sendMsg : TextView by lazy { findViewById(R.id.sendMsg) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)   //设置隐藏状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  //切换状态栏字体为黑色

        val msgList = mutableListOf(
            Msg( "你好", Msg.SEND),
            Msg( "嗯嗯", Msg.GET),
            Msg( "How are you?", Msg.SEND),
            Msg( "I'm fine, thanks.", Msg.GET),
            )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChatAdapter(this, msgList)

        editMsg.addTextChangedListener(object : TextWatcher {  //监听消息输入框的输入状态
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if (editMsg.text.isNotEmpty()) {
                    sendMsg.setTextColor(resources.getColor(R.color.silver_red))  //输入框有内容，发布字体变红色
                } else {
                    sendMsg.setTextColor(resources.getColor(R.color.light_gray))  //否则变灰色
                }
            }
        })
        sendMsg.setOnClickListener {
            if (editMsg.text.isNotEmpty()) {
                val msgType = Random.nextInt(0, 2)  //随机生成 0 和 1
                msgList.add(Msg(editMsg.text.toString(), msgType))  //模拟“发送消息”和“接收消息”
                recyclerView.adapter = ChatAdapter(this, msgList)  //更新适配器数据
                recyclerView.scrollToPosition(msgList.size - 6)
                recyclerView.smoothScrollBy(0, 1000, DecelerateInterpolator(), 1500)  //平滑刷新消息界面
                editMsg.text = null
            }
        }
    }
}