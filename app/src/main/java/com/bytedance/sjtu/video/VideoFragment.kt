package com.bytedance.sjtu.video

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bytedance.sjtu.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class VideoFragment : Fragment() {

    private val tabLayout: TabLayout by lazy { requireView().findViewById(R.id.tabLayout) }  //顶部导航栏
    private val viewPager2Video: ViewPager2 by lazy { requireView().findViewById(R.id.viewPager2_video) }
    private val video1Fragment = Video1Fragment()
    private val video2Fragment = Video2Fragment()
    private val video3Fragment = Video3Fragment()
    private val video4Fragment = Video4Fragment()
    private val video5Fragment = Video5Fragment()
    private val video6Fragment = Video6Fragment()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragments = mutableListOf(
            video1Fragment,
            video2Fragment,
            video3Fragment,
            video4Fragment,
            video5Fragment,
            video6Fragment
        )
        viewPager2Video.adapter = VideoViewPager2Adapter(activity, fragments)  //绑定数据
        val runnable = Runnable {
            run {
                viewPager2Video.currentItem = 1  //设置默认页面为第2页
            }
        }
        handler.post(runnable)

        TabLayoutMediator(tabLayout, viewPager2Video) { tab: TabLayout.Tab, position: Int ->  //绑定TabLayout与viewPager2Video
            tab.customView = layoutInflater.inflate(R.layout.layout_video_tab, null)
            val textView = tab.view.findViewById<TextView>(R.id.tv_video_tab)
            when (position) {
                    0 -> { textView.text = "关注" }
                    1 -> { textView.text = "推荐" }
                    2 -> { textView.text = "视频" }
                    3 -> { textView.text = "上海" }
                    4 -> { textView.text = "图片" }
                    5 -> { textView.text = "文字" }
                }
            }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("wdw", "${tab!!.position} onTabSelected")
                val textView = tab.customView!!.findViewById<TextView>(R.id.tv_video_tab)
                if (tab.position == 2) textView.setTextColor(Color.WHITE) else textView.setTextColor(Color.BLACK)
                val animator1 = ObjectAnimator.ofFloat(textView, "scaleX", 1f, 1.3f)
                val animator2 = ObjectAnimator.ofFloat(textView, "scaleY", 1f, 1.3f)
                val animSet = AnimatorSet()
                animSet.duration = 100
                animSet.play(animator1).with(animator2)
                animSet.start()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("wdw", "${tab!!.position} onTabUnselected")
                val textView = tab.customView!!.findViewById<TextView>(R.id.tv_video_tab)
                textView.setTextColor(Color.parseColor("#949494"))
                val animator1 = ObjectAnimator.ofFloat(textView, "scaleX", 1.3f, 1f)
                val animator2 = ObjectAnimator.ofFloat(textView, "scaleY", 1.3f, 1f)
                val animSet = AnimatorSet()
                animSet.duration = 100
                animSet.play(animator1).with(animator2)
                animSet.start()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

    }

}