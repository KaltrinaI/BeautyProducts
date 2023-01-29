package com.example.enchanted.Pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany(mappedBy = "product")
    private List<ProductOrder> productOrders;

    @Column(name = "totalPrice")
    private double price;
    public Cart() {

    }


    public Cart(List<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(List<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }

    public double totalPrice(){
        return productOrders.stream().mapToDouble(order->order.totalPricePerProduct()).reduce(0.0,(acc,order)-> acc+order);
    }

    public double getPrice() {
        return totalPrice();
    }

    public void setPrice(double price) {
        this.price = totalPrice();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", productOrders=" + productOrders +
                '}';
    }
}


