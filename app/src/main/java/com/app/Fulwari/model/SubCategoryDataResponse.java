package com.app.Fulwari.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoryDataResponse {


    @SerializedName("SubCategoryData")
    @Expose
    private List<SubCategoryDatum> subCategoryData = null;
    @SerializedName("Ack")
    @Expose
    private Integer ack;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<SubCategoryDatum> getSubCategoryData() {
        return subCategoryData;
    }

    public void setSubCategoryData(List<SubCategoryDatum> subCategoryData) {
        this.subCategoryData = subCategoryData;
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

    public class SubCategoryDatum {

        @SerializedName("subcategory_id")
        @Expose
        private String subcategoryId;
        @SerializedName("subcategory_name")
        @Expose
        private String subcategoryName;
        @SerializedName("subcategory_photo")
        @Expose
        private String subcategoryPhoto;

        public String getSubcategoryId() {
            return subcategoryId;
        }

        public void setSubcategoryId(String subcategoryId) {
            this.subcategoryId = subcategoryId;
        }

        public String getSubcategoryName() {
            return subcategoryName;
        }

        public void setSubcategoryName(String subcategoryName) {
            this.subcategoryName = subcategoryName;
        }

        public String getSubcategoryPhoto() {
            return subcategoryPhoto;
        }

        public void setSubcategoryPhoto(String subcategoryPhoto) {
            this.subcategoryPhoto = subcategoryPhoto;
        }

    }
}
