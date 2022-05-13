package com.bytedance.sjtu.news

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R
import com.bytedance.sjtu.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var newsUrl: String
    private lateinit var newsTitle: String
    private lateinit var commentList: MutableList<CommentBean.Comments>
    private val scrollView: NestedScrollView by lazy { findViewById(R.id.scrollView) }
    private val webView: WebView by lazy { findViewById(R.id.webView) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val layComment: LinearLayout by lazy { findViewById(R.id.lay_comment) }
    private val skipComment: FrameLayout by lazy { findViewById(R.id.skip_comment) }
    private val commentCount1: TextView by lazy { findViewById(R.id.comment_count_1) }
    private val commentCount2: TextView by lazy { findViewById(R.id.comment_count_2) }
    private val bottomBar1: LinearLayout by lazy { findViewById(R.id.bottomBar1) }
    private val bottomBar2: LinearLayout by lazy { findViewById(R.id.bottomBar2) }
    private val tvEditComment: TextView by lazy { findViewById(R.id.tv_edit_comment) }
    private val editComment: EditText by lazy { findViewById(R.id.edit_comment) }
    private val postComment: TextView by lazy { findViewById(R.id.post_comment) }
    private val handler = Handler(Looper.getMainLooper())

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news2_detail)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)   //设置隐藏状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  //初始化状态栏字体为黑色

        newsUrl = intent.getStringExtra("newsUrl")!!
        newsTitle = intent.getStringExtra("newsTitle")!!
        getNewsDetail(newsUrl)  //获取新闻内容
        getComment(newsTitle)  //获取新闻评论

        recyclerView.setOnTouchListener { view, event ->  //监听评论列表的点击事件
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    hideEditComment()
                }
                MotionEvent.ACTION_UP -> {
                    view.performClick()
                }
            }
            true
        }
        skipComment.setOnClickListener {  //监听跳转评论区的点击事件
            scrollView.smoothScrollTo(0, layComment.top)
        }
        tvEditComment.setOnClickListener {  //监听旧输入框的点击事件
            showEditComment()
        }
        editComment.setOnFocusChangeListener { view, hasFocus ->  //监听输入框的焦点状态
            if (!hasFocus) {
                hideEditComment()
            }
        }
        editComment.addTextChangedListener(object : TextWatcher {  //监听输入框的输入状态
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if (editComment.text.isNotEmpty()) {
                    postComment.setTextColor(resources.getColor(R.color.silver_red))  //输入框有内容，发布字体变红色
                } else {
                    postComment.setTextColor(resources.getColor(R.color.light_gray))  //否则变灰色
                }
            }
        })
        postComment.setOnClickListener {  //监听发布评论的点击事件
            val comment = editComment.text.toString()
            if (comment.isNotEmpty()) {
                postComment(comment)
                handler.postDelayed({  //延迟50ms刷新评论区
                    getComment(newsTitle)
                    editComment.setText("")  //清空输入框
                    hideEditComment()  //隐藏输入框
                    scrollView.smoothScrollTo(0, layComment.top)  //定位到评论区顶部
                }, 50)

            } else {
                Toast.makeText(this, "输入不能为空~", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun getNewsDetail(newsUrl: String) {
        webView.loadUrl(newsUrl)
        webView.webViewClient = WebViewClient()
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true  //启用js
        webSettings.blockNetworkImage = false  //显示网络图片
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW  //设置http和https混用
        webSettings.standardFontFamily = "Time News Roman"  //设置 WebView 的字体，默认字体为 "sans-serif"
        webSettings.defaultFontSize = 30  //设置 WebView 字体的大小，默认大小为 16
        webSettings.minimumFontSize = 12  //设置 WebView 支持的最小字体大小，默认为 8
    }

    private fun getComment(newsTitle: String) {
        getRetrofit().create(NewsService::class.java)
            .getComment(newsTitle)
            .enqueue(object : Callback<CommentBean> {
                override fun onResponse(call: Call<CommentBean>, response: Response<CommentBean>) {
                    Log.d("wdw", "get comment success")
                    commentList = response.body()!!.comments.asReversed()
                    recyclerView.layoutManager = LinearLayoutManager(this@NewsDetailActivity)
                    recyclerView.adapter = NewsDetailAdapter(this@NewsDetailActivity, commentList)
                    commentCount1.text = commentList.size.toString()  //更新总评论数量
                    commentCount2.text = commentList.size.toString()
                }
                override fun onFailure(call: Call<CommentBean>, t: Throwable) {
                    Log.d("wdw", "get comment failed -> $t")
                }
            })
    }

    private fun postComment(comment: String) {
        getRetrofit().create(NewsService::class.java)
            .postComment(PostCommentInfo("121110910068", newsTitle, comment))
            .enqueue(object : Callback<postCommentBean> {
                override fun onResponse(call: Call<postCommentBean>, response: Response<postCommentBean>) {
                    Log.d("wdw", "postComment success, newsTitle = $newsTitle, comment = $comment")
                    Toast.makeText(this@NewsDetailActivity, "发布评论成功~", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<postCommentBean>, t: Throwable) {
                    Log.d("wdw", "postComment failed -> $t")
                }
            })
    }

    private fun showEditComment() {
        bottomBar1.visibility = View.GONE  //隐藏旧输入框
        bottomBar2.visibility = View.VISIBLE
        editComment.isFocusable = true
        editComment.isFocusableInTouchMode = true  //同时让新输入框获得焦点
        editComment.requestFocus()
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editComment, 0)  //同时自动调起软键盘
    }

    private fun hideEditComment() {
        bottomBar1.visibility = View.VISIBLE  //恢复原来的输入框
        bottomBar2.visibility = View.GONE
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)  //同时自动收起软键盘
    }

}