package com.app.Fulwari.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Avik on 27-08-2017.
 */

public class MyCart {

    public List<String> total_arr;
    @SerializedName("CartData")
    @Expose
    public List<CartDatum> cartData = null;
    @SerializedName("PriceData")
    @Expose
    public PriceData priceData;
    @SerializedName("Ack")
    @Expose
    public String ack;
    @SerializedName("msg")
    @Expose
    public String msg;

    public List<CartDatum> getCartData() {
        return cartData;
    }

    public void setCartData(List<CartDatum> cartData) {
        this.cartData = cartData;
    }

    public PriceData getPriceData() {
        return priceData;
    }

    public void setPriceData(PriceData priceData) {
        this.priceData = priceData;
    }

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

    public class PriceData {

        @SerializedName("total_price")
        @Expose
        public String totalPrice;
        @SerializedName("total_quantity")
        @Expose
        public String total_quantity;
        @SerializedName("service_tax_percentage")
        @Expose
        public String service_tax_percentage;
        @SerializedName("delivery_charge")
        @Expose
        public String delivery_charge;
        @SerializedName("grand_total")
        @Expose
        public String grand_total;

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getTotal_quantity() {
            return total_quantity;
        }

        public void setTotal_quantity(String total_quantity) {
            this.total_quantity = total_quantity;
        }

        public String getService_tax_percentage() {
            return service_tax_percentage;
        }

        public void setService_tax_percentage(String service_tax_percentage) {
            this.service_tax_percentage = service_tax_percentage;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public String getGrand_total() {
            return grand_total;
        }

        public void setGrand_total(String grand_total) {
            this.grand_total = grand_total;
        }
    }

    public class CartDatum {
        @SerializedName("cart_id")
        @Expose
        public String cartId;
        @SerializedName("product_photo")
        @Expose
        public String productPhoto;
        @SerializedName("product_name")
        @Expose
        public String productName;
        @SerializedName("packet_size")
        @Expose
        public String packetSize;
        @SerializedName("quantity")
        @Expose
        public String quantity;
        @SerializedName("discount")
        @Expose
        public String discount;
        @SerializedName("unit_price")
        @Expose
        public String unitPrice;
        @SerializedName("subtotal")
        @Expose
        public String subtotal;
        @SerializedName("original_price")
        @Expose
        public String original_price;

        public String getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(String original_price) {
            this.original_price = original_price;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getProductPhoto() {
            return productPhoto;
        }

        public void setProductPhoto(String productPhoto) {
            this.productPhoto = productPhoto;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getPacketSize() {
            return packetSize;
        }

        public void setPacketSize(String packetSize) {
            this.packetSize = packetSize;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }

    }


}
