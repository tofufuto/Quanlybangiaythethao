package com.pack1.quanlybangiaythethao;

import android.graphics.Bitmap;

public class ProducImage {


    private int imageId;
    private Bitmap image;
    private int productId;



    public ProducImage(Bitmap image, int productId) {
        this.image = image;
        this.productId = productId;
    }
    public ProducImage(Bitmap image, int productId,int imageId) {
        this.image = image;
        this.productId = productId;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getProductId() {
        return productId;
    }

    public void setProduct_id(int productId) {
        this.productId = productId;
    }
}
