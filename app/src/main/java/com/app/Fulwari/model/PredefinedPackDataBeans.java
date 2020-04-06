package com.app.Fulwari.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PredefinedPackDataBeans {

    @SerializedName("PredefinedPackData")
    @Expose
    private List<FlowerProductBean.ProductDatum> productData = null;
    @SerializedName("Ack")
    @Expose
    private Integer ack;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<FlowerProductBean.ProductDatum> getProductData() {
        return productData;
    }

    public void setProductData(List<FlowerProductBean.ProductDatum> productData) {
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


        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }
    }
}
