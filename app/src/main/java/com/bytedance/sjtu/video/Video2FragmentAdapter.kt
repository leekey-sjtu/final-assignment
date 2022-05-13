package com.bytedance.sjtu.video

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bytedance.sjtu.R

class Video2FragmentAdapter(
    private val context: Context,
    private val handler: Handler,
    private val videoList: MutableList<VideoBean.Feeds>
): RecyclerView.Adapter<Video2FragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_video_2_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {  //绑定数据
        val id = videoList[position]._id
        val studentId = videoList[position].student_id
        val userName = videoList[position].user_name
        val extraValue = videoList[position].extra_value
        val videoUrl = videoList[position].video_url
        val imageUrl = videoList[position].image_url
        val imageH = videoList[position].image_h
        val imageW = videoList[position].image_w
        val createdAt = videoList[position].createdAt
        val updatedAt = videoList[position].updatedAt
        val video = holder.videoView
        val videoCover = holder.videoCover
        val layPlayPause = holder.layPlayPause

        holder.userName.text = userName
        holder.extraValue.text = extraValue
        Glide.with(context).load(imageUrl).into(videoCover)  //加载视频封面
        video.setVideoURI(Uri.parse(videoUrl))  //设置视频Url
        video.keepScreenOn = true  //屏幕常亮
        video.setOnPreparedListener {  //视频加载完成
            holder.progressBar.visibility = View.GONE
            layPlayPause.visibility = View.VISIBLE
        }
        video.setOnCompletionListener {  //视频播放完毕
            layPlayPause.visibility = View.VISIBLE  //显现暂停按钮
            pauseAnimator(holder)  //暂停按钮动画
        }
        video.setOnClickListener {
            if (layPlayPause.visibility == View.VISIBLE) {
                layPlayPause.visibility = View.GONE
            } else {
                layPlayPause.visibility = View.VISIBLE
            }
        }
        layPlayPause.setOnClickListener {
            if (video.isPlaying) {
                video.pause()
                pauseAnimator(holder)  //暂停按钮动画
            } else {
                videoCover.visibility = View.GONE  //开始播放时，隐藏视频封面
                video.start()
                playAnimator(holder)  //播放按钮动画
                handler.postDelayed({
                    layPlayPause.visibility = View.GONE
                }, 2000)
            }
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    private fun playAnimator(holder: ViewHolder) {
        val imgPlay = holder.imgPlay
        val imgPause = holder.imgPause
        val animator1 = ObjectAnimator.ofFloat(imgPlay, "rotation", 0f, -90f)
        val animator2 = ObjectAnimator.ofFloat(imgPlay, "alpha", 1f, 0f)
        val animator3 = ObjectAnimator.ofFloat(imgPause, "rotation", 90f, 0f)
        val animator4 = ObjectAnimator.ofFloat(imgPause, "alpha", 0f, 1f)
        val animSet = AnimatorSet()
        animSet.duration = 300
        animSet.play(animator1).with(animator2).with(animator3).with(animator4)
        animSet.start()
    }

    private fun pauseAnimator(holder: ViewHolder) {
        val imgPlay = holder.imgPlay
        val imgPause = holder.imgPause
        val animator1 = ObjectAnimator.ofFloat(imgPlay, "rotation", -90f, 0f)
        val animator2 = ObjectAnimator.ofFloat(imgPlay, "alpha", 0f, 1f)
        val animator3 = ObjectAnimator.ofFloat(imgPause, "rotation", 0f, 90f)
        val animator4 = ObjectAnimator.ofFloat(imgPause, "alpha", 1f, 0f)
        val animSet = AnimatorSet()
        animSet.duration = 300
        animSet.play(animator1).with(animator2).with(animator3).with(animator4)
        animSet.start()
    }

//    private fun showPlayController(holder: ViewHolder) {
//        val playController = holder.layPlayPause
//        val animator1 = ObjectAnimator.ofFloat(playController, "translationY", 60f, 0f)
//        val animator2 = ObjectAnimator.ofFloat(playController, "alpha", 0f, 1f)
//        val animSet = AnimatorSet()
//        animSet.duration = 300
//        animSet.play(animator1).with(animator2)
//        animSet.start()
//    }
//
//    private fun hidePlayController(holder: ViewHolder) {
//        val playController = holder.layPlayPause
//        val animator1 = ObjectAnimator.ofFloat(playController, "translationY", 0f, 60f)
//        val animator2 = ObjectAnimator.ofFloat(playController, "alpha", 1f, 0f)
//        val animSet = AnimatorSet()
//        animSet.duration = 300
//        animSet.play(animator1).with(animator2)
//        animSet.start()
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val follow: TextView = itemView.findViewById(R.id.imgFollow)
        val extraValue: TextView = itemView.findViewById(R.id.extra_value)
        val videoView: VideoView = itemView.findViewById(R.id.videoView)
        val videoCover: ImageView = itemView.findViewById(R.id.videoCover)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val layPlayPause: FrameLayout = itemView.findViewById(R.id.lay_play_pause)
        val imgPlay: ImageView = itemView.findViewById(R.id.imgPlay)
        val imgPause: ImageView = itemView.findViewById(R.id.imgPause)
        val like: ImageView = itemView.findViewById(R.id.imgLike)
        val likeNum: TextView = itemView.findViewById(R.id.like_num)
        val comment: ImageView = itemView.findViewById(R.id.comment)
        val commentNum: TextView = itemView.findViewById(R.id.comment_num)
        val share: ImageView = itemView.findViewById(R.id.share)
        val shareNum: TextView = itemView.findViewById(R.id.share_num)
    }
}