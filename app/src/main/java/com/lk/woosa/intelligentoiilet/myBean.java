package com.lk.woosa.intelligentoiilet;

public class myBean {
    private String text;
    private int ImageID;


    public myBean(String text,int imageID){
        this.ImageID = imageID;
        this.text = text;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageID() {
        return ImageID;
    }

    public void setImageID(int imageID) {
        ImageID = imageID;
    }
}
