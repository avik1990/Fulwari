package com.app.Fulwari.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notes {

    @SerializedName("DeliveryData")
    @Expose
    public DeliveryData deliveryData;

    public DeliveryData getDeliveryData() {
        return deliveryData;
    }

    public void setDeliveryData(DeliveryData deliveryData) {
        this.deliveryData = deliveryData;
    }

    public class DeliveryData {

        @SerializedName("content_id")
        @Expose
        public String contentId;
        @SerializedName("content_title")
        @Expose
        public String contentTitle;
        @SerializedName("content")
        @Expose
        public String content;
        @SerializedName("Ack")
        @Expose
        public Integer ack;
        @SerializedName("msg")
        @Expose
        public String msg;

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }

        public String getContentTitle() {
            return contentTitle;
        }

        public void setContentTitle(String contentTitle) {
            this.contentTitle = contentTitle;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getAck() {
            return ack;
        }

        public void setAck(Integer ack) {
            this.ack = ack;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }

}
