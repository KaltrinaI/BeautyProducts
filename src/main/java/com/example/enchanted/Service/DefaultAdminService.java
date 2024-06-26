package com.example.enchanted.Service;

import com.example.enchanted.Pojo.Category;
import com.example.enchanted.Pojo.Customer;
import com.example.enchanted.Pojo.Product;
import com.example.enchanted.Repository.CustomerRepository;
import com.example.enchanted.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (availableQuantity < 0) {
            throw new IllegalArgumentException("Available quantity cannot be negative.");
        }

        
        Product product = productRepository.findProductById(id);
        if (product==null){
            throw new EntityNotFoundException("Product with ID " + id + " not found.");
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
        Customer customer = customerRepository.findCustomerById(id);
        if (customer == null) {
            throw new EntityNotFoundException("Customer with ID " + id + " not found.");
        }
        return customer;
    }

    @Override
    public Customer findCustomerByCartId(Integer id) {
        Customer customer = customerRepository.findCustomerByCartId(id);
        if (customer == null) {
            throw new EntityNotFoundException("Customer with cart ID " + id + " not found.");
        }
        return customer;
    }

    @Override
    public void delete(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product with ID " + id + " not found.");
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product create(String name, Category category, String type, String color, double price, Integer availableQuantity) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (availableQuantity < 0) {
            throw new IllegalArgumentException("Available quantity cannot be negative.");
        }
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
