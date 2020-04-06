package com.app.Fulwari.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlowerProductBean {

    @SerializedName("CustomPackData")
    @Expose
    private List<ProductDatum> productData = null;
    @SerializedName("Ack")
    @Expose
    private Integer ack;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<ProductDatum> getProductData() {
        return productData;
    }

    public void setProductData(List<ProductDatum> productData) {
        this.productData = productData;
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

    public class ProductDatum {

        @SerializedName("product_id")
        @Expose
        private String productId;

        @SerializedName("product_price")
        @Expose
        private String product_price;


        @SerializedName("product_name_english")
        @Expose
        private String productNameEnglish;
        @SerializedName("product_name_bengali")
        @Expose
        private String productNameBengali;
        @SerializedName("product_details")
        @Expose
        private String productDetails;
        /*  @SerializedName("brand")
          @Expose
          private String brand;*/
        @SerializedName("product_photo")
        @Expose
        private String productPhoto;
        @SerializedName("Packets")
        @Expose
        private List<Packet> packets = null;

        private boolean isClicked = false;
        private int selectedPos = -1;

//        public String getBrand() {
//            return brand;
//        }
//
//        public void setBrand(String brand) {
//            this.brand = brand;
//        }

        public boolean isClicked() {
            return isClicked;
        }

        public void setClicked(boolean clicked) {
            isClicked = clicked;
        }

        public int getSelectedPos() {
            return selectedPos;
        }

        public void setSelectedPos(int selectedPos) {
            this.selectedPos = selectedPos;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductNameEnglish() {
            return productNameEnglish;
        }

        public void setProductNameEnglish(String productNameEnglish) {
            this.productNameEnglish = productNameEnglish;
        }

        public String getProductNameBengali() {
            return productNameBengali;
        }

        public void setProductNameBengali(String productNameBengali) {
            this.productNameBengali = productNameBengali;
        }

        public String getProductDetails() {
            return productDetails;
        }

        public void setProductDetails(String productDetails) {
            this.productDetails = productDetails;
        }

        public String getProductPhoto() {
            return productPhoto;
        }

        public void setProductPhoto(String productPhoto) {
            this.productPhoto = productPhoto;
        }

        public List<Packet> getPackets() {
            return packets;
        }

        public void setPackets(List<Packet> packets) {
            this.packets = packets;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }
    }
    public class Packet {
        @SerializedName("pack_id")
        @Expose
        private String pack_id;

        @SerializedName("product_id")
        @Expose
        private String productId;

        @SerializedName("flower_name")
        @Expose
        private String flower_name;

        @SerializedName("sunday")
        @Expose
        private String sunday;

        @SerializedName("monday")
        @Expose
        private String monday;
        @SerializedName("tuesday")
        @Expose
        private String tuesday;
        @SerializedName("wednesday")
        @Expose
        private String wednesday;
        @SerializedName("thursday")
        @Expose
        private String thursday;
        @SerializedName("friday")
        @Expose
        private String friday;
        @SerializedName("saturday")
        @Expose
        private String saturday;


        public String getPack_id() {
            return pack_id;
        }

        public void setPack_id(String pack_id) {
            this.pack_id = pack_id;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getFlower_name() {
            return flower_name;
        }

        public void setFlower_name(String flower_name) {
            this.flower_name = flower_name;
        }

        public String getSunday() {
            return sunday;
        }

        public void setSunday(String sunday) {
            this.sunday = sunday;
        }

        public String getMonday() {
            return monday;
        }

        public void setMonday(String monday) {
            this.monday = monday;
        }

        public String getTuesday() {
            return tuesday;
        }

        public void setTuesday(String tuesday) {
            this.tuesday = tuesday;
        }

        public String getWednesday() {
            return wednesday;
        }

        public void setWednesday(String wednesday) {
            this.wednesday = wednesday;
        }

        public String getThursday() {
            return thursday;
        }

        public void setThursday(String thursday) {
            this.thursday = thursday;
        }

        public String getFriday() {
            return friday;
        }

        public void setFriday(String friday) {
            this.friday = friday;
        }

        public String getSaturday() {
            return saturday;
        }

        public void setSaturday(String saturday) {
            this.saturday = saturday;
        }
    }
}
