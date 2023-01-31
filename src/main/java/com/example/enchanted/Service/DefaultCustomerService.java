package com.example.enchanted.Service;

import com.example.enchanted.Pojo.*;
import com.example.enchanted.Repository.CartRepository;
import com.example.enchanted.Repository.CustomerRepository;
import com.example.enchanted.Repository.ProductOrderRepository;
import com.example.enchanted.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultCustomerService implements CustomerService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductOrderRepository productOrderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public DefaultCustomerService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        List<Product> listOfProducts = new ArrayList<>();
        Iterable<Product> products = productRepository.findAll();
        for (Product product: products){
            listOfProducts.add(product);

        }
        return listOfProducts;
    }

    @Override
    public Product findProductById(Integer id) {
        return productRepository.findProductById(id);
    }

    @Override
    public List<Product> findProductByCategory(Category category) {
        return productRepository.findProductByCategory(category);
    }

    @Override
    public List<Product> findProductByColor(String color) {

        return productRepository.findProductByColor(color);
    }

    @Override
    public List<Product> findProductByPrice(double price) {

        return productRepository.findProductByPrice(price);
    }

    @Override
    public List<Product> findProductByType(String type) {

        return productRepository.findProductByType(type);
    }

    @Override
    public List<Product> findProductByCategoryAndColor(Category category, String color) {
        return productRepository.findProductByCategoryAndColor(category,color);
    }


    @Override
    public void createOrder( Integer cartId,Product product, Integer productAmount) throws InsufficientAmountException{
        ProductOrder productOrder = new ProductOrder(product, productAmount);
        if (productAmount>product.getAvailableQuantity()){
            throw new RuntimeException();
        }

        Cart cart= cartRepository.findCartById(cartId);
        productOrder.setCart(cart);
        productOrderRepository.save(productOrder);
    }

    @Override
    public void deleteProductFromCart(Integer cartId, Integer productId) {
        ProductOrder productOrder=productOrderRepository.findProductByCartIdAndProductId(cartId,productId);
        productOrderRepository.delete(productOrder);
    }

    @Override
    public ProductOrder editAmount(Integer cartId, Integer productId, Integer amount) {
        ProductOrder productOrder = productOrderRepository.findProductByCartIdAndProductId(cartId,productId);
        if (productOrder==null){
            return null;
        }
        productOrder.setAmount(amount);
        return productOrderRepository.save(productOrder);
    }


    @Override
    public Customer registerCustomer(String name, String email, String phoneNumber, String address) {
        Cart c=new Cart();
        Cart base = cartRepository.save(c);
        Customer customer= new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);
        customer.setCart(base);
        return customerRepository.save(customer);
    }

    @Override
    public List<ProductDto> viewProductsInCart(Integer cartId) {
       return productOrderRepository.ProductsInCart(cartId).stream().map(productOrder -> new ProductDto(productOrder.getProduct())).collect(Collectors.toList());
    }

    @Override
    public Double totalPrice(Integer cartId) {
        Cart cart =cartRepository.findCartById(cartId);
        cart.setProductOrders(productOrderRepository.findProductByCartId(cartId));
        return cart.getPrice();
    }


}

