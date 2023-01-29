package com.example.enchanted.Controller;

import com.example.enchanted.Pojo.*;
import com.example.enchanted.Service.CustomerService;
import com.example.enchanted.Service.InsufficientAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    /**
     *Finding products that are saved on the database
     * @return List of products
     */
    @GetMapping("/products")
    public List<Product> getAll() {
        return customerService.findAll();
    }


    /**
     * Finding a specific Product by its ID
     * @param id
     * @return Product by the specified ID
     */
    @GetMapping("/productById/{id}")
    public Product findProductById(@PathVariable Integer id){
        return customerService.findProductById(id);

    }

    /**
     * Finding products that belong to the same category
     * @param category
     * @return List of products by the specified category
     */

    @GetMapping("/productByCategory/{category}")
    public List<Product> findProductByCategory(@PathVariable Category category){
        return customerService.findProductByCategory(category);
    }

    /**
     * Finding products that have the same color
     * @param color
     * @return List of products by the specified color
     */

    @GetMapping("/productByColor/{color}")
    public List<Product> findProductByColor (@PathVariable String color){
        return customerService.findProductByColor(color);

    }

    /**
     *Finding products that have the same price
     * @param price
     * @return List of products by the specified price
     */
    @GetMapping("/productByPrice/{price}")
    public List<Product> findProductByPrice (@PathVariable double price){
        return customerService.findProductByPrice(price);

    }

    /**
     *Finding products that have the same type
     * @param type
     * @return List of products with the same type
     */

    @GetMapping("/productByType/{type}")
    public List<Product> findProductByType (@PathVariable String type){
        return customerService.findProductByType(type);

    }

    /**
     *Finding products by the category that they belong and by the color of the product
     * @param category
     * @param color
     * @return List of products that belong to the same category and same color
     */

    @GetMapping("/product/{category}/{color}")
    public List<Product> findProductByCategoryAndColor(@PathVariable Category category, @PathVariable String color){
        return customerService.findProductByCategoryAndColor(category,color);

    }

    /**
     * Saving a product to Cart by specifying the products' id, the cart id and the amount of product that you want to save
     * @param input
     * @throws InsufficientAmountException
     */

    @PostMapping("/addProductToCart")
    public void  addProductToCart(@RequestBody CreateOrderInput input){
        Integer productId=input.getProductId();
        Integer cartId=input.getCartId();
        Integer amount= input.getAmount();
        Product product = customerService.findProductById(productId);


        try {
            customerService.createOrder(cartId,product,amount);

        } catch (InsufficientAmountException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *Registering a customer to the database
     * @param input
     * @return Registered Customer
     */
    @PostMapping("/register")
    public Customer registerCustomer(@RequestBody CreateCustomerInput input){
        String customerName= input.getName();
        String email=input.getEmail();
        String phoneNumber=input.getPhoneNumber();
        String address=input.getAddress();

        return customerService.registerCustomer(customerName,email,phoneNumber,address);
    }

    /**
     *Editing a products amount that was added to the Cart by specifying its new amount
     * @param cartId
     * @param productId
     * @param editAmount
     * @return Product Order with the edited amount of the Product added in Cart
     */
    @PutMapping("/editAmount/{cartId}/{productId}")
    public ProductOrder editAmount(@PathVariable Integer cartId, @PathVariable Integer productId,
                                   @RequestBody EditCartProductAmount editAmount){
        Integer newAmount = editAmount.getAmount();

        return customerService.editAmount(cartId,productId,newAmount);
    }

    /**
     * Delete a product from Cart
     * @param cartId
     * @param productId
     */
    @DeleteMapping("/deleteProductFromCart/{cartId}/{productId}")
    public void deleteProductFromCart (@PathVariable Integer cartId, @PathVariable Integer productId){
        customerService.deleteProductFromCart(cartId,productId);
    }

    /**
     *List of all products(name of the product, type, category,color and price) that were previously added to a Cart
     * @param cartId
     * @return list of products that are added to cart
     */
    @GetMapping("/productsInCart/{cartId}")
    public List<ProductDto> viewCart(@PathVariable Integer cartId) {
        return customerService.viewProductsInCart(cartId);
    }

    /**
     *Calculating the total price of the products that are added to the cart (amount of the product * price)
     * @param cartId
     * @return total price of products saved on the cart
     */
    @GetMapping("/totalPrice/{cartId}")
    public Double totalPrice(@PathVariable Integer cartId) {
        return customerService.totalPrice(cartId);
    }




}
