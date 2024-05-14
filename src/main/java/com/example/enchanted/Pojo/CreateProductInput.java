package com.example.enchanted.Pojo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreateProductInput {
    @NotBlank(message = "Product name cannot be empty.")
    private String name;
    @NotNull(message = "Category cannot be null.")
    private Category category;
    @NotBlank(message = "Type cannot be empty.")
    private String type;
    @NotBlank(message = "Color cannot be empty.")
    private String color;
    @Positive(message = "Price must be greater than zero.")
    private double price;
    @Min(value = 0, message = "Available quantity cannot be negative.")
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
