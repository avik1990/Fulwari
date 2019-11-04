package com.app.mbazzar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrdersDetailsModel {

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
        public String totalQuantity;
        @SerializedName("delivery_charge")
        @Expose
        public String deliveryCharge;
        @SerializedName("Quick Delivery")
        @Expose
        public String quickDelivery;
        @SerializedName("grand_total")
        @Expose
        public String grandTotal;

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getTotalQuantity() {
            return totalQuantity;
        }

        public void setTotalQuantity(String totalQuantity) {
            this.totalQuantity = totalQuantity;
        }

        public String getDeliveryCharge() {
            return deliveryCharge;
        }

        public void setDeliveryCharge(String deliveryCharge) {
            this.deliveryCharge = deliveryCharge;
        }

        public String getQuickDelivery() {
            return quickDelivery;
        }

        public void setQuickDelivery(String quickDelivery) {
            this.quickDelivery = quickDelivery;
        }

        public String getGrandTotal() {
            return grandTotal;
        }

        public void setGrandTotal(String grandTotal) {
            this.grandTotal = grandTotal;
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
        @SerializedName("original_price")
        @Expose
        public String originalPrice;
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

        public String getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(String originalPrice) {
            this.originalPrice = originalPrice;
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
