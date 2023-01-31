package com.example.enchanted.Controller;

import com.example.enchanted.Pojo.*;
import com.example.enchanted.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {
    @Autowired
    AdminService adminService;

    /**
     * Deleting a product from the database by its ID
     * @param id
     */
    @DeleteMapping("/admin/deleteProduct/{id}")
    public void delete (@PathVariable Integer id){
        adminService.delete(id);
    }

    /**
     * Adding a product to the database by specifying its name, category, type, color, price and available quantity
     * @param input
     * @return Created Product
     */
    @PostMapping("/admin/createProduct")
    public Product create(@RequestBody CreateProductInput input){
        String newProductName= input.getName();
        Category newProductCategory=input.getCategory();
        String newProductType=input.getType();
        String newProductColor=input.getColor();
        double newProductPrice=input.getPrice();
        Integer newProductStock=input.getAvailableQuantity();

        return adminService.create(newProductName,newProductCategory,newProductType,newProductColor,newProductPrice,newProductStock);
    }

    /**
     * Changing the name, price and the quantity of a product
     * @param id
     * @param input
     * @return Product with edited fields
     */
    @PutMapping("/admin/editProduct/{id}")
    public Product edit(@PathVariable Integer id,
                        @RequestBody EditProductInput input){
        String newName = input.getName();
        double newPrice = input.getPrice();
        Integer newQuantity = input.getAvailableQuantity();
        return adminService.edit(id,newName,newPrice,newQuantity);
    }

    /**
     *Finding products that are out of Stock
     * @return Products whose available quantity is equal to 0
     */

    @GetMapping("/outOfStock")
    public List<Product> outOfStockProducts(){
        return adminService.outOfStockProducts();

    }

    /**
     *Finding Customers that are registered in the database
     * @return List of Customers
     */

    @GetMapping("/customers")
    public List<Customer> findAllCustomers() {
        return adminService.findAllCustomers();
    }

    /**
     * Finding a Customer by its ID
     * @param id
     * @return Customer by ID
     */

    @GetMapping("/findCustomerById/{id}")
    public Customer findCustomerById(@PathVariable Integer id){
        return adminService.findCustomerById(id);
    }

    /**
     *Finding a customer by its Cart ID
     * @param id
     * @return Customer by its cart ID
     */

    @GetMapping("/findCustomerByCartId/{id}")
    public Customer findCustomerByCartId(@PathVariable Integer id){
        return adminService.findCustomerByCartId(id);
    }



}
