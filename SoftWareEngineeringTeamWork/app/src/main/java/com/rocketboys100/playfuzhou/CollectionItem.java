package com.rocketboys100.playfuzhou;

import java.util.ArrayList;

public class CollectionItem {

    CollectionItem(String shopName, String picUrl){
        this.shopName = shopName;
        this.picUrl = picUrl;
    }

//    CollectionItem(String shopName, String picUrl, ArrayList<Comment> comments){
//        this.shopName = shopName;
//        this.picUrl = picUrl;
//        this.comments = (ArrayList<Comment>)(comments.clone());
//    }

    public String getShopName() {
        return shopName;
    }
    public String getPicUrl(){
        return picUrl;
    }
//    public ArrayList<Comment> getComments(){
//        return comments;
//    }

    private String shopName;
    private String picUrl;
//    private ArrayList<Comment> comments = new ArrayList<Comment>();
}
