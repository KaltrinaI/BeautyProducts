package com.example.enchanted.Service;
import com.example.enchanted.Pojo.Category;
import com.example.enchanted.Pojo.Customer;
import com.example.enchanted.Pojo.Product;
import java.util.List;

public interface AdminService {
    List<Product> findAll();
    Product findProductById(Integer id);
    List<Product> findProductByCategory(Category category);
    List<Product> findProductByColor (String color);
    List<Product> findProductByPrice (double price);
    List<Product> findProductByType (String type);
    List<Product> findProductByCategoryAndColor(Category category, String color);
    void delete (Integer id);
    Product create(String name, Category category, String type, String color, double price, Integer availableQuantity);
    Product edit(Integer id, String name, double price, Integer availableQuantity);
    List<Product> outOfStockProducts();
    List<Customer> findAllCustomers();
    Customer findCustomerById(Integer id);
    Customer findCustomerByCartId(Integer id);


}
