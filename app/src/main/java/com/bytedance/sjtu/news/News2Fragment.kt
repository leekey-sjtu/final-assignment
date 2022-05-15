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

class News2Fragment : Fragment() {

    private lateinit var mContext: Context  //获取上下文
    private val recyclerView: RecyclerView by lazy { requireView().findViewById(R.id.recyclerView_news_2) }  //要加requireView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mContext = requireActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNews()
    }

    private fun getNews() {
        getRetrofit().create(NewsService::class.java)
            .getNews("1", "50")
            .enqueue(object : Callback<NewsBean> {
                override fun onResponse(call: Call<NewsBean>, response: Response<NewsBean>) {
                    Log.d("wdw", "get News success")
                    val newsBean = response.body()
                    val newsList = newsBean!!.news  //TODO(头条api失效)
                    recyclerView.layoutManager = LinearLayoutManager(mContext)
                    recyclerView.adapter = News2FragmentAdapter(mContext, newsListTemp())
                }
                override fun onFailure(call: Call<NewsBean>, t: Throwable) {
                    Log.d("wdw", "get News failed -> $t")
                }
            })
    }

    private fun newsListTemp(): MutableList<NewsBean.News> {
        return mutableListOf(
            NewsBean.News(
                "https://www.toutiao.com/article/7087899746806923790/?log_from=8e27058ab24f6_1651224666687",
                "https://p6.toutiaoimg.com/origin/tos-cn-i-qvj2lq49k0/f2246bedfde34c6b912a5d29ab891253?from=pc",
                "大家都被8小时睡眠论忽悠了？50岁后，最佳的睡眠时间是多少？",
                "发布于 2022-04-18 21:15"),
            NewsBean.News(
                "https://www.toutiao.com/article/7093168719932768779/?log_from=967f336a68d98_1651765682246",
                "https://p26.toutiaoimg.com/origin/tos-cn-i-qvj2lq49k0/9f216d0cfe9e4a53a5b395b46b3e4983?from=pc",
                "微信正式发布8.0.22版，增加了5个新功能",
                "发布于 2022-05-03 09:00"),
            NewsBean.News(
                "https://www.toutiao.com/article/7094242861893534248/?log_from=31ac3304e3c79_1651765843152",
                "https://p6.toutiaoimg.com/origin/tos-cn-i-qvj2lq49k0/69109ad0377441b38439a0c3701ac5ba?from=pc",
                "5月份，养老金上涨方案将公布，高龄倾斜调整差别最大，怎么回事",
                "发布于 2022-05-05 22:11"),
            NewsBean.News(
                "https://www.toutiao.com/article/7073416315402453515/?log_from=10010982c3c76_1651766034211",
                "https://p3.toutiaoimg.com/origin/tos-cn-i-qvj2lq49k0/938dff91ebd9401a8b75fa0d326d86c9?from=pc",
                "东北人喝酒必上的4个“硬菜”，道道特色经典，外地人尝过都说好",
                "发布于 2022-03-10 18:28"),
            NewsBean.News(
                "https://www.toutiao.com/article/7094138672706028066/?log_from=f1859b65baf3a_1651766133021",
                "https://p3.toutiaoimg.com/origin/tos-cn-i-qvj2lq49k0/feae7839e09a409086186e2716030f23?from=pc",
                "泽连斯基警告：各国都必须孤立俄罗斯，并给乌克兰凑3000亿重建费",
                "发布于 2022-05-05 14:45"),
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE  //切换状态栏字体为白色
    }

}