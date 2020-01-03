package com.app.Fulwari.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PredefinedPackCategoryData {
    @SerializedName("Ack")
    @Expose
    private Integer ack;
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("PredefinedPackCategoryData")
    @Expose
    private List<PredefinedPackList> predefinedPackCategoryDataList;




    public class PredefinedPackList{
        @SerializedName("category_id")
        @Expose
        private String category_id;
        @SerializedName("category_name")
        @Expose
        private String category_name;

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }
    }
}


