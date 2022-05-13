package com.bytedance.sjtu.me

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bytedance.sjtu.MainActivity
import com.bytedance.sjtu.R

class LoginActivity : AppCompatActivity() {

    private val dbHelper = SQLiteHelper(this, "user.db", 1)  //数据库OpenHelper
    private var db: SQLiteDatabase? = null  //声明数据库db
    private val userName: EditText by lazy { findViewById(R.id.userName) }
    private val imgPwdInv: ImageView by lazy { findViewById(R.id.imgPwdInv) }
    private val imgPwdVis: ImageView by lazy { findViewById(R.id.imgPwdVis) }
    private val password: EditText by lazy { findViewById(R.id.password) }
    private val imgLogin: ImageView by lazy { findViewById(R.id.imgLogin) }

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)   //设置隐藏状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  //初始化状态栏字体为黑色

        userName.setText("wudewei")
        password.setText("wudewei12138")
        imgPwdInv.setOnClickListener {
            if (imgPwdInv.alpha == 1f) {  //若此时密码不可见
                imgPwdInv.alpha = 0f
                imgPwdVis.alpha = 1f
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()  //设置密码可见
            } else {
                imgPwdInv.alpha = 1f
                imgPwdVis.alpha = 0f
                password.transformationMethod = PasswordTransformationMethod.getInstance()  //设置密码不可见
            }
        }
        imgLogin.setOnClickListener {
            if (userName.text.isEmpty()) {
                Toast.makeText(this, "请输入用户名~", Toast.LENGTH_SHORT).show()
            } else if (password.text.isEmpty()) {
                Toast.makeText(this, "请输入密码~", Toast.LENGTH_SHORT).show()
            } else {
                db = dbHelper.writableDatabase  //获取数据库引用
                val cursor = db!!.query("user", null, null, null, null, null, null, null)
                if (cursor.moveToFirst()) {
                    do {
                        val dbUserName = cursor.getString(cursor.getColumnIndex("userName"))
                        val dbPassword = cursor.getString(cursor.getColumnIndex("password"))
                        if (userName.text.toString() == dbUserName && password.text.toString() == dbPassword) {
                            Toast.makeText(this, "登录成功~", Toast.LENGTH_SHORT).show()
                            val intent = Intent()
                            intent.putExtra("loginResult", true)
                            intent.putExtra("userName", dbUserName)
                            setResult(666, intent)
                            finish()
                        } else {
                            Toast.makeText(this, "用户名或密码错误~", Toast.LENGTH_SHORT).show()
                        }
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_none, R.anim.slide_out_bottom)
    }

}