package com.rocketboys100.playfuzhou;

public class Comment {
    Comment(String userName, String generalComment, String detailComment, String timeComment){
        this.userName = userName;
        this.generalComment = generalComment;
        this.detailComment = detailComment;
        this.timeComment = timeComment;
    }

    public String getUserName(){
        return userName;
    }
    public String getGeneralComment(){
        return generalComment;
    }
    public String getDetailComment(){
        return detailComment;
    }

    public String getTimeComment() {
        return timeComment;
    }

    private String userName;
    private String generalComment;
    private String detailComment;
    private String timeComment;
}
