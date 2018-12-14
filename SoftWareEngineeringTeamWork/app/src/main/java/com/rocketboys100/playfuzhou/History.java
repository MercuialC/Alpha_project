package com.rocketboys100.playfuzhou;

public class History {

    History(String picPath, String text) {
        this.picPath = picPath;
        this.text = text;
    }

    String getPicPath(){
        return picPath;
    }

    String getText(){
        return text;
    }


    private String picPath;
    private String text;

}
