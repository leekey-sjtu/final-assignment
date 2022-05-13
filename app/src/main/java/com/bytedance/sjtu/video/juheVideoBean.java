package com.bytedance.sjtu.video;

import java.util.Date;
import java.util.List;

public class juheVideoBean {

    private String reason;
    private List<Result> result;
    private int error_code;
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getReason() {
        return reason;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
    public List<Result> getResult() {
        return result;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
    public int getError_code() {
        return error_code;
    }

    public class Result {

        private String title;
        private String share_url;
        private String author;
        private String item_cover;
        private long hot_value;
        private Date hot_words;
        private long play_count;
        private long digg_count;
        private long comment_count;
        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }
        public String getShare_url() {
            return share_url;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
        public String getAuthor() {
            return author;
        }

        public void setItem_cover(String item_cover) {
            this.item_cover = item_cover;
        }
        public String getItem_cover() {
            return item_cover;
        }

        public void setHot_value(long hot_value) {
            this.hot_value = hot_value;
        }
        public long getHot_value() {
            return hot_value;
        }

        public void setHot_words(Date hot_words) {
            this.hot_words = hot_words;
        }
        public Date getHot_words() {
            return hot_words;
        }

        public void setPlay_count(long play_count) {
            this.play_count = play_count;
        }
        public long getPlay_count() {
            return play_count;
        }

        public void setDigg_count(long digg_count) {
            this.digg_count = digg_count;
        }
        public long getDigg_count() {
            return digg_count;
        }

        public void setComment_count(long comment_count) {
            this.comment_count = comment_count;
        }
        public long getComment_count() {
            return comment_count;
        }

    }

}