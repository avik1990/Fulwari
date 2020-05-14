package com.app.Fulwari.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrders {

    @SerializedName("OrderData")
    @Expose
    private List<OrderDatum> orderData = null;
    @SerializedName("Ack")
    @Expose
    private Integer ack;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<OrderDatum> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<OrderDatum> orderData) {
        this.orderData = orderData;
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

    public class OrderDatum {

        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("total_items")
        @Expose
        private String totalItems;
        @SerializedName("total_price")
        @Expose
        private String totalPrice;
        @SerializedName("order_date")
        @Expose
        private String orderDate;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("delivery_status")
        @Expose
        private String deliveryStatus;

        @SerializedName("suggestion_posted")
        @Expose
        private String suggestion_posted;
        @SerializedName("cancel_request_sent")
        @Expose
        private String cancel_request_sent;

        public String getSuggestion_posted() {
            return suggestion_posted;
        }

        public void setSuggestion_posted(String suggestion_posted) {
            this.suggestion_posted = suggestion_posted;
        }

        public String getCancel_request_sent() {
            return cancel_request_sent;
        }

        public void setCancel_request_sent(String cancel_request_sent) {
            this.cancel_request_sent = cancel_request_sent;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(String totalItems) {
            this.totalItems = totalItems;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        public void setDeliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
        }

    }

}
