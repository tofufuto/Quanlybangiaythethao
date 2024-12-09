package com.pack1.models;

import java.util.Date;

public class Review {

    private int reviewId;
    private String review;
    private float rating;
    private int userId;
    private int productId;
    private Date reviewDate;


    public Review(String review, float rating, int productId, int userId) {
        this.review = review;
        this.rating = rating;
        this.productId = productId;
        this.userId = userId;
        this.reviewDate = new Date();
    }
    public Review(String review, float rating, int productId, int userId,int reviewId) {
        this.review = review;
        this.rating = rating;
        this.productId = productId;
        this.userId = userId;
        this.reviewId = reviewId;
        this.reviewDate = new Date();
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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
}
