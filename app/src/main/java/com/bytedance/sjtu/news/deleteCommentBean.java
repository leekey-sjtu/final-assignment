package com.bytedance.sjtu.news;

public class deleteCommentBean {
    private String message;
    private boolean success;

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getSuccess() {
        return success;
    }
}