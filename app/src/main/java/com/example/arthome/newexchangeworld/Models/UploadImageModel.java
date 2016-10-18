package com.example.arthome.newexchangeworld.Models;

/**
 * Created by arthome on 2016/10/12.
 */

public class UploadImageModel {
    int filesize =0;
    String filename = "";
    String base64;
    String filetype="jpg";

    public UploadImageModel(String base64){
        this.base64 = base64;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

}
