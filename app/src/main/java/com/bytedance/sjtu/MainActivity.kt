package com.bytedance.sjtu

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bytedance.sjtu.me.LoginActivity
import com.bytedance.sjtu.me.MeFragment
import com.bytedance.sjtu.news.NewsFragment
import com.bytedance.sjtu.post.CameraActivity
import com.bytedance.sjtu.shop.MenuActivity
import com.bytedance.sjtu.shop.ShopFragment
import com.bytedance.sjtu.video.VideoFragment

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val navigationBarNews: LinearLayout by lazy { findViewById(R.id.lay_navigation_bar_news) }
    private val navigationBarVideo: LinearLayout by lazy { findViewById(R.id.lay_navigation_bar_video) }
    private val navigationBarPost: LinearLayout by lazy { findViewById(R.id.lay_navigation_bar_post) }
    private val navigationBarShop: LinearLayout by lazy { findViewById(R.id.lay_navigation_bar_shop) }
    private val navigationBarMe: LinearLayout by lazy { findViewById(R.id.lay_navigation_bar_me) }
    private val newsFragment =  NewsFragment()
    private val videoFragment =  VideoFragment()
    private val shopFragment =  ShopFragment()
    private val meFragment = MeFragment()
    private lateinit var currentFragment: Fragment


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)   //设置隐藏状态栏

        supportFragmentManager.beginTransaction().add(R.id.lay_fragment_container, newsFragment).commit()  //初始化首个fragment
        currentFragment = newsFragment  //记录当前fragment
        navigationBarNews.setOnClickListener(this)  //设置导航栏单击事件
        navigationBarVideo.setOnClickListener(this)
        navigationBarPost.setOnClickListener(this)
        navigationBarShop.setOnClickListener{
            Intent(this, MenuActivity::class.java).apply {
                startActivity(this)
            }
        }
        navigationBarMe.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.lay_navigation_bar_news -> {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE  //切换状态栏字体为白色
                switchFragment(newsFragment)
            }
            R.id.lay_navigation_bar_video -> {
                switchFragment(videoFragment)
            }
            R.id.lay_navigation_bar_post -> {
                if (getSharedPreferences("login", MODE_PRIVATE).getBoolean("loginSuccess", false)) {  //根据SharedPreferences判断用户是否登录
                    startActivity(Intent(this, CameraActivity::class.java))  //有用户登录信息直接跳转CameraActivity
                    overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_none)
                } else {  //否则跳到登录界面
                    startActivityForResult(Intent(this, LoginActivity::class.java), 333)
                    overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_none)
                }
            }
//            R.id.lay_navigation_bar_shop -> {
//                switchFragment(shopFragment)
//            }
            R.id.lay_navigation_bar_me -> {
                if (getSharedPreferences("login", MODE_PRIVATE).getBoolean("loginSuccess", false)) {  //根据SharedPreferences判断用户是否登录
                    switchFragment(meFragment)  //有用户登录信息直接跳转meFragment
                } else {  //否则跳到登录界面
                    startActivityForResult(Intent(this, LoginActivity::class.java), 555)
                    overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_none)
                }
            }
        }
    }

    //切换fragment
    private fun switchFragment(toFragment: Fragment) {
        val transaction =  supportFragmentManager.beginTransaction()
        if (!toFragment.isAdded) {
            transaction
                .hide(currentFragment)  //hide当前fragment
                .add(R.id.lay_fragment_container, toFragment)  //先add，再显示新的fragment
                .commit()
        } else {
            transaction
                .hide(currentFragment)  //hide当前的fragment
                .show(toFragment)  //直接显示新的fragment
                .commit()
        }
        currentFragment = toFragment  //记录新的currentFragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val loginResult = data?.getBooleanExtra("loginResult", false)  //获取回传的loginResult的值
        if (requestCode == 555 && resultCode == 666) {
            if (loginResult == true) {  //如果登录成功
                switchFragment(meFragment)  //同时跳转meFragment
                updateLoginInfo(data)  //同时更新SharedPreferences -> login.XML
            }
        } else if (requestCode == 333 && resultCode == 666) {
            if (loginResult == true) {  //如果登录成功
                startActivity(Intent(this, CameraActivity::class.java))  //同时跳转CameraActivity
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_none)
                updateLoginInfo(data)  //同时更新SharedPreferences -> login.XML
            }
        }
    }

    private fun updateLoginInfo(intent: Intent) {
        val userName = intent.getStringExtra("userName")  //同时写入登录信息
        val preferences  = getSharedPreferences("login", MODE_PRIVATE)  //获取Preferences
        val edit = preferences.edit()  //获取edit
        edit.putBoolean("loginSuccess", true)
        edit.putString("userName", userName)
        edit.apply()  //提交修改
    }

}