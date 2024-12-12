package com.pack1.models;

import android.graphics.Bitmap;

import java.util.Date;

public class UserOrder {

    private int orderId;
    private int quantity;
    private float totalPrice;
    private String status;
    private Date dateOrder;
    private int userId;
    private int productId;

    private String shipAddress;
    private String productName;
    private Bitmap productImage;
    private String customerName;

    public String getShipAdress() {
        return shipAdress;
    }

    public void setShipAdress(String shipAdress) {
        this.shipAdress = shipAdress;
    }

    private String shipAdress;

    public UserOrder( int quantity, float totalPrice, String status, int productId,int userId,String shipAdress) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.userId = userId;
        this.productId = productId;
        this.dateOrder = new Date();
        this.shipAdress = shipAdress;
    } public UserOrder( int quantity, float totalPrice, String status, int productId,int userId,String shipAdress,int orderId) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.productId = productId;
        this.dateOrder = new Date();
        this.shipAdress = shipAdress;
    }
    // Constructor đã được cập nhật
    public UserOrder(int orderId, int quantity, float totalPrice, String status, Date dateOrder, int userId, int productId, String shipAddress, String productName, Bitmap productImage, String customerName) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.dateOrder = dateOrder;
        this.userId = userId;
        this.productId = productId;
        this.shipAddress = shipAddress;
        this.productName = productName;
        this.productImage = productImage;
        this.customerName = customerName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Bitmap getProductImage() {
        return productImage;
    }

    public void setProductImage(Bitmap productImage) {
        this.productImage = productImage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
