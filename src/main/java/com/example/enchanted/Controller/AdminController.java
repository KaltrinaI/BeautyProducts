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
     *List of all products that are saved on database
     * @return List of products
     */
    @GetMapping("/admin/findProducts")
    public List<Product> getAll() {
        return adminService.findAll();
    }

    /**
     * Finding a specific Product by its ID
     * @param id
     * @return Product by the specified ID
     */
    @GetMapping("/admin/productById/{id}")

    Product findProductById(@PathVariable Integer id){
        return adminService.findProductById(id);

    }

    /**
     * Finding products that belong to the same category
     * @param category
     * @return List of products by the specified category
     */

    @GetMapping("/admin/productByCategory/{category}")
    List<Product> findProductByCategory(@PathVariable Category category){
        return adminService.findProductByCategory(category);
    }

    /**
     * Finding products that have the same color
     * @param color
     * @return List of products by the specified color
     */
    @GetMapping("/admin/productByColor/{color}")
    List<Product> findProductByColor (@PathVariable String color){
        return adminService.findProductByColor(color);

    }

    /**
     *Finding products that have the same price
     * @param price
     * @return List of products by the specified price
     */
    @GetMapping("/admin/productByPrice/{price}")
    List<Product> findProductByPrice (@PathVariable double price){
        return adminService.findProductByPrice(price);

    }

    /**
     *Finding products that have the same type
     * @param type
     * @return List of products with the same type
     */
    @GetMapping("/admin/productByType/{type}")
    List<Product> findProductByType (@PathVariable String type){
        return adminService.findProductByType(type);

    }

    /**
     *Finding products by the category that they belong and by the color of the product
     * @param category
     * @param color
     * @return List of products that belong to the same category and same color
     */

    @GetMapping("/admin/product/{category}/{color}")
    public List<Product> findProductByCategoryAndColor(@PathVariable Category category, @PathVariable String color){
        return adminService.findProductByCategoryAndColor(category,color);

    }

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
    @PutMapping("/editProduct/{id}")
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

    @GetMapping("/admin/outOfStock")
    public List<Product> outOfStockProducts(){
        return adminService.outOfStockProducts();

    }

    /**
     *Finding Customers that are registered in the database
     * @return List of Customers
     */

    @GetMapping("/admin/customers")
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
