package com.example.enchanted.Pojo;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class EditProductInput {
    @NotBlank(message = "Product name cannot be empty.")
    private String name;
    @Positive(message = "Price must be greater than zero.")
    private double price;
    @Min(value = 0, message = "Available quantity cannot be negative.")
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
