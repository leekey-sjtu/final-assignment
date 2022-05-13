package com.bytedance.sjtu.news;

import java.util.Date;
import java.util.List;

public class CommentBean {

    private List<Comments> comments;
    private boolean success;

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }
    public List<Comments> getComments() {
        return comments;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getSuccess() {
        return success;
    }

    public static class Comments {

        private String _id;
        private String title;
        private String comment;
        private Date createdAt;
        public void set_id(String _id) {
            this._id = _id;
        }
        public String get_id() {
            return _id;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
        public String getComment() {
            return comment;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
        public Date getCreatedAt() {
            return createdAt;
        }

    }

}