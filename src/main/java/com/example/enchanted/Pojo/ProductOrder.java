package com.example.enchanted.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="productOrders")
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @OneToOne
    private Product product;

    @ManyToOne
    private Cart cart;

    private Integer amount;

    public ProductOrder() {
    }

    public ProductOrder(Product product, Integer amount) {
        this.product = product;
        this.amount = amount;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public double totalPricePerProduct(){
        return product.getPrice()*amount;
    }

    @JsonIgnore
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
