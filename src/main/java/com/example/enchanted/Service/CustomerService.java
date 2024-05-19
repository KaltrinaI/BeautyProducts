package com.example.enchanted.Service;

import com.example.enchanted.Pojo.*;


import java.util.List;

public interface CustomerService {
    List<Product> findAll();
    Product findProductById(Integer id);
    List<Product> findProductByCategory(Category category);
    List<Product> findProductByColor (String color);
    List<Product> findProductByPrice (double price);
    List<Product> findProductByType (String type);
    List<Product> findProductByCategoryAndColor(Category category, String color);
    void createOrder(Integer cartId, Product product, Integer productAmount);
    void deleteProductFromCart(Integer cartId,Integer productId);
    ProductOrder editAmount(Integer cartId, Integer productId,Integer amount);
    Customer registerCustomer(String name, String email, String phoneNumber, String address);
    List<ProductDto> viewProductsInCart(Integer cartId);
    Double totalPrice (Integer cartId);


}

