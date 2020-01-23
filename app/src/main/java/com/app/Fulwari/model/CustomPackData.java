package com.app.Fulwari.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomPackData {
    @SerializedName("Ack")
    int Ack;
    @SerializedName("msg")
    String msg;
    @SerializedName("CustomPackData")
    List<CustomPackDataModel> customPackDataModels;

    public int getAck() {
        return Ack;
    }

    public void setAck(int ack) {
        Ack = ack;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CustomPackDataModel> getCustomPackDataModels() {
        return customPackDataModels;
    }

    public void setCustomPackDataModels(List<CustomPackDataModel> customPackDataModels) {
        this.customPackDataModels = customPackDataModels;
    }


    public class  CustomPackDataModel{
        @SerializedName("Packet_details")
        List<PacketDetails> packetDetails;
        @SerializedName("product_id")
        String product_id;
        @SerializedName("product_name_english")
        String product_name_english;
        @SerializedName("product_photo")
        String product_photo;
        @SerializedName("product_price")
        String product_price;
        private boolean isClicked = false;

        public List<PacketDetails> getPacketDetails() {
            return packetDetails;
        }

        public void setPacketDetails(List<PacketDetails> packetDetails) {
            this.packetDetails = packetDetails;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name_english() {
            return product_name_english;
        }

        public void setProduct_name_english(String product_name_english) {
            this.product_name_english = product_name_english;
        }

        public String getProduct_photo() {
            return product_photo;
        }

        public void setProduct_photo(String product_photo) {
            this.product_photo = product_photo;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

        public boolean isClicked() {
            return isClicked;
        }

        public void setClicked(boolean clicked) {
            isClicked = clicked;
        }
    }


    public class PacketDetails{
        @SerializedName("pack_id")
        String pack_id;
        @SerializedName("product_id")
        String product_id;
        @SerializedName("flower_name")
        String flower_name;
        @SerializedName("flower_quantity")
        String flower_quantity;

        public String getPack_id() {
            return pack_id;
        }

        public void setPack_id(String pack_id) {
            this.pack_id = pack_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getFlower_name() {
            return flower_name;
        }

        public void setFlower_name(String flower_name) {
            this.flower_name = flower_name;
        }

        public String getFlower_quantity() {
            return flower_quantity;
        }

        public void setFlower_quantity(String flower_quantity) {
            this.flower_quantity = flower_quantity;
        }
    }
}
