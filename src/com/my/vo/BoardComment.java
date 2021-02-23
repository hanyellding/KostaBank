package com.my.vo;

public class BoardComment {
    private User user;
    private String comment_wdate;
    private String comment_content;

    public BoardComment() {
    }

    public BoardComment(User user, String comment_wdate, String comment_content) {
        this.user = user;
        this.comment_wdate = comment_wdate;
        this.comment_content = comment_content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment_wdate() {
        return comment_wdate;
    }

    public void setComment_wdate(String comment_wdate) {
        this.comment_wdate = comment_wdate;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    @Override
    public String toString() {
        return "BoardComment{" +
                "user=" + user +
                ", comment_wdate='" + comment_wdate + '\'' +
                ", comment_content='" + comment_content + '\'' +
                '}';
    }
}
