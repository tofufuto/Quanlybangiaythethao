package com.pack1.quanlybangiaythethao;

public class ShoppingCart {
    private int userId;
    private int productId;
    private int cartId;


    public ShoppingCart(int userId, int productId, int cartId) {
        this.userId = userId;
        this.productId = productId;
        this.cartId = cartId;
    }
    public ShoppingCart(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
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

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
