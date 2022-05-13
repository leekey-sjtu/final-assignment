package com.bytedance.sjtu.me

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bytedance.sjtu.R
import com.bytedance.sjtu.post.CameraActivity
import com.bytedance.sjtu.uriToPath
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class EditInfoActivity : AppCompatActivity() {

    private val readRequestCode = 101  //读文件权限的RequestCode
    private val locationRequestCode = 102  //定位权限的RequestCode
    private val albumRequestCode = 103  //打开系统相册的RequestCode

    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val imgAvatar: ImageView by lazy { findViewById(R.id.imgAvatar) }
    private val etUserName: EditText by lazy { findViewById(R.id.et_userName) }
    private val etIntroduction : EditText by lazy { findViewById(R.id.et_introduction) }
    private val tvGender : TextView by lazy { findViewById(R.id.tv_gender) }
    private val tvBirthday : TextView by lazy { findViewById(R.id.tv_birthday) }
    private val tvLocation : TextView by lazy { findViewById(R.id.tv_location) }
    private val etSchool : EditText by lazy { findViewById(R.id.et_school) }
    private val tvCommitInfo : TextView by lazy { findViewById(R.id.tv_commitInfo) }

    private val layChangeAvatar : LinearLayout by lazy { findViewById(R.id.lay_changeAvatar) }
    private val layGender : LinearLayout by lazy { findViewById(R.id.lay_gender) }
    private val layBirthday : LinearLayout by lazy { findViewById(R.id.lay_birthday) }
    private val layLocation : LinearLayout by lazy { findViewById(R.id.lay_location) }

    private val viewShadow : View by lazy { findViewById(R.id.viewShadow) }  //使整个activity变暗
    private val view1 : View by lazy { findViewById(R.id.view_1) }
    private val view2 : View by lazy { findViewById(R.id.view_2) }
    private val view3 : View by lazy { findViewById(R.id.view_3) }

    private val dbHelper = SQLiteHelper(this, "user.db", 1)  //数据库OpenHelper
    private var db: SQLiteDatabase? = null  //声明数据库db
    private val avatarOutputStream = ByteArrayOutputStream()  //获取头像jpg的字节输出流

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)   //设置隐藏状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  //初始化状态栏字体为黑色

        db = dbHelper.writableDatabase  //首先获取数据库引用
        initInfo()  //然后读取初始化个人资料
        imgBack.setOnClickListener {  //监听返回按钮
            finish()
        }
        layChangeAvatar.setOnClickListener {  //监听修改用户头像
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {  //查询读文件权限
                ActivityCompat.requestPermissions(this, listOf(Manifest.permission.READ_EXTERNAL_STORAGE).toTypedArray(), readRequestCode)
            } else {
                changeAvatar()  //有权限就可以选择图片修改头像
            }
        }
        layGender.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    view1.alpha = 1f
                }
                MotionEvent.ACTION_UP -> {
                    viewAnimator(view1)
                    viewShadowAnimator()
                    val popView = LayoutInflater.from(this).inflate(R.layout.popupwindow_gender, null)  //给popWin设置contentView
                    val popWin = PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
                    popWin.isFocusable = true  //设置焦点
                    popWin.isTouchable = true  //设置触摸
                    popWin.isOutsideTouchable = true  //设置点击外部可取消
                    popWin.setBackgroundDrawable(ColorDrawable(0x00000000))  //要设置背景才会生效！
                    popWin.setTouchInterceptor { view, event ->  false }  //监听触摸拦截，返回false，touch事件才不会被拦截
                    popWin.setOnDismissListener { viewShadowAnimator() }  //监听取消事件
                    popWin.animationStyle = R.style.anim_style_popwin_gender  //设置style进出动画效果
                    popWin.showAtLocation(viewShadow, Gravity.BOTTOM, 0, 0)  //设置显示位置
                    popWinGenderOnClick(popView, popWin)  //给popWin设置点击事件
                }
            }
            true
        }
        layBirthday.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    view2.alpha = 1f
                }
                MotionEvent.ACTION_UP -> {
                    viewAnimator(view2)
                    viewShadowAnimator()
                    val popView = LayoutInflater.from(this).inflate(R.layout.popupwindow_birthday, null)  //给popWin设置contentView
                    val popWin = PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
                    popWin.isFocusable = true  //设置焦点
                    popWin.isTouchable = true  //设置触摸
                    popWin.isOutsideTouchable = true  //设置点击外部可取消
                    popWin.setBackgroundDrawable(ColorDrawable(0x00000000))  //要设置背景才会生效！
                    popWin.setTouchInterceptor { view, event ->  false }  //监听触摸拦截，返回false，touch事件才不会被拦截
                    popWin.setOnDismissListener { viewShadowAnimator() }  //监听取消事件
                    popWin.animationStyle = R.style.anim_style_popwin_gender  //设置style进出动画效果
                    popWin.showAtLocation(viewShadow, Gravity.BOTTOM, 0, 0)  //设置显示位置
                    popWinBirthdayOnClick(popView, popWin)  //给popWin设置点击事件
                }
            }
            true
        }
        layLocation.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    view3.alpha = 1f
                }
                MotionEvent.ACTION_UP -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {  //询问获取定位权限
                        ActivityCompat.requestPermissions(this, listOf(Manifest.permission.ACCESS_FINE_LOCATION).toTypedArray(), locationRequestCode)
                    } else {
                        viewAnimator(view3)
                        viewShadowAnimator()
                        val popView = LayoutInflater.from(this).inflate(R.layout.popupwindow_location, null)  //给popWin设置contentView
                        val popWin = PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
                        popWin.isFocusable = true  //设置焦点
                        popWin.isTouchable = true  //设置触摸
                        popWin.isOutsideTouchable = true  //设置点击外部可取消
                        popWin.setBackgroundDrawable(ColorDrawable(0x00000000))  //要设置背景才会生效！
                        popWin.setTouchInterceptor { view, event ->  false }  //监听触摸拦截，返回false，touch事件才不会被拦截
                        popWin.setOnDismissListener { viewShadowAnimator() }  //监听取消事件
                        popWin.animationStyle = R.style.anim_style_popwin_gender  //设置style进出动画效果
                        popWin.showAtLocation(viewShadow, Gravity.BOTTOM, 0, 0)  //设置显示位置
                        popWinLocationOnClick(popView, popWin)  //给popWin设置点击事件
                    }
                }
            }
            true
        }
        tvCommitInfo.setOnClickListener {
            commitInfo()  //提交个人资料
        }
    }

    private fun initInfo() {
        val userName = getSharedPreferences("login", MODE_PRIVATE).getString("userName", "")  //获取登录用户名
        val cursor = db!!.query("user", null, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                if (userName == cursor.getString(cursor.getColumnIndex("userName"))) {  //根据登录用户名, 取出对应用户信息
                    etUserName.setText(userName)
                    etIntroduction.setText(cursor.getString(cursor.getColumnIndex("introduction")))
                    tvGender.text = cursor.getString(cursor.getColumnIndex("gender"))
                    tvBirthday.text = cursor.getString(cursor.getColumnIndex("birthday"))
                    tvLocation.text = cursor.getString(cursor.getColumnIndex("location"))
                    etSchool.setText(cursor.getString(cursor.getColumnIndex("school")))
                    val byteArray = cursor.getBlob(cursor.getColumnIndex("avatar"))  //利用字节流加载用户头像
                    val inputStream = ByteArrayInputStream(byteArray)
                    val drawable = Drawable.createFromStream(inputStream, "")
                    imgAvatar.setImageDrawable(drawable)
                    break
                }
            } while (cursor.moveToNext())
        }
        cursor.close()  //关闭游标
    }

    private fun commitInfo() {
        Toast.makeText(this, "个人资料修改成功~", Toast.LENGTH_LONG).show()
        val oldUserName = getSharedPreferences("login", MODE_PRIVATE).getString("userName", "")  //获取旧的登录用户名
        val newUserName = etUserName.text.toString()
        val values = ContentValues().apply {
            put("userName", newUserName)
            put("gender", tvGender.text.toString())
            put("birthday", tvBirthday.text.toString())
            put("location", tvLocation.text.toString())
            put("school", etSchool.text.toString())
            put("introduction", etIntroduction.text.toString())
            put("avatar", avatarOutputStream.toByteArray())
        }
        db?.update("user", values, "userName = ?", arrayOf(oldUserName))  //更新数据库
        getSharedPreferences("login", MODE_PRIVATE).edit()  //同时将newUserName写入SharedPreferences
            .putString("userName", newUserName)
            .apply()
    }

    private fun popWinGenderOnClick(popView: View, popWin: PopupWindow) {
        popView.findViewById<Button>(R.id.btn_male).setOnClickListener {
            tvGender.text = "男"
            popWin.dismiss()
        }
        popView.findViewById<Button>(R.id.btn_female).setOnClickListener {
            tvGender.text = "女"
            popWin.dismiss()
        }
        popView.findViewById<Button>(R.id.btn_gender_secret).setOnClickListener {
            tvGender.text = "保密"
            popWin.dismiss()
        }
        popView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            popWin.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun popWinBirthdayOnClick(popView: View, popWin: PopupWindow) {
        val datePicker : DatePicker = popView.findViewById(R.id.date_picker)
        val tvConfirm : TextView = popView.findViewById(R.id.tv_confirm)
        datePicker.maxDate = System.currentTimeMillis()  //设置最大选择日期
        datePicker.init(tvBirthday.text.substring(0, 4).toInt(),tvBirthday.text.substring(5, 7).toInt() - 1, tvBirthday.text.substring(8, 10).toInt(), null)  //初始化picker日期
        tvConfirm.setOnClickListener {  //监听确认
            val year = datePicker.year.toString()
            val month = if ((datePicker.month + 1) >= 10) (datePicker.month + 1).toString() else "0" + (datePicker.month + 1).toString()  //月份范围是[0,11]
            val day = if (datePicker.dayOfMonth >= 10) datePicker.dayOfMonth.toString() else "0" + datePicker.dayOfMonth.toString()
            tvBirthday.text = "$year-$month-$day"
            popWin.dismiss()
        }
    }

    private fun popWinLocationOnClick(popView: View, popWin: PopupWindow) {
        val location : TextView = popView.findViewById(R.id.tv_location)
        location.text = getAddress()
        location.setOnClickListener {
            if (location.text != "定位失败") {
                tvLocation.text = location.text
            }
            popWin.dismiss()
        }
    }

    //根据纬度、经度获取具体的地点信息
    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getAddress() : String {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (location != null) {
            val addrList = Geocoder(this).getFromLocation(location.latitude, location.longitude, 1)
            if (addrList.size > 0) {
                for (i in addrList.indices) {
                    val addr = addrList[i]
                    return addr.locality + "·" + addr.subLocality  //分别获取市、区
                }
            }
        }
        return "定位失败"
    }

    //使得整个窗口变暗
    private fun viewShadowAnimator() {
        val animator1 = if (viewShadow.alpha == 0f) {
            ObjectAnimator.ofFloat(viewShadow, "alpha", 0f, 1f)
        } else {
            ObjectAnimator.ofFloat(viewShadow, "alpha", 1f, 0f)
        }
        val animSet = AnimatorSet()
        animSet.duration = 300
        animSet.play(animator1)
        animSet.start()
    }

    private fun changeAvatar() {
        Toast.makeText(this,"请尽量选择正方形图片~~~", Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_PICK)  //直接调用系统相册选择照片
        intent.type = "image/*"
        startActivityForResult(intent, albumRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == albumRequestCode && data != null) {
            val imgUri = data.data!!  //获取图片的Uri
            val imgPath = uriToPath(this, imgUri)  //获取图片的path
            Log.d("wdw", "imgUri.path = $imgPath")
            val bitmap = BitmapFactory.decodeFile(imgPath)  //获取bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, avatarOutputStream)  //压缩50%头像文件避免OOM
            imgAvatar.setImageURI(imgUri)  //同时设置选择的头像预览照片
        }
    }

    private fun viewAnimator(view: View) {
        val animator1 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        val animSet = AnimatorSet()
        animSet.duration = 300
        animSet.play(animator1)
        animSet.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == readRequestCode) {
            changeAvatar()  //有权限就可以选择图片修改头像
        } else if (requestCode == locationRequestCode) {
            //TODO()
        }
    }

}