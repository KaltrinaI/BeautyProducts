package com.example.enchanted.Pojo;

public class ProductDto {

    private final String name;
    private final double price;
    private final String type;
    private final Category category;
    private final String color;

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public ProductDto(Product p){
        name=p.getName();
        price=p.getPrice();
        type=p.getType();
        category=p.getCategory();
        color=p.getColor();
    }
}
