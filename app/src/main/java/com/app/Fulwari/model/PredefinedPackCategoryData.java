package com.app.Fulwari.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PredefinedPackCategoryData {
    @SerializedName("Ack")
    @Expose
    private int ack;
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("PredefinedPackCategoryData")
    @Expose
    private List<PredefinedPackList> predefinedPackCategoryDataList;

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

    public List<PredefinedPackList> getPredefinedPackCategoryDataList() {
        return predefinedPackCategoryDataList;
    }

    public void setPredefinedPackCategoryDataList(List<PredefinedPackList> predefinedPackCategoryDataList) {
        this.predefinedPackCategoryDataList = predefinedPackCategoryDataList;
    }


    public class PredefinedPackList{
        @SerializedName("category_id")
        @Expose
        private String category_id;
        @SerializedName("category_name")
        @Expose
        private String category_name;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        private boolean isSelected;

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


