package com.bytedance.sjtu.video

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bytedance.sjtu.R
import com.bytedance.sjtu.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Video3Fragment : Fragment()  {

    private lateinit var mContext: Context  //获取嵌套的fragment的上下文
    private val handler = Handler(Looper.getMainLooper())
    private val viewPager2Video2: ViewPager2 by lazy { requireView().findViewById(R.id.viewPager2_video_3) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mContext = requireActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRetrofit().create(VideoService::class.java)
            .getVideo("121110910068_portrait")
            .enqueue(object : Callback<VideoBean> {
                override fun onResponse(call: Call<VideoBean>, response: Response<VideoBean>) {
                    Log.d("wdw", "getVideo success")
                    val videoList = response.body()!!.feeds.asReversed()  //获取所有的视频列表
                    viewPager2Video2.adapter = Video3ViewPager2Adapter(mContext, handler,  videoList)
                }
                override fun onFailure(call: Call<VideoBean>, t: Throwable) {
                    Log.d("wdw", "getVideo failed")
                }
            })
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE  //切换状态栏字体为白色
    }

}