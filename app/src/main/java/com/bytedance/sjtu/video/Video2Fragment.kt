package com.bytedance.sjtu.video

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R
import com.bytedance.sjtu.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Video2Fragment : Fragment()  {

    private lateinit var mContext: Context  //获取嵌套的fragment的上下文
    private val handler = Handler(Looper.getMainLooper())
    private val recyclerViewVideo2: RecyclerView by lazy { requireView().findViewById(R.id.recyclerView_video_2) } //requireView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mContext = requireActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getVideo()
    }

    private fun getVideo() {
        getRetrofit().create(VideoService::class.java)
            .getVideo("121110910068")  //改成121110910068 TODO()
            .enqueue(object : Callback<VideoBean> {
                override fun onResponse(call: Call<VideoBean>, response: Response<VideoBean>) {
                    Log.d("wdw", "getVideo success")
                    val videoList = response.body()!!.feeds.asReversed()  //获取所有的视频列表
                    recyclerViewVideo2.layoutManager = LinearLayoutManager(mContext)
                    recyclerViewVideo2.adapter = Video2FragmentAdapter(mContext, handler, videoList)
                }
                override fun onFailure(call: Call<VideoBean>, t: Throwable) {
                    Log.d("wdw", "getVideo failed -> $t")
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  //切换状态栏字体为黑色
    }

}