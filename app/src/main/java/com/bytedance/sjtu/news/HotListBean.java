package com.bytedance.sjtu.news;

import java.util.List;

public class HotListBean {

    private int code;
    private String msg;
    private Data data;
    private int time;
    private String log_id;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
        return this.data;
    }
    public void setTime(int time){
        this.time = time;
    }
    public int getTime(){
        return this.time;
    }
    public void setLog_id(String log_id){
        this.log_id = log_id;
    }
    public String getLog_id(){
        return this.log_id;
    }


    public static class Data {
        private String name;
        private String last_update;
        private List<News> list;

        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setLast_update(String last_update){
            this.last_update = last_update;
        }
        public String getLast_update(){
            return this.last_update;
        }
        public void setList(List<News> list){
            this.list = list;
        }
        public List<News> getList(){
            return this.list;
        }
    }


    public static class News {
        private String title;
        private String link;
        private String other;

        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setLink(String link){
            this.link = link;
        }
        public String getLink(){
            return this.link;
        }
        public void setOther(String other){
            this.other = other;
        }
        public String getOther(){
            return this.other;
        }
    }

}