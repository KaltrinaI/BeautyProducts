package com.example.enchanted.Pojo;

import javax.persistence.*;

@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="name")
    private String name;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(name="type")
    private String type;
    @Column(name="color")
    private String color;
    @Column(name="price")
    private double price;
    @Column(name="availableQuantity")
    private Integer availableQuantity;


    public Product() {
    }




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Product(Integer id, String name, Category category, String type, String color, double price, Integer availableQuantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.type = type;
        this.color = color;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }
}
