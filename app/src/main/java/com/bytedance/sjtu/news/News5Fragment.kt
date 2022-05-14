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

class News5Fragment(private val category: String) : Fragment() {

    private lateinit var mContext: Context  //获取上下文
    private val recyclerView: RecyclerView by lazy { requireView().findViewById(R.id.recyclerView) }  //要加requireView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mContext = requireActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news_5, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNews()
    }

    private fun getNews() {
        getRetrofit("https://way.jd.com/jisuapi/")
            .create(JDNewsService::class.java)
            .getJDNews(category, "50", "0", "92b9f9e7465ed6a8a72e27330aa8310a")
            .enqueue(object : Callback<JDNewsBean> {
                override fun onResponse(call: Call<JDNewsBean>, response: Response<JDNewsBean>) {
                    Log.d("wdw", "get JDNews success")
                    val newsList = response.body()?.result?.result?.list
                    recyclerView.layoutManager = LinearLayoutManager(mContext)
                    recyclerView.adapter = newsList?.let { News5FragmentAdapter(mContext, it) }  //newsList应该不为空
                }
                override fun onFailure(call: Call<JDNewsBean>, t: Throwable) {
                    Log.d("wdw", "get JDNews failed, $t")
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE  //切换状态栏字体为白色
    }

}