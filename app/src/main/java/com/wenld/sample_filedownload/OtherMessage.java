package com.wenld.sample_filedownload;

import java.io.Serializable;

/**
 * Created by wenld on 2017/1/3.
 */
public class OtherMessage implements Serializable {
    private int postion;
    private String id;

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
