package com.example.enchanted.Pojo;

public class CreateProductInput {
    private String name;
    private Category category;
    private String type;
    private String color;
    private double price;
    private Integer availableQuantity;

    public CreateProductInput(String name, Category category, String type, String color, double price, Integer availableQuantity) {
        this.name = name;
        this.category = category;
        this.type = type;
        this.color = color;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public void setAvailableQuantity(Integer stock) {
        this.availableQuantity = stock;
    }
}
