package com.example.enchanted;

import com.example.enchanted.Pojo.*;
import com.example.enchanted.Repository.CartRepository;
import com.example.enchanted.Repository.CustomerRepository;
import com.example.enchanted.Repository.ProductOrderRepository;
import com.example.enchanted.Repository.ProductRepository;
import com.example.enchanted.Service.DefaultCustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class CustomerServiceTests {

    @InjectMocks
    private DefaultCustomerService sut; //system under test

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductOrderRepository productOrderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.openMocks(this);
        sut = new DefaultCustomerService(productRepository, productOrderRepository, cartRepository, customerRepository);
    }

    @Test
    public void getAll_ReturnsAllProducts(){

        //arrange
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product(1,"rare",Category.FACE,"foundation","beige",580.0,24));
        allProducts.add(new Product(2,"kylie",Category.LIPS,"lipLiner","red",250.0,29));
        allProducts.add(new Product(3,"maybelline",Category.EYES,"mascara","blue",670.0,28));
        allProducts.add(new Product(4,"dior",Category.LIPS,"lipOil","violet",2500.0,18));

        when(productRepository.findAll()).thenReturn(allProducts);

        //act
        List<Product> result = sut.findAll();

        //assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(),allProducts.size());
        assertTrue(result.containsAll(allProducts));
    }

    @Test
    public void testFindAll_ReturnsEmptyListWhenNoProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Product> result = sut.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAll_RepositoryThrowsException() {
        // Arrange
        when(productRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sut.findAll();
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testFindProductById_SuccessfulRetrieval() {
        // Arrange
        Integer productId = 1;
        Product product = new Product(productId, "rare", Category.FACE, "foundation", "beige", 580.0, 24);

        when(productRepository.findProductById(productId)).thenReturn(product);

        // Act
        Product result = sut.findProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("rare", result.getName());
    }

    @Test
    public void testFindProductById_ProductNotFound() {
        // Arrange
        Integer productId = 1;
        when(productRepository.findProductById(productId)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            sut.findProductById(productId);
        });

        assertEquals("Product with ID " + productId + " not found.", exception.getMessage());
    }

    @Test
    public void testFindProductById_NullIdThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.findProductById(null);
        });

        assertEquals("Product ID cannot be null.", exception.getMessage());
    }

    @Test
    public void testFindProductByCategory_SuccessfulRetrieval() {
        // Arrange
        Category category = Category.LIPS;
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "lipstick", category, "lipstick", "red", 250.0, 10));
        products.add(new Product(2, "lip gloss", category, "lip gloss", "pink", 300.0, 15));

        when(productRepository.findProductByCategory(category)).thenReturn(products);

        // Act
        List<Product> result = sut.findProductByCategory(category);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(products));
    }

    @Test
    public void testFindProductByCategory_NoProductsFound() {
        // Arrange
        Category category = Category.LIPS;
        when(productRepository.findProductByCategory(category)).thenReturn(new ArrayList<>());

        // Act
        List<Product> result = sut.findProductByCategory(category);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindProductByCategory_NullCategoryThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.findProductByCategory(null);
        });

        assertEquals("Category cannot be null.", exception.getMessage());
    }


    @ParameterizedTest
    @ValueSource(strings = {"beige","red","blue","violet"})
    public void findProductByColor_ReturnsProductWithSameColor(String color){

        //arrange
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product(1,"rare",Category.FACE,"foundation","beige",580.0,24));
        allProducts.add(new Product(2,"kylie",Category.LIPS,"lipLiner","red",250.0,29));
        allProducts.add(new Product(3,"maybelline",Category.EYES,"mascara","blue",670.0,28));
        allProducts.add(new Product(4,"dior",Category.LIPS,"lipOil","violet",2500.0,18));

        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> product.getColor().equals(color))
                .collect(Collectors.toList());

        when(productRepository.findProductByColor(color)).thenReturn(filteredProducts);

        //act
        List<Product> result = sut.findProductByColor(color);

        //assert
        Assert.assertNotNull(result);
        assertEquals(filteredProducts.size(), result.size());
        for (Product product : result) {
            assertEquals(color, product.getColor());
        }
    }

    @Test
    public void testFindProductByColor_NoProductsFound() {
        // Arrange
        String color = "blue";
        when(productRepository.findProductByColor(color)).thenReturn(new ArrayList<>());

        // Act
        List<Product> result = sut.findProductByColor(color);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindProductByColor_NullColorThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.findProductByColor(null);
        });

        assertEquals("Color cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testFindProductByColor_EmptyColorThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.findProductByColor("");
        });

        assertEquals("Color cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testFindProductByColor_BlankColorThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.findProductByColor("   ");
        });

        assertEquals("Color cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testFindProductByPrice_SuccessfulRetrieval() {
        // Arrange
        double price = 299.99;
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product 1", Category.EYES, "type1", "color1", price, 10));
        products.add(new Product(2, "Product 2", Category.FACE, "type2", "color2", price, 15));

        when(productRepository.findProductByPrice(price)).thenReturn(products);

        // Act
        List<Product> result = sut.findProductByPrice(price);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(products));
    }

    @Test
    public void testFindProductByPrice_NoProductsFound() {
        // Arrange
        double price = 299.99;
        when(productRepository.findProductByPrice(price)).thenReturn(new ArrayList<>());

        // Act
        List<Product> result = sut.findProductByPrice(price);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -100.0, -0.01})
    public void testFindProductByPrice_NegativePriceThrowsException(double price) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.findProductByPrice(price);
        });

        assertEquals("Price cannot be negative.", exception.getMessage());
    }
    @Test
    public void testFindProductByType_SuccessfulRetrieval() {
        // Arrange
        String type = "foundation";
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product 1", Category.FACE, type, "beige", 580.0, 10));
        products.add(new Product(2, "Product 2", Category.FACE, type, "light", 620.0, 15));

        when(productRepository.findProductByType(type)).thenReturn(products);

        // Act
        List<Product> result = sut.findProductByType(type);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(products));
    }

    @Test
    public void testFindProductByType_NoProductsFound() {
        // Arrange
        String type = "blush";
        when(productRepository.findProductByType(type)).thenReturn(new ArrayList<>());

        // Act
        List<Product> result = sut.findProductByType(type);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "  ", "\t", "\n" })
    public void testFindProductByType_EmptyTypeThrowsException(String type) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.findProductByType(type);
        });

        assertEquals("Type cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testFindProductByType_NullTypeThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.findProductByType(null);
        });

        assertEquals("Type cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testFindProductByCategoryAndColor_SuccessfulRetrieval() {
        // Arrange
        Category category = Category.LIPS;
        String color = "red";
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "lipstick", category, "lipstick", color, 250.0, 10));
        products.add(new Product(2, "lip gloss", category, "lip gloss", color, 300.0, 15));

        when(productRepository.findProductByCategoryAndColor(category, color)).thenReturn(products);

        // Act
        List<Product> result = sut.findProductByCategoryAndColor(category, color);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(products));
    }

    @Test
    public void testFindProductByCategoryAndColor_NoProductsFound() {
        // Arrange
        Category category = Category.LIPS;
        String color = "red";
        when(productRepository.findProductByCategoryAndColor(category, color)).thenReturn(new ArrayList<>());

        // Act
        List<Product> result = sut.findProductByCategoryAndColor(category, color);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({ "LIPS, ''", "EYES, '   '", "FACE, '\t'", "TOOLS, '\n'" })
    public void testFindProductByCategoryAndColor_EmptyColorThrowsException(Category category, String color) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.findProductByCategoryAndColor(category, color);
        });

        assertEquals("Color cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testFindProductByCategoryAndColor_NullCategoryThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.findProductByCategoryAndColor(null, "red");
        });

        assertEquals("Category cannot be null.", exception.getMessage());
    }

    @Test
    public void testFindProductByCategoryAndColor_NullColorThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.findProductByCategoryAndColor(Category.LIPS, null);
        });

        assertEquals("Color cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testCreateOrder_ProductAmountZero_ThrowsException() {
        Product product = new Product();
        product.setAvailableQuantity(10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.createOrder(1, product, 0);
        });

        assertEquals("Product amount must be positive.", exception.getMessage());
    }

    @Test
    void testCreateOrder_ProductAmountNegative_ThrowsException() {
        Product product = new Product();
        product.setAvailableQuantity(10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sut.createOrder(1, product, -1);
        });

        assertEquals("Product amount must be positive.", exception.getMessage());
    }

    @Test
    void testCreateOrder_ProductAmountGreaterThanAvailable_ThrowsException() {
        Product product = new Product();
        product.setAvailableQuantity(5);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sut.createOrder(1, product, 10);
        });

        assertEquals("Not enough product available", exception.getMessage());
    }

    //fail
    @Test
    void testCreateOrder_CartNotFound_ThrowsException() {
        Product product = new Product();
        product.setAvailableQuantity(10);

        when(cartRepository.findCartById(1)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sut.createOrder(1, product, 5);
        });

        assertEquals("Cart not found.", exception.getMessage());

        // Verify that findCartById was called with the correct parameter
        verify(cartRepository, times(1)).findCartById(1);
    }

    //fail
    @Test
    void testCreateOrder_Success() {
        Product product = new Product();
        product.setAvailableQuantity(10);

        Cart cart = new Cart();
        when(cartRepository.findCartById(1)).thenReturn(cart);

        sut.createOrder(1, product, 5);

        verify(productOrderRepository, times(1)).save(any(ProductOrder.class));
    }

    //fail
    @Test
    void testCreateOrder_ProductAmountEqualToAvailable_Success() {
        Product product = new Product();
        product.setAvailableQuantity(5);

        Cart cart = new Cart();
        when(cartRepository.findCartById(1)).thenReturn(cart);

        sut.createOrder(1, product, 5);

        verify(productOrderRepository, times(1)).save(any(ProductOrder.class));
    }

    //fail
    @Test
    void testDeleteProductFromCart_ProductOrderExists_Success() {
        Integer cartId = 1;
        Integer productId = 1;

        ProductOrder productOrder = new ProductOrder();
        when(productOrderRepository.findProductByCartIdAndProductId(cartId, productId)).thenReturn(productOrder);

        sut.deleteProductFromCart(cartId, productId);

        verify(productOrderRepository, times(1)).delete(productOrder);
    }

    //fail
    @Test
    void testEditAmount_ProductOrderFound_Success() {
        Integer cartId = 1;
        Integer productId = 1;
        Integer amount = 5;

        ProductOrder productOrder = new ProductOrder();
        productOrder.setAmount(1);
        when(productOrderRepository.findProductByCartIdAndProductId(cartId, productId)).thenReturn(productOrder);
        when(productOrderRepository.save(productOrder)).thenReturn(productOrder);

        ProductOrder updatedProductOrder = sut.editAmount(cartId, productId, amount);

        assertNotNull(updatedProductOrder);
        assertEquals(amount, updatedProductOrder.getAmount());
        verify(productOrderRepository, times(1)).save(productOrder);
    }

    //fail
    @Test
    void testEditAmount_ProductOrderNotFound_ReturnsNull() {
        Integer cartId = 1;
        Integer productId = 1;
        Integer amount = 5;

        when(productOrderRepository.findProductByCartIdAndProductId(cartId, productId)).thenReturn(null);

        ProductOrder updatedProductOrder = sut.editAmount(cartId, productId, amount);

        assertNull(updatedProductOrder);
        verify(productOrderRepository, never()).save(any(ProductOrder.class));
    }

    //fail
    @Test
    void testRegisterCustomer_Success() {
        String name = "John Doe";
        String email = "john.doe@example.com";
        String phoneNumber = "1234567890";
        String address = "123 Main St";

        Cart cart = new Cart();
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);
        customer.setCart(cart);

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer registeredCustomer = sut.registerCustomer(name, email, phoneNumber, address);

        assertEquals(name, registeredCustomer.getName());
        assertEquals(email, registeredCustomer.getEmail());
        assertEquals(phoneNumber, registeredCustomer.getPhoneNumber());
        assertEquals(address, registeredCustomer.getAddress());
        assertEquals(cart, registeredCustomer.getCart());

        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    //fail
    @Test
    void testViewProductsInCart_ProductsExist() {
        Integer cartId = 1;

        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product 2");

        ProductOrder productOrder1 = new ProductOrder();
        productOrder1.setProduct(product1);

        ProductOrder productOrder2 = new ProductOrder();
        productOrder2.setProduct(product2);

        when(productOrderRepository.ProductsInCart(cartId)).thenReturn(Arrays.asList(productOrder1, productOrder2));

        List<ProductDto> productsInCart = sut.viewProductsInCart(cartId);

        assertEquals(2, productsInCart.size());
        assertEquals("Product 1", productsInCart.get(0).getName());
        assertEquals("Product 2", productsInCart.get(1).getName());

        verify(productOrderRepository, times(1)).ProductsInCart(cartId);
    }

    //fail
    @Test
    void testViewProductsInCart_NoProducts() {
        Integer cartId = 1;

        when(productOrderRepository.ProductsInCart(cartId)).thenReturn(Collections.emptyList());

        List<ProductDto> productsInCart = sut.viewProductsInCart(cartId);

        assertEquals(0, productsInCart.size());

        verify(productOrderRepository, times(1)).ProductsInCart(cartId);
    }

    @Test
    void testTotalPrice_CartExistsWithProducts() {
        Integer cartId = 1;

        Product product1 = new Product();
        product1.setId(1);
        product1.setPrice(10.0);

        Product product2 = new Product();
        product2.setId(2);
        product2.setPrice(20.0);

        ProductOrder productOrder1 = new ProductOrder();
        productOrder1.setProduct(product1);
        productOrder1.setAmount(1);

        ProductOrder productOrder2 = new ProductOrder();
        productOrder2.setProduct(product2);
        productOrder2.setAmount(2);

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setProductOrders(Arrays.asList(productOrder1, productOrder2));

        when(cartRepository.findCartById(cartId)).thenReturn(cart);
        when(productOrderRepository.findProductByCartId(cartId)).thenReturn(Arrays.asList(productOrder1, productOrder2));

        Double totalPrice = sut.totalPrice(cartId);

        assertEquals(50.0, totalPrice);
        verify(cartRepository, times(1)).findCartById(cartId);
        verify(productOrderRepository, times(1)).findProductByCartId(cartId);
    }

    @Test
    void testTotalPrice_CartExistsNoProducts() {
        Integer cartId = 1;

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setProductOrders(Collections.emptyList());

        when(cartRepository.findCartById(cartId)).thenReturn(cart);
        when(productOrderRepository.findProductByCartId(cartId)).thenReturn(Collections.emptyList());

        Double totalPrice = sut.totalPrice(cartId);

        assertEquals(0.0, totalPrice);
        verify(cartRepository, times(1)).findCartById(cartId);
        verify(productOrderRepository, times(1)).findProductByCartId(cartId);
    }


}

