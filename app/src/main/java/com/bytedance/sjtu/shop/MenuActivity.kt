package com.bytedance.sjtu.shop

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.sjtu.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityMenuBinding

    private var introduction:String = "欢迎您进入神秘商城\n" +
            "三个随机商品折扣\n"+
            "1~3折、4~6折、7~9折\n"+
            "开盲盒选择一个吧，Good Luck!!"


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMenuBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.buttonDiscount1.setOnClickListener {
            Intent(this, SubjectActivity::class.java).apply {
                putExtra("折扣等级", (1..3).random())
                startActivity(this)
            }
        }

        binding.buttonDiscount2.setOnClickListener {
            Intent(this, SubjectActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.buttonDiscount3.setOnClickListener {
            Intent(this, SubjectActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.appIntroduction.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder
                .setTitle("详情")
                .setMessage(introduction)
                .setPositiveButton(
                    "返回",
                    object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            Log.d("TAG", "onClick:返回")
                        }
                    })
                .show()
        }


    }




}
