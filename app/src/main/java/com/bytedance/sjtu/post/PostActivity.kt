package com.bytedance.sjtu.post

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.bytedance.sjtu.R
import com.bytedance.sjtu.getRetrofit
import com.bytedance.sjtu.video.VideoService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PostActivity : AppCompatActivity() {

    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val extraValue: EditText by lazy { findViewById(R.id.et_extra_value) }
    private val btnCancel: Button by lazy { findViewById(R.id.btn_cancel) }
    private val btnConfirm: Button by lazy { findViewById(R.id.btn_confirm) }
    private val layUploading: FrameLayout by lazy { findViewById(R.id.lay_uploading) }
    private val layPostSuccess: LinearLayout by lazy { findViewById(R.id.lay_post_success) }
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)   //设置隐藏状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  //初始化状态栏字体为黑色

        imgBack.setOnClickListener {  //监听返回按钮
            finish()
        }
        btnCancel.setOnClickListener {  //监听取消按钮
            finish()
        }
        btnConfirm.setOnClickListener {  //监听确认发布按钮
            val userName = getSharedPreferences("login", MODE_PRIVATE).getString("userName", "")!!  //获取当前登录用户名
            val extraValue = extraValue.text.toString()  //获取视频extraValue
            val imgPath = intent.getStringExtra("imgPath")!!  //获取imgPath
            val videoPath = intent.getStringExtra("videoPath")!!  //获取videoPath
            if (extraValue.isEmpty()) {
                Toast.makeText(this, "视频描述不能为空~", Toast.LENGTH_SHORT).show()
            } else {
                postVideo(userName, extraValue, imgPath, videoPath)
                layUploading.visibility = View.VISIBLE
            }
        }
    }

    //提交封面和视频
    private fun postVideo(userName: String, extraValue: String, imgPath: String, videoPath: String) {
        val imageFile = File(imgPath)
        val imageBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("cover_image", imageFile.name, imageBody)
        val videoFile = File(videoPath)
        val videoBody = videoFile.asRequestBody("video/mp4".toMediaTypeOrNull())
        val videoPart = MultipartBody.Part.createFormData("video", videoFile.name, videoBody)
        getRetrofit().create(VideoService::class.java)
            .postVideo("121110910068_post", userName, extraValue, imagePart, videoPart)
            .enqueue(object : Callback<PostVideoBean> {
                override fun onResponse(call: Call<PostVideoBean>, response: Response<PostVideoBean>) {
                    if (response.body()?.success == true) {  //若response返回success
                        postSuccessAnimator()  //发布视频成功的动画
                        handler.postDelayed({ finish() }, 2000)  //延迟2000ms关闭当前activity
                    }
                }
                override fun onFailure(call: Call<PostVideoBean>, t: Throwable) {
                    Log.d("wdw", "postVideo failed, $t")
                }
            })
    }


    private fun postSuccessAnimator() {
        layUploading.visibility = View.GONE  //隐藏uploading动画
        layPostSuccess.visibility =  View.VISIBLE  //显示loadSuccess动画
        val animator1 = ObjectAnimator.ofFloat(layPostSuccess, "scaleX", 0f, 1.1f, 1f)
        val animator2 = ObjectAnimator.ofFloat(layPostSuccess, "scaleY", 0f, 1.1f, 1f)
        val animSet = AnimatorSet()
        animSet.duration = 500
        animSet.play(animator1).with(animator2)
        animSet.start()
    }

}