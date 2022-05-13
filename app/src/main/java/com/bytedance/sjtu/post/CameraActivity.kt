package com.bytedance.sjtu.post

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bytedance.sjtu.R
import com.bytedance.sjtu.uriToPath
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var camera: Camera
    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private lateinit var cameraExecutor: ExecutorService
    private var imgPath: String? = null
    private var videoPath: String? = null

    private val viewFinder: PreviewView by lazy { findViewById(R.id.viewFinder) }  //cameraX的预览
    private val layCamera: FrameLayout by lazy { findViewById(R.id.lay_camera) }  //拍照时的布局
    private val layPreview: LinearLayout by lazy { findViewById(R.id.lay_preview) }  //拍照完成的布局
    private val photoPreview: ImageView by lazy { findViewById(R.id.photo_preview) }  //拍照完成显示照片
    private val videoPreview: VideoView by lazy { findViewById(R.id.video_preview) }  //拍照完成显示视频
    private val imgFocusing: ImageView by lazy { findViewById(R.id.img_focusing) }  //手动对焦
    private val photoShutter: ImageView by lazy { findViewById(R.id.photo_shutter) }
    private val videoShutter: ImageView by lazy { findViewById(R.id.video_shutter) }
    private val videoShutter2: ImageView by lazy { findViewById(R.id.video_shutter_2) }
    private val tabLayout: TabLayout by lazy { findViewById(R.id.tabLayout_camera) }
    private val btnCancel: Button by lazy { findViewById(R.id.btn_cancel) }
    private val btnConfirm: Button by lazy { findViewById(R.id.btn_confirm) }
    private var handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)   //设置隐藏状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE  //初始化状态栏字体为白色

        viewFinder.scaleType = PreviewView.ScaleType.FIT_CENTER  //设置PreviewView缩放类型
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionsGranted()) {  //检查全部权限
            initCamera()
        } else {
            requestPermission()
        }

        photoShutter.setOnClickListener {
            takePhoto()
        }

        videoShutter.setOnClickListener {
            recordVideo()
        }

        viewFinder.setOnTouchListener(object : View.OnTouchListener {
            private var mLastTime: Long = 0
            private  var mCurrentTime: Long = 0
            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mLastTime = System.currentTimeMillis()
                    }
                    MotionEvent.ACTION_MOVE -> {
                    }
                    MotionEvent.ACTION_UP -> {
                        mCurrentTime = System.currentTimeMillis()
                        if (mCurrentTime - mLastTime <= 500 || mLastTime == 0L) {  //判断onClick事件
                            imgFocusing.x = motionEvent.x - (imgFocusing.width / 2)  //改变对焦图片位置
                            imgFocusing.y = motionEvent.y - (imgFocusing.height / 2)
                            imgFocusingAnimator1()  //播放对焦动画
                            handler.postDelayed({ imgFocusingAnimator2() }, 2000)  //3s后隐藏对焦动画
                            val focusMeteringAction = FocusMeteringAction.Builder(
                                viewFinder.meteringPointFactory.createPoint(motionEvent.x, motionEvent.y)
                            ).build()
                            camera.cameraControl.startFocusAndMetering(focusMeteringAction)  //实现camera对焦
                        }
                    }
                }
                return true
            }
        })

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    photoShutter.visibility = View.VISIBLE
                    videoShutter.visibility = View.GONE
                    videoShutter2.visibility = View.GONE
                } else {
                    photoShutter.visibility = View.GONE
                    videoShutter.visibility = View.VISIBLE
                    videoShutter2.visibility = View.VISIBLE
                    videoShutter2.alpha = 0f
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        btnCancel.setOnClickListener {   //取消上传照片/视频
            layPreview.visibility = View.GONE
            layCamera.visibility = View.VISIBLE
        }

        btnConfirm.setOnClickListener {  //确认上传照片/视频
            if (imgPath == null) {
                Toast.makeText(this, "需要拍照提供封面~", Toast.LENGTH_SHORT).show()
            } else if (videoPath == null) {
                Toast.makeText(this, "需要录像提供视频~", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PostActivity::class.java)
                intent.putExtra("imgPath", imgPath)
                intent.putExtra("videoPath", videoPath)
                startActivity(intent)
            }
        }
    }

    private fun initCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)  //创建ProcessCameraProvider实例
        cameraProviderFuture.addListener({  //给cameraProviderFuture添加监听
            val cameraProvider = cameraProviderFuture.get()  //获取相机信息
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA  //默认后置摄像头
            val preview = Preview.Builder().build().also { it.setSurfaceProvider(viewFinder.surfaceProvider) }  //viewFinder设置预览画面
            imageCapture = ImageCapture.Builder().build()
            videoCapture = VideoCapture.withOutput(Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HD)).build())
            cameraProvider.unbindAll()  //先解除再绑定
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, videoCapture)  //Bind use cases to camera
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        takePhotoAnimator()  //拍照动画
        val imgName = "IMG_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(System.currentTimeMillis())  //使用当前时间来命名
        val contentValues = ContentValues().apply {  //contentValues
            put(MediaStore.MediaColumns.DISPLAY_NAME, imgName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) { put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/TikTok") }
        }
        val outputOptions = ImageCapture.OutputFileOptions  //创建OutputFileOptions
            .Builder(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            .build()
        imageCapture!!.takePicture(outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults){
                imgPath = uriToPath(this@CameraActivity, output.savedUri!!)  //更新照片的path name
                Log.d("wdw", "照片已保存至：${output.savedUri} \n path = $imgPath")
                layPreview.visibility = View.VISIBLE
                photoPreview.visibility = View.VISIBLE
                videoPreview.visibility = View.GONE
                layCamera.visibility = View.GONE
                photoPreview.setImageURI(output.savedUri)
            }
            override fun onError(exc: ImageCaptureException) {
                Log.e("wdw", "imageCapture failed: ${exc.message}", exc)
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun recordVideo() {
        if (recording != null) {  //清空已存在的recording会话
            recording!!.stop()
            recording = null
            return
        }
        val videoName =  "VID_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, videoName)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) { put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/TikTok") }
        }
        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .setFileSizeLimit(25 * 1024 * 1024)  //限制文件大小25MB
            .build()
        recording = videoCapture!!.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .withAudioEnabled()
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when(recordEvent) {
                    is VideoRecordEvent.Start -> {  //开始录像
                        Toast.makeText(this, "开始录制", Toast.LENGTH_SHORT).show()
                        recordAnimator1()  //开始录像动画
                    }
                    is VideoRecordEvent.Finalize -> {  //结束录像
                        if (!recordEvent.hasError()) {  //判断录像是否有错
                            recordAnimator2()  //结束录像动画
                            videoPath = uriToPath(this, recordEvent.outputResults.outputUri)  //更新视频的path name
                            Log.d("wdw","视频已保存至：${recordEvent.outputResults.outputUri} \n videoPath = $videoPath")
                            layPreview.visibility = View.VISIBLE
                            photoPreview.visibility = View.GONE
                            videoPreview.visibility = View.VISIBLE
                            layCamera.visibility = View.GONE
                            videoPreview.setVideoURI(recordEvent.outputResults.outputUri)
                            videoPreview.start()
                            videoPreview.setOnCompletionListener { videoPreview.start() }  //循环播放视频
                        } else {
                            recording?.close()
                            recording = null
                            Log.e("wdw", "videoCapture error: ${recordEvent.error}")
                        }
                    }
                }
            }
    }

    private fun imgFocusingAnimator1() {
        imgFocusing.alpha = 1f
        handler.removeCallbacksAndMessages(null)  //TODO(如何移除特定的callback message?)
        val animator1 = ObjectAnimator.ofFloat(imgFocusing, "scaleX", 1.5f, 1f)
        val animator2 = ObjectAnimator.ofFloat(imgFocusing, "scaleY", 1.5f, 1f)
        val animSet = AnimatorSet()
        animSet.duration = 300
        animSet.play(animator1).with(animator2)
        animSet.start()
    }

    private fun imgFocusingAnimator2() {
        val animator = ObjectAnimator.ofFloat(imgFocusing, "alpha", 1f, 0f)
        val animSet = AnimatorSet()
        animSet.duration = 300
        animSet.play(animator)
        animSet.start()
    }

    private fun takePhotoAnimator() {
        val animator1 = ObjectAnimator.ofFloat(photoShutter, "scaleX", 1f, 0.9f, 1f)
        val animator2 = ObjectAnimator.ofFloat(photoShutter, "scaleY", 1f, 0.9f, 1f)
        val animSet = AnimatorSet()
        animSet.duration = 200
        animSet.play(animator1).with(animator2)
        animSet.start()
    }

    private fun recordAnimator1() {
        val animator1 = ObjectAnimator.ofFloat(videoShutter, "scaleX", 1f, 0.5f)
        val animator2 = ObjectAnimator.ofFloat(videoShutter, "scaleY", 1f, 0.5f)
        val animator3 = ObjectAnimator.ofFloat(videoShutter, "alpha", 1f, 0f)
        val animator4 = ObjectAnimator.ofFloat(videoShutter2, "scaleX", 0.5f, 1f)
        val animator5 = ObjectAnimator.ofFloat(videoShutter2, "scaleY", 0.5f, 1f)
        val animator6 = ObjectAnimator.ofFloat(videoShutter2, "alpha", 0f, 1f)
        val animSet = AnimatorSet()
        animSet.duration = 200
        animSet.play(animator1).with(animator2).with(animator3).with(animator4).with(animator5).with(animator6)
        animSet.start()
    }

    private fun recordAnimator2() {
        val animator1 = ObjectAnimator.ofFloat(videoShutter2, "scaleX", 1f, 0.5f)
        val animator2 = ObjectAnimator.ofFloat(videoShutter2, "scaleY", 1f, 0.5f)
        val animator3 = ObjectAnimator.ofFloat(videoShutter2, "alpha", 1f, 0f)
        val animator4 = ObjectAnimator.ofFloat(videoShutter, "scaleX", 0.5f, 1f)
        val animator5 = ObjectAnimator.ofFloat(videoShutter, "scaleY", 0.5f, 1f)
        val animator6 = ObjectAnimator.ofFloat(videoShutter, "alpha", 0f, 1f)
        val animSet = AnimatorSet()
        animSet.duration = 200
        animSet.play(animator1).with(animator2).with(animator3).with(animator4).with(animator5).with(animator6)
        animSet.start()
    }

    //返回键关闭相机
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition( R.anim.slide_none, R.anim.slide_out_bottom)
        cameraExecutor.shutdown()
    }

    //检查是否拥有全部权限
    private fun allPermissionsGranted() = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    //申请未获得的权限
    private fun requestPermission() {
        val permissions = mutableListOf<String>()
        for (per in PERMISSIONS_REQUIRED) {
            if (ContextCompat.checkSelfPermission(this, per) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(per)
            }
        }
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSIONS_REQUEST_CODE)
    }

    //返回用户选择的权限结果
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (allPermissionsGranted()) {
                initCamera()
            } else {
                Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 1001
        private val PERMISSIONS_REQUIRED = mutableListOf (
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).apply {
            if (Build.VERSION.SDK_INT <= 28) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)  //SdkVersion<=28要加上写权限
            }
        }.toTypedArray()
    }

}