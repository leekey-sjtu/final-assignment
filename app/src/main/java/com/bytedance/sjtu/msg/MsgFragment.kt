package com.bytedance.sjtu.msg

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R

class MsgFragment : Fragment() {

    private lateinit var mContext: Context  //获取上下文
    private val recyclerView : RecyclerView by lazy { requireView().findViewById(R.id.recyclerView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.mContext = requireActivity()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_msg, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = mutableListOf("吴德威","张三","李四","王五","abcd","123","1433223","2333","1234","2022","196","278","122","2345","124","2645","1645","212")
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = MsgFragmentAdapter(mContext, list)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  //切换状态栏字体为黑色
    }

}