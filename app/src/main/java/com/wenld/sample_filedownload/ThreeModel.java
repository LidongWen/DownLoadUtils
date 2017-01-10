package com.wenld.sample_filedownload;


import com.wenld.downloadutils.bean.FileInfo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/30.
 */

public class ThreeModel extends FileInfo implements Serializable {

    /**
     * id : 文件id
     * file_name : 文件名称
     * file_desc : 文件描述
     * category_id : 类别ID
     * file_url : 文件下载地址
     * file_img:
     * file_author : 文件作者
     * create_time : 创建时间
     * flag : 标记0正常，1删除
     * order_no : 排序号
     */

    private String id;
    private String file_name;
    private String file_desc;
    private String category_id;
    private String file_url;
    private String file_author;
    private String create_time;
    private String flag;
    private String order_no;
    private String file_img;

    public String getFile_img() {
        return file_img;
    }

    public void setFile_img(String file_img) {
        this.file_img = file_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_desc() {
        return file_desc;
    }

    public void setFile_desc(String file_desc) {
        this.file_desc = file_desc;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getFile_author() {
        return file_author;
    }

    public void setFile_author(String file_author) {
        this.file_author = file_author;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
}
