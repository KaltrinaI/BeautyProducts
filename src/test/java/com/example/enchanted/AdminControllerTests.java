package com.example.enchanted;

import com.example.enchanted.Pojo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.enchanted.Controller.AdminController;
import com.example.enchanted.Service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(AdminController.class)
public class AdminControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AdminService adminService;

    @BeforeEach
    public void setUp() {
        Mockito.reset(adminService);
    }

    @Test
    public void testDeleteProduct_SuccessfulDeletion() throws Exception {
        // Arrange
        Integer productId = 1;

        // Act & Assert
        mockMvc.perform(delete("/admin/deleteProduct/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(adminService, Mockito.times(1)).delete(productId);
    }

    @Test
    public void testDeleteProduct_NonExistentProduct() throws Exception {
        // Arrange
        Integer nonExistentProductId = 999;
        doThrow(new EntityNotFoundException("Product with ID " + nonExistentProductId + " not found."))
                .when(adminService).delete(nonExistentProductId);

        // Act & Assert
        mockMvc.perform(delete("/admin/deleteProduct/{id}", nonExistentProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(adminService, Mockito.times(1)).delete(nonExistentProductId);
    }


    @Test
    public void testCreateProduct_EmptyProductName() throws Exception {
        // Arrange
        CreateProductInput input = new CreateProductInput("", Category.EYES, "Cosmetic", "Black", 299.99, 100);

        // Act & Assert
        mockMvc.perform(post("/admin/createProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateProduct_NegativePrice() throws Exception {
        // Arrange
        CreateProductInput input = new CreateProductInput("Test Product", Category.FACE, "Cosmetic", "Blue", -1.0, 50);

        // Act & Assert
        mockMvc.perform(post("/admin/createProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateProduct_NegativeAvailableQuantity() throws Exception {
        // Arrange
        CreateProductInput input = new CreateProductInput("Test Product", Category.TOOLS, "Tool", "Green", 150.0, -10);

        // Act & Assert
        mockMvc.perform(post("/admin/createProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateProduct_SuccessfulCreation() throws Exception {
        // Arrange
        CreateProductInput input = new CreateProductInput("Test Product", Category.EYES, "Cosmetic", "Black", 299.99, 100);
        Product createdProduct = new Product(1, "Test Product", Category.EYES, "Cosmetic", "Black", 299.99, 100);

        when(adminService.create(input.getName(), input.getCategory(), input.getType(), input.getColor(), input.getPrice(), input.getAvailableQuantity()))
                .thenReturn(createdProduct);

        // Act & Assert
        mockMvc.perform(post("/admin/createProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.category").value("EYES"))
                .andExpect(jsonPath("$.type").value("Cosmetic"))
                .andExpect(jsonPath("$.color").value("Black"))
                .andExpect(jsonPath("$.price").value(299.99))
                .andExpect(jsonPath("$.availableQuantity").value(100));

        verify(adminService).create(input.getName(), input.getCategory(), input.getType(), input.getColor(), input.getPrice(), input.getAvailableQuantity());
    }

    @Test
    public void testEditProduct_SuccessfulEdit() throws Exception {
        // Arrange
        Integer productId = 1;
        EditProductInput input = new EditProductInput("Updated Product", 399.99, 50);
        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(399.99);
        updatedProduct.setAvailableQuantity(50);

        when(adminService.edit(productId, input.getName(), input.getPrice(), input.getAvailableQuantity()))
                .thenReturn(updatedProduct);

        // Act & Assert
        mockMvc.perform(put("/admin/editProduct/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(399.99))
                .andExpect(jsonPath("$.availableQuantity").value(50));

        verify(adminService).edit(productId, input.getName(), input.getPrice(), input.getAvailableQuantity());
    }

    @Test
    public void testEditProduct_ProductNotFound() throws Exception {
        // Arrange
        Integer productId = 999;
        EditProductInput input = new EditProductInput("Updated Product", 399.99, 50);

        when(adminService.edit(productId, input.getName(), input.getPrice(), input.getAvailableQuantity()))
                .thenThrow(new EntityNotFoundException("Product with ID " + productId + " not found."));

        // Act & Assert
        mockMvc.perform(put("/admin/editProduct/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());

        verify(adminService).edit(productId, input.getName(), input.getPrice(), input.getAvailableQuantity());
    }

    @Test
    public void testEditProduct_InvalidInput() throws Exception {
        // Arrange
        Integer productId = 1;
        EditProductInput input = new EditProductInput("", -100.0, -5);

        // Act & Assert
        mockMvc.perform(put("/admin/editProduct/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testOutOfStockProducts_SuccessfulRetrieval() throws Exception {
        // Arrange
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");
        product1.setCategory(Category.EYES);
        product1.setType("Type 1");
        product1.setColor("Color 1");
        product1.setPrice(100.0);
        product1.setAvailableQuantity(0);

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product 2");
        product2.setCategory(Category.FACE);
        product2.setType("Type 2");
        product2.setColor("Color 2");
        product2.setPrice(200.0);
        product2.setAvailableQuantity(0);

        List<Product> outOfStockProducts = Arrays.asList(product1, product2);

        when(adminService.outOfStockProducts()).thenReturn(outOfStockProducts);

        // Act & Assert
        mockMvc.perform(get("/outOfStock")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[0].category").value("EYES"))
                .andExpect(jsonPath("$[0].type").value("Type 1"))
                .andExpect(jsonPath("$[0].color").value("Color 1"))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[0].availableQuantity").value(0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product 2"))
                .andExpect(jsonPath("$[1].category").value("FACE"))
                .andExpect(jsonPath("$[1].type").value("Type 2"))
                .andExpect(jsonPath("$[1].color").value("Color 2"))
                .andExpect(jsonPath("$[1].price").value(200.0))
                .andExpect(jsonPath("$[1].availableQuantity").value(0));
    }

    @Test
    public void testOutOfStockProducts_EmptyList() throws Exception {
        // Arrange
        when(adminService.outOfStockProducts()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/outOfStock")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testFindAllCustomers_SuccessfulRetrieval() throws Exception {
        //Arrange
        Customer customer1 = new Customer();
        customer1.setId(1);
        customer1.setName("John Doe");
        customer1.setEmail("john.doe@example.com");
        customer1.setAddress("123 Main St");
        customer1.setPhoneNumber("123-456-7890");

        Customer customer2 = new Customer();
        customer2.setId(2);
        customer2.setName("Jane Smith");
        customer2.setEmail("jane.smith@example.com");
        customer2.setAddress("456 Elm St");
        customer2.setPhoneNumber("098-765-4321");

        List<Customer> customers = Arrays.asList(customer1, customer2);

        when(adminService.findAllCustomers()).thenReturn(customers);

        // Act & Assert
        mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].address").value("123 Main St"))
                .andExpect(jsonPath("$[0].phoneNumber").value("123-456-7890"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].email").value("jane.smith@example.com"))
                .andExpect(jsonPath("$[1].address").value("456 Elm St"))
                .andExpect(jsonPath("$[1].phoneNumber").value("098-765-4321"));
    }

    @Test
    public void testFindAllCustomers_EmptyList() throws Exception {
        // Arrange
        when(adminService.findAllCustomers()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testFindCustomerById_SuccessfulRetrieval() throws Exception {
        // Arrange
        Integer customerId = 1;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAddress("123 Main St");
        customer.setPhoneNumber("123-456-7890");

        when(adminService.findCustomerById(customerId)).thenReturn(customer);

        // Act & Assert
        mockMvc.perform(get("/findCustomerById/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.phoneNumber").value("123-456-7890"));

        verify(adminService).findCustomerById(customerId);
    }

    @Test
    public void testFindCustomerById_CustomerNotFound() throws Exception {
        // Arrange
        Integer customerId = 999;

        when(adminService.findCustomerById(customerId)).thenThrow(new EntityNotFoundException("Customer with ID " + customerId + " not found."));

        // Act & Assert
        mockMvc.perform(get("/findCustomerById/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(adminService).findCustomerById(customerId);
    }

    @Test
    public void testFindCustomerByCartId_SuccessfulRetrieval() throws Exception {
        // Arrange
        Integer cartId = 1;
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAddress("123 Main St");
        customer.setPhoneNumber("123-456-7890");

        when(adminService.findCustomerByCartId(cartId)).thenReturn(customer);

        // Act & Assert
        mockMvc.perform(get("/findCustomerByCartId/{id}", cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.phoneNumber").value("123-456-7890"));

        verify(adminService).findCustomerByCartId(cartId);
    }

    @Test
    public void testFindCustomerByCartId_CustomerNotFound() throws Exception {
        // Arrange
        Integer cartId = 999;

        when(adminService.findCustomerByCartId(cartId)).thenThrow(new EntityNotFoundException("Customer with cart ID " + cartId + " not found."));

        // Act & Assert
        mockMvc.perform(get("/findCustomerByCartId/{id}", cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(adminService).findCustomerByCartId(cartId);
    }
}


