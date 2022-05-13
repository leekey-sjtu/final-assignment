package com.bytedance.sjtu.me

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.bytedance.sjtu.R
import java.io.ByteArrayOutputStream


class SQLiteHelper(private val context: Context, name: String, version: Int): SQLiteOpenHelper(context, name, null, version) {

    private val createUser = "create table user(" +
            "id integer primary key autoincrement," +
            "userName text," +
            "password text," +
            "avatar BLOB," +
            "gender text," +
            "birthday text," +
            "location text," +
            "school text," +
            "introduction text)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createUser)
        initDatabase(db)
    }

    private fun initDatabase(db: SQLiteDatabase?) {
        val outputStream = ByteArrayOutputStream()  //头像二进制流
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_avatar)  //获取默认头像的bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)  //压缩头像文件

        val values = ContentValues().apply {
            put("userName", "wudewei")
            put("password", "wudewei12138")
            put("avatar", outputStream.toByteArray())
            put("gender", "男")
            put("birthday", "1998-01-01")
            put("location", "武汉市·洪山区")
            put("school", "华中科技大学")
            put("introduction", "Impossible is nothing.")
        }
        db?.insert("user", null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        if (oldVersion <= 1) {
//            db?.execSQL(createTaskList_3)
//        }
//        if (oldVersion <= 2) {
//            db?.execSQL(createTaskList_4)
//        }
    }
}