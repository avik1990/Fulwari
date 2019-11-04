package com.app.mbazzar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddToCart {
    
    @SerializedName("Ack")
    @Expose
    public String ack;
    @SerializedName("msg")
    @Expose
    public String msg;

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}