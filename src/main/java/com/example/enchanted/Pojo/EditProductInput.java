package com.example.enchanted.Pojo;


public class EditProductInput {
    private String name;
    private double price;
    private Integer availableQuantity;

    public EditProductInput() {
    }


    public EditProductInput(String name, double price, Integer availableQuantity) {
        this.name = name;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
