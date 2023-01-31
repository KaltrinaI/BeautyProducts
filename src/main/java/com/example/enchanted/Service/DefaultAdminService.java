package com.example.enchanted.Service;

import com.example.enchanted.Pojo.Category;
import com.example.enchanted.Pojo.Customer;
import com.example.enchanted.Pojo.Product;
import com.example.enchanted.Repository.CustomerRepository;
import com.example.enchanted.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultAdminService implements AdminService {
    private ProductRepository productRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public DefaultAdminService(ProductRepository productRepository, CustomerRepository customerRepository){
        this.productRepository=productRepository;
        this.customerRepository=customerRepository;
    }

    @Override
    public Product edit(Integer id, String name, double price, Integer availableQuantity) {
        Product product = productRepository.findProductById(id);
        if (product==null){
            return null;
        }
        product.setName(name);
        product.setPrice(price);
        product.setAvailableQuantity(availableQuantity);
        return productRepository.save(product);

    }

    @Override
    public List<Product> outOfStockProducts() {
        return productRepository.outOfStock();
    }

    @Override
    public List<Customer> findAllCustomers() {
        List<Customer> listOfCustomers = new ArrayList<>();
        Iterable<Customer> customers = customerRepository.findAll();
        for (Customer customer: customers){
            listOfCustomers.add(customer);
        }
        return listOfCustomers;
    }

    @Override
    public Customer findCustomerById(Integer id) {
        return customerRepository.findCustomerById(id);
    }

    @Override
    public Customer findCustomerByCartId(Integer id) {
        return customerRepository.findCustomerByCartId(id);
    }

    @Override
    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product create(String name, Category category, String type, String color, double price, Integer availableQuantity) {
        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setType(type);
        product.setColor(color);
        product.setPrice(price);
        product.setAvailableQuantity(availableQuantity);
        return productRepository.save(product);
    }
}
