package com.example.enchanted.Service;
import com.example.enchanted.Pojo.Category;
import com.example.enchanted.Pojo.Customer;
import com.example.enchanted.Pojo.Product;
import java.util.List;

public interface AdminService {
    void delete (Integer id);
    Product create(String name, Category category, String type, String color, double price, Integer availableQuantity);
    Product edit(Integer id, String name, double price, Integer availableQuantity);
    List<Product> outOfStockProducts();
    List<Customer> findAllCustomers();
    Customer findCustomerById(Integer id);
    Customer findCustomerByCartId(Integer id);


}
