package com.bytedance.sjtu.shop

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R
import com.bytedance.sjtu.databinding.ActivitySubjectBinding
import com.bytedance.sjtu.shop.Utils.Utils
import java.util.*

class SubjectActivity : AppCompatActivity() {

    lateinit var binding : ActivitySubjectBinding

    private var map = TreeMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)

        //完成列表页
        val data = (1..6).map{"第${Utils.format(it)}件商品"}

        val demoAdapter = DemoAdapter()
        demoAdapter.updatedData(data)

        val rv = findViewById<RecyclerView>(R.id.recycler_view)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = demoAdapter


        var findViewById = findViewById<EditText>(R.id.words_et).addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                demoAdapter.setFilter(s?.toString())
            }

        })

    }
}
