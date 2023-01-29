package com.example.enchanted.Pojo;

public class CreateOrderInput {
    private Integer cartId;
    private Integer productId;
    private Integer amount;

    public CreateOrderInput() {
    }

    public CreateOrderInput(Integer cartId, Integer productId, Integer amount) {
        this.cartId = cartId;
        this.productId = productId;
        this.amount = amount;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
