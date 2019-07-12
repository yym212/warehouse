package com.example.guard_app.Bean;

public class listBean {

    private int ImageID;
    private String tv_1;
    private String tv_2;
    private String tv_3;


    public listBean(int imageID,String tv_1,String tv_2,String tv_3){
        this.ImageID = imageID;
        this.tv_1=tv_1;
        this.tv_2=tv_2;
        this.tv_3=tv_3;
    }

    public int getImageID() {
        return ImageID;
    }

    public void setImageID(int imageID) {
        ImageID = imageID;
    }

    public String getTv_1() {
        return tv_1;
    }

    public void setTv_1(String tv_1) {
        this.tv_1 = tv_1;
    }

    public String getTv_2() {
        return tv_2;
    }

    public void setTv_2(String tv_2) {
        this.tv_2 = tv_2;
    }

    public String getTv_3() {
        return tv_3;
    }

    public void setTv_3(String tv_3) {
        this.tv_3 = tv_3;
    }
}
