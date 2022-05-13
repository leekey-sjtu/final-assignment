package com.bytedance.sjtu.news

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sjtu.R
import com.bytedance.sjtu.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//News2DetailActivity的comment列表
class NewsDetailAdapter(private val context: Context, private val commentList: MutableList<CommentBean.Comments>
): RecyclerView.Adapter<NewsDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_news_2_comment_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {  //绑定数据
        holder.comment.text = commentList[position].comment
        holder.postTime.text = commentList[position].createdAt.toString()
        holder.avatar.setImageResource(R.drawable.ic_avatar_1)
        holder.deleteComment.setOnClickListener {  //监听删除评论的点击事件
            deleteComment(commentList[position].title, commentList[position]._id)
        }
        holder.itemView.setOnLongClickListener {  //长按
            Log.d("wdw", "长按, comment $position")
            deleteComment(commentList[position].title, commentList[position]._id)
            return@setOnLongClickListener true  //这里必须要返回Boolean类型
        }
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val comment: TextView = itemView.findViewById(R.id.comment)
        val postTime: TextView = itemView.findViewById(R.id.postTime)
        val avatar: ImageView = itemView.findViewById(R.id.avatar)
        val deleteComment: TextView = itemView.findViewById(R.id.delete_comment)
    }

    private fun deleteComment(title: String, _id: String) {
        //创建对话框
        AlertDialog.Builder(context).apply {
            setTitle("确定要删除该评论吗？")  //设置标题
            setNegativeButton("取消") { p0, p1 ->
            }
            setPositiveButton("确认删除") { p0, p1 ->
                getRetrofit().create(NewsService::class.java)
                    .deleteComment("121110910068", title, _id)
                    .enqueue(object : Callback<deleteCommentBean> {
                        override fun onResponse(call: Call<deleteCommentBean>, response: Response<deleteCommentBean>, ) {
                            Log.d("wdw", "deleteComment success\n" +
                                    "title = $title\n" +
                                    "_id = $_id\n" +
                                    "response.success = ${response.body()?.success}\n" +
                                    "response.message = ${response.body()?.message}")
                            Toast.makeText(context, "删除评论成功~，请刷新页面~", Toast.LENGTH_LONG).show()
                        }
                        override fun onFailure(call: Call<deleteCommentBean>, t: Throwable) {
                            Log.d("wdw", "deleteComment failed -> $t")
                        }
                    })
            }
            setCancelable(true)  //设置是否可以按返回键取消对话框
            show()  //显示此Dialog
        }
    }

}