package com.bytedance.sjtu.news

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R
import com.bytedance.sjtu.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class News3Fragment : Fragment() {

    private lateinit var mContext: Context  //获取上下文
    private val recyclerView: RecyclerView by lazy { requireView().findViewById(R.id.recyclerView) }  //要加requireView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mContext = requireActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getHotList()
    }

    private fun getHotList() {
        getRetrofit("https://v2.alapi.cn/api/tophub/")
            .create(HotListService::class.java)
            .getHotList("weibo", "oLOJadEsoHlYkQh1")
            .enqueue(object : Callback<HotListBean> {
                override fun onResponse(call: Call<HotListBean>, response: Response<HotListBean>) {
                    Log.d("wdw", "get HotList success")
                    val hotList = response.body()?.data?.list
                    recyclerView.layoutManager = LinearLayoutManager(mContext)
                    recyclerView.setItemViewCacheSize(40)  //设置recyclerView屏幕外的缓存大小
                    recyclerView.adapter = hotList?.let { News3FragmentAdapter(mContext, it) }  //hotList应该不为空
                }
                override fun onFailure(call: Call<HotListBean>, t: Throwable) {
                    Log.d("wdw", "get HotList failed, $t")
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE  //切换状态栏字体为白色
    }

}