package com.bytedance.sjtu.news;

import java.util.List;

public class NewsBean {

    private List<News> news;
    private boolean success;
    public void setNews(List<News> news) {
        this.news = news;
    }
    public List<News> getNews() {
        return news;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getSuccess() {
        return success;
    }

    public static class News {

        public News(String path, String image, String title, String passtime) {
            this.path = path;
            this.image = image;
            this.title = title;
            this.passtime = passtime;
        }

        private String path;
        private String image;
        private String title;
        private String passtime;
        public void setPath(String path) {
            this.path = path;
        }
        public String getPath() {
            return path;
        }

        public void setImage(String image) {
            this.image = image;
        }
        public String getImage() {
            return image;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }
        public String getPasstime() {
            return passtime;
        }

    }

}