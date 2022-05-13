package com.bytedance.sjtu.me

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.bytedance.sjtu.MainActivity
import com.bytedance.sjtu.R

class SettingActivity : AppCompatActivity() {

    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val layEditInfo: FrameLayout by lazy { findViewById(R.id.lay_editInfo) }
    private val layLogout: FrameLayout by lazy { findViewById(R.id.lay_logout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)   //设置隐藏状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  //初始化状态栏字体为黑色

        imgBack.setOnClickListener {  //监听返回按钮
            finish()
        }
        layEditInfo.setOnClickListener {
            startActivity(Intent(this, EditInfoActivity::class.java))
        }
        layLogout.setOnClickListener {
            Toast.makeText(this, "用户退出登录~", Toast.LENGTH_SHORT).show()
            val preferences  = getSharedPreferences("login", MODE_PRIVATE)  //同时清空登录信息
            val edit = preferences.edit()
            edit.putBoolean("loginSuccess", false)
            edit.putString("userName", "")
            edit.apply()  //提交修改
            startActivity(Intent(this, MainActivity::class.java))  //然后返回首页
        }
    }

}