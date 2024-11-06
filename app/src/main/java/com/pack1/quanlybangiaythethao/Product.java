package com.pack1.quanlybangiaythethao;

import android.graphics.Bitmap;

import java.util.Date;

public class Product {


    private int productId;
    private String name;
    private int quantity;
    private String size;
    private String color;
    private String gender;
    private float price;
    private String brand;
    private String description;
    private Date dateAdded;
    private float rating;
    private Bitmap pdImage;

    public Product(String name, int quantity, String size, String color, String gender, float price, String brand, String description,float rating, Bitmap pdImage) {
        this.name = name;
        this.quantity = quantity;
        this.size = size;
        this.color = color;
        this.gender = gender;
        this.price = price;
        this.brand = brand;
        this.description = description;
        this.dateAdded = new Date();
        this.pdImage = pdImage;
        this.rating = rating;
    }
    public Product(String name, int quantity, String size, String color, String gender, float price, String brand, String description,float rating ,Bitmap pdImage,int productId) {
        this.name = name;
        this.quantity = quantity;
        this.size = size;
        this.color = color;
        this.gender = gender;
        this.price = price;
        this.brand = brand;
        this.description = description;
        this.dateAdded = new Date();
        this.pdImage = pdImage;
        this.productId = productId;
        this.rating = rating;
    }

    public int getProduct_id() {
        return productId;
    }

    public void setProduct_id(int product_id) {
        this.productId = product_id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getGender() {
        return gender;
    }

    public float getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public Bitmap getPdImage() {
        return pdImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPdImage(Bitmap pdImage) {
        this.pdImage = pdImage;
    }
}
