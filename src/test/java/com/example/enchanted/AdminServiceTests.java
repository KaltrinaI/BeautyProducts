package com.example.enchanted;

import com.example.enchanted.Pojo.Cart;
import com.example.enchanted.Pojo.Category;
import com.example.enchanted.Pojo.Customer;
import com.example.enchanted.Pojo.Product;
import com.example.enchanted.Repository.CustomerRepository;
import com.example.enchanted.Repository.ProductRepository;
import com.example.enchanted.Service.DefaultAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AdminServiceTests {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DefaultAdminService adminService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testEditProduct_ExistingProduct() {
        // Arrange
        Product existingProduct = new Product();
        existingProduct.setId(1);
        existingProduct.setName("Old Name");
        existingProduct.setPrice(100.0);
        existingProduct.setAvailableQuantity(10);

        when(productRepository.findProductById(1)).thenReturn(existingProduct);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Product updatedProduct = adminService.edit(1, "New Name", 150.0, 5);

        // Assert
        assertNotNull(updatedProduct, "The returned product should not be null.");
        assertEquals("New Name", updatedProduct.getName(), "The name should be updated.");
        assertEquals(150.0, updatedProduct.getPrice(), "The price should be updated.");
        assertEquals(5, updatedProduct.getAvailableQuantity(), "The available quantity should be updated.");
    }

    @Test
    public void testEditProduct_NonExistingProduct() {
        // Arrange
        when(productRepository.findProductById(1)).thenReturn(null);

        // Act
        Product result = adminService.edit(1, "New Name", 150.0, 5);

        // Assert
        assertNull(result, "The method should return null for non-existing product.");
    }
    @Test
    public void testEditProduct_NegativePriceThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.edit(1, "New Name", -100.0, 5);
        });

        assertEquals("Price must be greater than zero", exception.getMessage());
    }

    @Test
    public void testOutOfStockProducts_WhenProductsAreOutOfStock() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");
        product1.setAvailableQuantity(0);

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product 2");
        product2.setAvailableQuantity(0);

        when(productRepository.outOfStock()).thenReturn(Arrays.asList(product1, product2));

        // Act
        List<Product> outOfStockProducts = adminService.outOfStockProducts();

        // Assert
        assertNotNull(outOfStockProducts, "The list should not be null.");
        assertEquals(2, outOfStockProducts.size(), "There should be two out-of-stock products.");
        assertTrue(outOfStockProducts.contains(product1), "The list should contain product1.");
        assertTrue(outOfStockProducts.contains(product2), "The list should contain product2.");
    }

    @Test
    public void testOutOfStockProducts_WhenNoProductsAreOutOfStock() {
        // Arrange
        when(productRepository.outOfStock()).thenReturn(Arrays.asList());

        // Act
        List<Product> outOfStockProducts = adminService.outOfStockProducts();

        // Assert
        assertNotNull(outOfStockProducts, "The list should not be null.");
        assertTrue(outOfStockProducts.isEmpty(), "The list should be empty.");
    }

    @Test
    public void testFindAllCustomers_WithCustomers() {
        // Arrange
        Customer customer1 = new Customer();
        customer1.setId(1);
        Customer customer2 = new Customer();
        customer2.setId(2);
        List<Customer> customerList = Arrays.asList(customer1, customer2);
        when(customerRepository.findAll()).thenReturn(customerList);

        // Act
        List<Customer> resultList = adminService.findAllCustomers();

        // Assert
        assertNotNull(resultList, "The returned list should not be null.");
        assertEquals(2, resultList.size(), "The list should contain two customers.");
        assertTrue(resultList.containsAll(Arrays.asList(customer1, customer2)), "The list should contain the expected customers.");
    }


    @Test
    public void testFindAllCustomers_WithNoCustomers() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Customer> resultList = adminService.findAllCustomers();

        // Assert
        assertNotNull(resultList, "The returned list should not be null.");
        assertTrue(resultList.isEmpty(), "The list should be empty.");
    }

    @Test
    public void testFindCustomerById_WithFoundCustomer() {
        // Arrange
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId(1);
        expectedCustomer.setName("John Doe");

        when(customerRepository.findCustomerById(1)).thenReturn(expectedCustomer);

        // Act
        Customer actualCustomer = adminService.findCustomerById(1);

        // Assert
        assertNotNull(actualCustomer, "Customer should not be null when found.");
        assertEquals(expectedCustomer, actualCustomer, "The returned customer should match the expected one.");
    }

    @Test
    public void testFindCustomerById_WithNoCustomerFound() {
        // Arrange
        when(customerRepository.findCustomerById(1)).thenReturn(null);

        // Act
        Customer actualCustomer = adminService.findCustomerById(1);

        // Assert
        assertNull(actualCustomer, "Customer should be null when not found.");
    }

    @Test
    public void testFindCustomerByCartId_WithFoundCustomer() {
        // Arrange
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId(1);
        expectedCustomer.setCart(new Cart()); // Assuming Cart is a class linked to Customer
        expectedCustomer.getCart().setId(10); // Set cart ID for linkage
        expectedCustomer.setName("Jane Doe");

        when(customerRepository.findCustomerByCartId(10)).thenReturn(expectedCustomer);

        // Act
        Customer actualCustomer = adminService.findCustomerByCartId(10);

        // Assert
        assertNotNull(actualCustomer, "Customer should not be null when found.");
        assertEquals(expectedCustomer, actualCustomer, "The returned customer should match the expected one.");
    }

    @Test
    public void testFindCustomerByCartId_WithNoCustomerFound() {
        // Arrange
        when(customerRepository.findCustomerByCartId(10)).thenReturn(null);

        // Act
        Customer actualCustomer = adminService.findCustomerByCartId(10);

        // Assert
        assertNull(actualCustomer, "Customer should be null when not found.");
    }

    @Test
    public void testDelete_NonExistentProductThrowsException() {
        // Arrange
        Integer nonExistentProductId = 999;
        when(productRepository.existsById(nonExistentProductId)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> adminService.delete(nonExistentProductId));
        verify(productRepository, never()).deleteById(nonExistentProductId);
    }
    @Test
    public void testDelete_ProductExists_DeletesSuccessfully() {
        // Arrange
        Integer existingProductId = 1;
        when(productRepository.existsById(existingProductId)).thenReturn(true);

        // Act
        adminService.delete(existingProductId);

        // Assert
        verify(productRepository, times(1)).deleteById(existingProductId);
    }


    @Test
    public void testCreate_SuccessfulProductCreation() {
        // Arrange
        String name = "Test Product";
        Category category = Category.EYES;
        String type = "Cosmetic";
        String color = "Black";
        double price = 299.99;
        Integer availableQuantity = 100;

        Product savedProduct = new Product();
        savedProduct.setName(name);
        savedProduct.setCategory(category);
        savedProduct.setType(type);
        savedProduct.setColor(color);
        savedProduct.setPrice(price);
        savedProduct.setAvailableQuantity(availableQuantity);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = adminService.create(name, category, type, color, price, availableQuantity);

        // Assert
        assertNotNull(result, "The result should not be null.");
        assertEquals(name, result.getName(), "The product name should match the input.");
        assertEquals(category, result.getCategory(), "The product category should match the input.");
        assertEquals(type, result.getType(), "The product type should match the input.");
        assertEquals(color, result.getColor(), "The product color should match the input.");
        assertEquals(price, result.getPrice(), "The product price should match the input.");
        assertEquals(availableQuantity, result.getAvailableQuantity(), "The product available quantity should match the input.");
    }

    @Test
    public void testCreate_WithEmptyProductName_ThrowsIllegalArgumentException() {
        // Arrange
        String name = "";
        Category category = Category.EYES;
        String type = "Cosmetic";
        String color = "Black";
        double price = 299.99;
        Integer availableQuantity = 100;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.create(name, category, type, color, price, availableQuantity);
        });
        assertEquals("Product name cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreate_NegativePrice_ThrowsIllegalArgumentException() {
        // Arrange
        String name = "Test Product";
        Category category = Category.FACE;
        String type = "Cosmetic";
        String color = "Blue";
        double price = -1.0;
        Integer availableQuantity = 50;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.create(name, category, type, color, price, availableQuantity);
        });
        assertEquals("Price must be greater than zero.", exception.getMessage());
    }

    @Test
    public void testCreate_NegativeAvailableQuantity_ThrowsIllegalArgumentException() {
        // Arrange
        String name = "Test Product";
        Category category = Category.TOOLS;
        String type = "Tool";
        String color = "Green";
        double price = 150.0;
        Integer availableQuantity = -10;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.create(name, category, type, color, price, availableQuantity);
        });
        assertEquals("Available quantity cannot be negative.", exception.getMessage());
    }





















}
