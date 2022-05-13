package com.bytedance.sjtu.me

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.style.UpdateLayout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.sjtu.R
import java.io.ByteArrayInputStream
import java.util.*

class MeFragment : Fragment() {

    private val imgMore: ImageView by lazy { requireView().findViewById(R.id.imgMore) }
    private val imgAvatar: ImageView by lazy { requireView().findViewById(R.id.imgAvatar) }
    private val tvUserName: TextView by lazy { requireView().findViewById(R.id.userName) }
    private val introduction: TextView by lazy { requireView().findViewById(R.id.introduction) }
    private val imgGender: ImageView by lazy { requireView().findViewById(R.id.imgGender) }
    private val age: TextView by lazy { requireView().findViewById(R.id.age) }
    private val location: TextView by lazy { requireView().findViewById(R.id.location) }
    private val school: TextView by lazy { requireView().findViewById(R.id.school) }
    private var dbHelper: SQLiteHelper? = null  //数据库Helper
    private var db: SQLiteDatabase? = null  //声明数据库db

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dbHelper = SQLiteHelper(context, "user.db", 1)  //activity与之attach后再实例化数据库Helper, 否则报错
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_me, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInfo()
        imgMore.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingActivity::class.java))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initInfo() {
        val preferences = requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)  //获取Preferences
        val userName = preferences.getString("userName", "")  //获取登录用户名
        db = dbHelper?.writableDatabase  //获取数据库引用
        val cursor = db!!.query("user", null, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val dbUserName = cursor.getString(cursor.getColumnIndex("userName"))
                if (userName == dbUserName) {  //根据登录用户名, 取出对应用户信息
                    tvUserName.text = userName
                    introduction.text = cursor.getString(cursor.getColumnIndex("introduction"))
                    age.text = getAge(cursor.getString(cursor.getColumnIndex("birthday")))
                    location.text = cursor.getString(cursor.getColumnIndex("location"))
                    school.text = cursor.getString(cursor.getColumnIndex("school"))
                    if (cursor.getString(cursor.getColumnIndex("gender")) == "男") {
                        imgGender.setImageResource(R.drawable.ic_male)
                    } else {
                        imgGender.setImageResource(R.drawable.ic_female)
                    }
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

    private fun getAge(birthday: String): String {
        val age = (Calendar.getInstance().get(Calendar.YEAR) - birthday.substring(0, 4).toInt()).toString()
        val constellation: String
        val date = birthday.substring(5, 7) + birthday.substring(8, 10)
        if ("0120" <= date && date <= "0218") {
            constellation = "水瓶座"
        } else if ("0219" <= date && date <= "0320") {
            constellation = "双鱼座"
        } else if ("0321" <= date && date <= "0419") {
            constellation = "白羊座"
        } else if ("0420" <= date && date <= "0520") {
            constellation = "金牛座"
        } else if ("0521" <= date && date <= "0621") {
            constellation = "双子座"
        } else if ("0622" <= date && date <= "0722") {
            constellation = "巨蟹座"
        } else if ("0723" <= date && date <= "0822") {
            constellation = "狮子座"
        } else if ("0823" <= date && date <= "0922") {
            constellation = "处女座"
        } else if ("0923" <= date && date <= "1023") {
            constellation = "天枰座"
        } else if ("1024" <= date && date <= "1122") {
            constellation = "天蝎座"
        } else if ("1123" <= date && date <= "1221") {
            constellation = "射手座"
        } else if ("1222" <= date && date <= "1231" || "0101" <= date && date <= "0119") {
            constellation = "魔羯座"
        } else {
            constellation = ""
        }
        return age + "岁·" + constellation
    }

    override fun onResume() {
        super.onResume()
        initInfo()  //重新进入fragment需要重新加载个人信息
    }

}