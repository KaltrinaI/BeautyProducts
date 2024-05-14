package com.example.enchanted.Controller;

import com.example.enchanted.Pojo.*;
import com.example.enchanted.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
public class AdminController {
    @Autowired
    AdminService adminService;
    /**
     * Deleting a product from the database by its ID
     *
     * @param id
     */
    @DeleteMapping("/admin/deleteProduct/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        adminService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Adding a product to the database by specifying its name, category, type, color, price and available quantity
     *
     * @param input
     * @return Created Product
     */
    @PostMapping("/admin/createProduct")
    public ResponseEntity<Product> create(@Valid @RequestBody CreateProductInput input) {
        Product createdProduct = adminService.create(input.getName(), input.getCategory(), input.getType(), input.getColor(), input.getPrice(), input.getAvailableQuantity());
        return new ResponseEntity<>(createdProduct, HttpStatus.OK);
    }

    /**
     * Changing the name, price and the quantity of a product
     *
     * @param id
     * @param input
     * @return Product with edited fields
     */
    @PutMapping("/admin/editProduct/{id}")
    public ResponseEntity<Product> edit(@PathVariable Integer id, @Valid @RequestBody EditProductInput input) {
        Product updatedProduct = adminService.edit(id, input.getName(), input.getPrice(), input.getAvailableQuantity());
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    /**
     * Finding products that are out of Stock
     *
     * @return Products whose available quantity is equal to 0
     */

    @GetMapping("/outOfStock")
    public ResponseEntity<List<Product>> outOfStockProducts() {
        List<Product> products = adminService.outOfStockProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Finding Customers that are registered in the database
     *
     * @return List of Customers
     */

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> findAllCustomers() {
        List<Customer> customers = adminService.findAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Finding a Customer by its ID
     *
     * @param id
     * @return Customer by ID
     */

    @GetMapping("/findCustomerById/{id}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable Integer id) {
        Customer customer = adminService.findCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    /**
     * Finding a customer by its Cart ID
     *
     * @param id
     * @return Customer by its cart ID
     */

    @GetMapping("/findCustomerByCartId/{id}")
    public ResponseEntity<Customer> findCustomerByCartId(@PathVariable Integer id) {
        Customer customer = adminService.findCustomerByCartId(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(EntityNotFoundException ex) {
        return ex.getMessage();
    }

}