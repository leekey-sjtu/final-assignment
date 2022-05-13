package com.bytedance.sjtu.news

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R
import com.bytedance.sjtu.getRetrofit
import com.bytedance.sjtu.news.covidBean.COVID19Bean
import com.bytedance.sjtu.news.covidBean.Data
import com.bytedance.sjtu.news.covidBean.StatisGradeCityDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class News4Fragment : Fragment() {

    private lateinit var mContext: Context  //获取上下文
    private val tvUpdateTime : TextView by lazy { requireView().findViewById(R.id.tv_updateTime) }
    private val tvAddConfirm : TextView by lazy { requireView().findViewById(R.id.tv_addConfirm) }
    private val tvAddWzz : TextView by lazy { requireView().findViewById(R.id.tv_addWzz) }
    private val tvAddLocalConfirm : TextView by lazy { requireView().findViewById(R.id.tv_addLocalConfirm) }
    private val tvAddHeal : TextView by lazy { requireView().findViewById(R.id.tv_addHeal) }
    private val tvNowConfirm : TextView by lazy { requireView().findViewById(R.id.tv_nowConfirm) }
    private val tvNowWzz : TextView by lazy { requireView().findViewById(R.id.tv_nowWzz) }
    private val tvNowHeal : TextView by lazy { requireView().findViewById(R.id.tv_nowHeal) }
    private val tvNowDead : TextView by lazy { requireView().findViewById(R.id.tv_nowDead) }
    private val recyclerView : RecyclerView by lazy { requireView().findViewById(R.id.recyclerView) }
    private val scrollView: NestedScrollView by lazy { requireView().findViewById(R.id.scrollView) }
    private val layTitleBar : LinearLayout by lazy { requireView().findViewById(R.id.lay_titleBar) }
    private val layFixedTitleBar : LinearLayout by lazy { requireView().findViewById(R.id.lay_fixedTitleBar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mContext = requireActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news_4, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCOVID19Data()
        scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY >= layTitleBar.y.toInt()) {  //显示、隐藏标题栏
                layFixedTitleBar.alpha = 1f
            } else {
                layFixedTitleBar.alpha = 0f
            }
        }
    }

    private fun getCOVID19Data() {
        getRetrofit("https://api.inews.qq.com/newsqa/v1/query/inner/publish/modules/")
            .create(COVID19Service::class.java)
            .getData("statisGradeCityDetail,diseaseh5Shelf")
            .enqueue(object : Callback<COVID19Bean> {
                override fun onResponse(call: Call<COVID19Bean>, response: Response<COVID19Bean>) {
                    Log.d("wdw", "get COVID19 success")
                    response.body()?.data?.let { updateCOVID19Data(it) }  //data应该不为空
                }
                override fun onFailure(call: Call<COVID19Bean>, t: Throwable) {
                    Log.d("wdw", "get COVID19 failed, $t")
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun updateCOVID19Data(data: Data) {
        val diseaseShelf = data.diseaseh5Shelf
        tvUpdateTime.text = "更新时间：" + diseaseShelf.lastUpdateTime

        val chinaAdd = diseaseShelf.chinaAdd
        tvAddConfirm.text = chinaAdd.confirm.toString()  //新增确诊
        tvAddWzz.text = chinaAdd.noInfect.toString()  //新增无症状
        tvAddLocalConfirm.text = chinaAdd.localConfirmH5.toString()  //新增本土
        tvAddHeal.text = chinaAdd.heal.toString()  //新增治愈

        val chinaTotal = diseaseShelf.chinaTotal
        tvNowConfirm.text = chinaTotal.nowConfirm.toString()  //现有确诊
        tvNowWzz.text = chinaTotal.noInfect.toString()  //现有无症状
        tvNowHeal.text = chinaTotal.heal.toString()  //累计治愈
        tvNowDead.text = chinaTotal.dead.toString()  //累计死亡

        val cityList = data.statisGradeCityDetail
        cityList.sortWith { city1, city2 ->  //将cityList依次按照新增确诊、现有确诊、新增无症状降序排列
            if (city1.confirmAdd != city2.confirmAdd) {
                city2.confirmAdd.compareTo(city1.confirmAdd)
            } else if (city1.nowConfirm != city2.nowConfirm) {
                city2.nowConfirm.compareTo(city1.nowConfirm)
            } else {
                city2.wzz_add.compareTo(city1.wzz_add)
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = News4FragmentAdapter(mContext, cityList)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE  //切换状态栏字体为白色
    }

}