package com.example.arthome.newexchangeworld;

import com.example.arthome.newexchangeworld.Models.PostModel;

/**
 * Created by arthome on 2016/10/13.
 */

public class AsyncWrapper {
    private PostModel postModel;
    private int statusCode;


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public PostModel getPostModel() {
        return postModel;
    }

    public void setPostModel(PostModel postModel) {
        this.postModel = postModel;
    }
}
