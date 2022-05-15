package com.bytedance.sjtu.news

import android.graphics.Color
import android.graphics.Color.parseColor
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.bytedance.sjtu.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class NewsFragment : Fragment() {

    private val tabLayout: TabLayout by lazy { requireView().findViewById(R.id.tabLayout) }  //顶部导航栏
    private val viewPager2News: ViewPager2 by lazy { requireView().findViewById(R.id.viewPager2_news) }
    private val news1Fragment = News1Fragment()
    private val news2Fragment = News2Fragment()
    private val news3Fragment = News3Fragment()
    private val news4Fragment = News4Fragment()
    private var categoryList = mutableListOf("国际", "科技", "教育", "娱乐", "女性", "体育", "财经", "军事")  //新闻频道分类列表
    private val handler = Handler(Looper.getMainLooper())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragments = mutableListOf(
            news1Fragment,
            news2Fragment,
            news3Fragment,
            news4Fragment,
            News5Fragment(categoryList[0]),
            News5Fragment(categoryList[1]),
            News5Fragment(categoryList[2]),
            News5Fragment(categoryList[3]),
            News5Fragment(categoryList[4]),
            News5Fragment(categoryList[5]),
            News5Fragment(categoryList[6]),
            News5Fragment(categoryList[7])
        )
        viewPager2News.offscreenPageLimit = 10  //设置viewPager2的缓存页面数量
        viewPager2News.adapter = NewsFragmentVP2Adapter(activity, fragments)  //绑定数据
        val runnable = Runnable {
            run {
                viewPager2News.currentItem = 1  //设置默认页面为第2页
            }
        }
        handler.post(runnable)

        TabLayoutMediator(tabLayout, viewPager2News) { tab: TabLayout.Tab, position: Int ->  //绑定TabLayout与viewPager2Video
            tab.customView = layoutInflater.inflate(R.layout.layout_news_tab, null)
            val textView = tab.view.findViewById<TextView>(R.id.tv_video_tab)
            when (position) {
                0 -> { textView.text = "关注" }  //固定标签分类
                1 -> { textView.text = "推荐" }
                2 -> { textView.text = "热榜" }
                3 -> { textView.text = "疫情" }
                4 -> { textView.text = categoryList[0] }  //可变标签分类
                5 -> { textView.text = categoryList[1] }
                6 -> { textView.text = categoryList[2] }
                7 -> { textView.text = categoryList[3] }
                8 -> { textView.text = categoryList[4] }
                9 -> { textView.text = categoryList[5] }
                10 -> { textView.text = categoryList[6] }
                11 -> { textView.text = categoryList[7] }
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val textView = tab.customView!!.findViewById<TextView>(R.id.tv_video_tab)
                textView.setTextColor(parseColor("#e44845"))
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val textView = tab.customView!!.findViewById<TextView>(R.id.tv_video_tab)
                textView.setTextColor(Color.BLACK)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE  //切换状态栏字体为白色
    }

}