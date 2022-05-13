package com.bytedance.sjtu.video;

import java.util.Date;
import java.util.List;

public class VideoBean {

    private List<Feeds> feeds;
    private boolean success;

    public void setFeeds(List<Feeds> feeds) {
        this.feeds = feeds;
    }
    public List<Feeds> getFeeds() {
        return feeds;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getSuccess() {
        return success;
    }

    public static class Feeds {

        private String _id;
        private String student_id;
        private String user_name;
        private String extra_value;
        private String video_url;
        private String image_url;
        private int image_w;
        private int image_h;
        private Date createdAt;
        private Date updatedAt;

        public void set_id(String _id) {
            this._id = _id;
        }
        public String get_id() {
            return _id;
        }

        public void setStudent_id(String student_id) {
            this.student_id = student_id;
        }
        public String getStudent_id() {
            return student_id;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }
        public String getUser_name() {
            return user_name;
        }

        public void setExtra_value(String extra_value) {
            this.extra_value = extra_value;
        }
        public String getExtra_value() {
            return extra_value;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }
        public String getVideo_url() {
            return video_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
        public String getImage_url() {
            return image_url;
        }

        public void setImage_w(int image_w) {
            this.image_w = image_w;
        }
        public int getImage_w() {
            return image_w;
        }

        public void setImage_h(int image_h) {
            this.image_h = image_h;
        }
        public int getImage_h() {
            return image_h;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
        public Date getCreatedAt() {
            return createdAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }
        public Date getUpdatedAt() {
            return updatedAt;
        }

    }

}