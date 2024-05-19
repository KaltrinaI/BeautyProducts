package com.example.enchanted;

import com.example.enchanted.Controller.CustomerController;
import com.example.enchanted.Pojo.*;
import com.example.enchanted.Service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetAll_Success() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(new Product(1, "Product1", null, "Type1", "Color1", 100.0, 10),
                new Product(2, "Product2", null, "Type2", "Color2", 200.0, 20));
        when(customerService.findAll()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/findProducts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(products.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[0].type").value("Type1"))
                .andExpect(jsonPath("$[0].color").value("Color1"))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[0].availableQuantity").value(10))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product2"))
                .andExpect(jsonPath("$[1].type").value("Type2"))
                .andExpect(jsonPath("$[1].color").value("Color2"))
                .andExpect(jsonPath("$[1].price").value(200.0))
                .andExpect(jsonPath("$[1].availableQuantity").value(20));

        verify(customerService, times(1)).findAll();
    }

    @Test
    public void testGetAll_NoProducts() throws Exception {
        // Arrange
        when(customerService.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/findProducts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(customerService, times(1)).findAll();
    }

    @Test
    public void testGetAll_ServiceThrowsException() throws Exception {
        // Arrange
        when(customerService.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/findProducts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(customerService, times(1)).findAll();
    }

    @Test
    public void testFindProductById_Success() throws Exception {
        // Arrange
        Product product = new Product(1, "Product1", null, "Type1", "Color1", 100.0, 10);
        when(customerService.findProductById(1)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/productById/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product1"))
                .andExpect(jsonPath("$.type").value("Type1"))
                .andExpect(jsonPath("$.color").value("Color1"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.availableQuantity").value(10));

        verify(customerService, times(1)).findProductById(1);
    }

    @Test
    public void testFindProductById_ProductNotFound() throws Exception {
        // Arrange
        when(customerService.findProductById(1)).thenThrow(new EntityNotFoundException("Product with ID 1 not found"));

        // Act & Assert
        mockMvc.perform(get("/productById/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).findProductById(1);
    }

    @Test
    public void testFindProductById_InvalidId() throws Exception {
        // Arrange
        int invalidId = -1;

        // Act & Assert
        mockMvc.perform(get("/productById/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).findProductById(invalidId);
    }

    @Test
    public void testFindProductById_ServiceThrowsException() throws Exception {
        // Arrange
        when(customerService.findProductById(1)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/productById/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(customerService, times(1)).findProductById(1);
    }

    @Test
    public void testFindProductByCategory_Success() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product(1, "Product1", Category.FACE, "Type1", "Color1", 100.0, 10),
                new Product(2, "Product2", Category.FACE, "Type2", "Color2", 200.0, 20)
        );
        when(customerService.findProductByCategory(Category.FACE)).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/productsByCategory/FACE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(products.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[0].category").value("FACE"))
                .andExpect(jsonPath("$[0].type").value("Type1"))
                .andExpect(jsonPath("$[0].color").value("Color1"))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[0].availableQuantity").value(10))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product2"))
                .andExpect(jsonPath("$[1].category").value("FACE"))
                .andExpect(jsonPath("$[1].type").value("Type2"))
                .andExpect(jsonPath("$[1].color").value("Color2"))
                .andExpect(jsonPath("$[1].price").value(200.0))
                .andExpect(jsonPath("$[1].availableQuantity").value(20));

        verify(customerService, times(1)).findProductByCategory(Category.FACE);
    }

    @Test
    public void testFindProductByCategory_NoProducts() throws Exception {
        // Arrange
        when(customerService.findProductByCategory(Category.FACE)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/productsByCategory/FACE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(customerService, times(1)).findProductByCategory(Category.FACE);
    }

    @Test
    public void testFindProductByCategory_ServiceThrowsException() throws Exception {
        // Arrange
        when(customerService.findProductByCategory(Category.FACE)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/productsByCategory/FACE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(customerService, times(1)).findProductByCategory(Category.FACE);
    }

    @Test
    public void testFindProductByColor_Success() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product(1, "Product1", null, "Type1", "Red", 100.0, 10),
                new Product(2, "Product2", null, "Type2", "Red", 200.0, 20)
        );
        when(customerService.findProductByColor("Red")).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/productsByColor/Red")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(products.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[0].color").value("Red"))
                .andExpect(jsonPath("$[0].type").value("Type1"))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[0].availableQuantity").value(10))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product2"))
                .andExpect(jsonPath("$[1].color").value("Red"))
                .andExpect(jsonPath("$[1].type").value("Type2"))
                .andExpect(jsonPath("$[1].price").value(200.0))
                .andExpect(jsonPath("$[1].availableQuantity").value(20));

        verify(customerService, times(1)).findProductByColor("Red");
    }

    @Test
    public void testFindProductByColor_NoProducts() throws Exception {
        // Arrange
        when(customerService.findProductByColor("Red")).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/productsByColor/Red")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(customerService, times(1)).findProductByColor("Red");
    }

    @Test
    public void testFindProductByColor_ServiceThrowsException() throws Exception {
        // Arrange
        when(customerService.findProductByColor("Red")).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/productsByColor/Red")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(customerService, times(1)).findProductByColor("Red");
    }

    @Test
    public void testFindProductByPrice_Success() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product(1, "Product1", null, "Type1", "Color1", 100.0, 10),
                new Product(2, "Product2", null, "Type2", "Color2", 100.0, 20)
        );
        when(customerService.findProductByPrice(100.0)).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/productsByPrice/100.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(products.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[0].type").value("Type1"))
                .andExpect(jsonPath("$[0].color").value("Color1"))
                .andExpect(jsonPath("$[0].availableQuantity").value(10))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product2"))
                .andExpect(jsonPath("$[1].price").value(100.0))
                .andExpect(jsonPath("$[1].type").value("Type2"))
                .andExpect(jsonPath("$[1].color").value("Color2"))
                .andExpect(jsonPath("$[1].availableQuantity").value(20));

        verify(customerService, times(1)).findProductByPrice(100.0);
    }

    @Test
    public void testFindProductByPrice_NoProducts() throws Exception {
        // Arrange
        when(customerService.findProductByPrice(100.0)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/productsByPrice/100.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(customerService, times(1)).findProductByPrice(100.0);
    }

    @Test
    public void testFindProductByPrice_InvalidPrice() throws Exception {
        // Arrange
        double invalidPrice = -1.0;

        // Act & Assert
        mockMvc.perform(get("/productsByPrice/{price}", invalidPrice)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Price must be a positive value."));

        verify(customerService, never()).findProductByPrice(invalidPrice);
    }

    @Test
    public void testFindProductByPrice_ServiceThrowsException() throws Exception {
        // Arrange
        when(customerService.findProductByPrice(100.0)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/productsByPrice/100.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));

        verify(customerService, times(1)).findProductByPrice(100.0);
    }

    @Test
    public void testFindProductByType_Success() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product(1, "Product1", null, "Cosmetic", "Red", 100.0, 10),
                new Product(2, "Product2", null, "Cosmetic", "Blue", 200.0, 20)
        );
        when(customerService.findProductByType("Cosmetic")).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/productsByType/Cosmetic")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(products.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[0].type").value("Cosmetic"))
                .andExpect(jsonPath("$[0].color").value("Red"))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[0].availableQuantity").value(10))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product2"))
                .andExpect(jsonPath("$[1].type").value("Cosmetic"))
                .andExpect(jsonPath("$[1].color").value("Blue"))
                .andExpect(jsonPath("$[1].price").value(200.0))
                .andExpect(jsonPath("$[1].availableQuantity").value(20));

        verify(customerService, times(1)).findProductByType("Cosmetic");
    }

    @Test
    public void testFindProductByType_NoProducts() throws Exception {
        // Arrange
        when(customerService.findProductByType("Cosmetic")).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/productsByType/Cosmetic")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(customerService, times(1)).findProductByType("Cosmetic");
    }

    @Test
    public void testFindProductByType_ServiceThrowsException() throws Exception {
        // Arrange
        when(customerService.findProductByType("Cosmetic")).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/productsByType/Cosmetic")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));

        verify(customerService, times(1)).findProductByType("Cosmetic");
    }

    @Test
    public void testFindProductByCategoryAndColor_Success() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product(1, "Product1", Category.FACE, "Type1", "Red", 100.0, 10),
                new Product(2, "Product2", Category.FACE, "Type2", "Red", 200.0, 20)
        );
        when(customerService.findProductByCategoryAndColor(Category.FACE, "Red")).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/products/FACE/Red")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(products.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[0].category").value("FACE"))
                .andExpect(jsonPath("$[0].color").value("Red"))
                .andExpect(jsonPath("$[0].type").value("Type1"))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[0].availableQuantity").value(10))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product2"))
                .andExpect(jsonPath("$[1].category").value("FACE"))
                .andExpect(jsonPath("$[1].color").value("Red"))
                .andExpect(jsonPath("$[1].type").value("Type2"))
                .andExpect(jsonPath("$[1].price").value(200.0))
                .andExpect(jsonPath("$[1].availableQuantity").value(20));

        verify(customerService, times(1)).findProductByCategoryAndColor(Category.FACE, "Red");
    }

    @Test
    public void testFindProductByCategoryAndColor_NoProducts() throws Exception {
        // Arrange
        when(customerService.findProductByCategoryAndColor(Category.FACE, "Red")).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/products/FACE/Red")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(customerService, times(1)).findProductByCategoryAndColor(Category.FACE, "Red");
    }

    @Test
    public void testFindProductByCategoryAndColor_ServiceThrowsException() throws Exception {
        // Arrange
        when(customerService.findProductByCategoryAndColor(Category.FACE, "Red")).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/products/FACE/Red")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));

        verify(customerService, times(1)).findProductByCategoryAndColor(Category.FACE, "Red");
    }

    @Test
    public void testAddProductToCart_Success() throws Exception {
        // Arrange
        CreateOrderInput input = new CreateOrderInput(1, 1, 5);
        Product product = new Product(1, "Product", null, "type", "color", 100.0, 10);
        when(customerService.findProductById(1)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(post("/addProductToCart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());

        verify(customerService, times(1)).createOrder(1, product, 5);
    }
    @Test
    public void testAddProductToCart_ProductNotFound() throws Exception {
        // Arrange
        CreateOrderInput input = new CreateOrderInput(1, 1, 5);
        when(customerService.findProductById(1)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/addProductToCart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Product not found."));

        verify(customerService, never()).createOrder(anyInt(), any(Product.class), anyInt());
    }
    @Test
    public void testAddProductToCart_InvalidInput() throws Exception {
        // Arrange
        CreateOrderInput input = new CreateOrderInput(null, 1, 5);

        // Act & Assert
        mockMvc.perform(post("/addProductToCart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).findProductById(anyInt());
        verify(customerService, never()).createOrder(anyInt(), any(), anyInt());
    }

    @Test
    public void testAddProductToCart_InsufficientAmount() throws Exception {
        // Arrange
        CreateOrderInput input = new CreateOrderInput(1, 1, 15);
        Product product = new Product(1, "Product", null, "type", "color", 100.0, 10);
        when(customerService.findProductById(1)).thenReturn(product);
        doThrow(new RuntimeException("Not enough product available.")).when(customerService).createOrder(1, product, 15);

        // Act & Assert
        mockMvc.perform(post("/addProductToCart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Not enough product available."));

        verify(customerService, times(1)).createOrder(1, product, 15);
    }

    @Test
    public void testRegisterCustomer_Success() throws Exception {
        // Arrange
        CreateCustomerInput input = new CreateCustomerInput();
        input.setName("John Doe");
        input.setEmail("john.doe@example.com");
        input.setPhoneNumber("1234567890");
        input.setAddress("123 Main St");

        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhoneNumber("1234567890");
        customer.setAddress("123 Main St");

        when(customerService.registerCustomer(anyString(), anyString(), anyString(), anyString())).thenReturn(customer);

        // Act & Assert
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.address").value("123 Main St"));

        verify(customerService, times(1)).registerCustomer("John Doe", "john.doe@example.com", "1234567890", "123 Main St");
    }
    @Test
    public void testRegisterCustomer_ServiceThrowsException() throws Exception {
        // Arrange
        CreateCustomerInput input = new CreateCustomerInput();
        input.setName("John Doe");
        input.setEmail("john.doe@example.com");
        input.setPhoneNumber("1234567890");
        input.setAddress("123 Main St");

        when(customerService.registerCustomer(anyString(), anyString(), anyString(), anyString())).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));

        verify(customerService, times(1)).registerCustomer("John Doe", "john.doe@example.com", "1234567890", "123 Main St");
    }

    @Test
    public void testEditAmount_Success() throws Exception {
        // Arrange
        Integer cartId = 1;
        Integer productId = 1;
        EditCartProductAmount editAmount = new EditCartProductAmount(5);
        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(1);
        productOrder.setAmount(5);

        when(customerService.editAmount(cartId, productId, 5)).thenReturn(productOrder);

        // Act & Assert
        mockMvc.perform(put("/editAmount/{cartId}/{productId}", cartId, productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editAmount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(5));

        verify(customerService, times(1)).editAmount(cartId, productId, 5);
    }

    @Test
    public void testEditAmount_InvalidAmount() throws Exception {
        // Arrange
        Integer cartId = 1;
        Integer productId = 1;
        EditCartProductAmount editAmount = new EditCartProductAmount(0);

        // Act & Assert
        mockMvc.perform(put("/editAmount/{cartId}/{productId}", cartId, productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editAmount)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Amount must be greater than zero."));

        verify(customerService, never()).editAmount(anyInt(), anyInt(), anyInt());
    }

    @Test
    public void testEditAmount_ServiceThrowsException() throws Exception {
        // Arrange
        Integer cartId = 1;
        Integer productId = 1;
        EditCartProductAmount editAmount = new EditCartProductAmount(5);

        when(customerService.editAmount(cartId, productId, 5)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(put("/editAmount/{cartId}/{productId}", cartId, productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editAmount)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));

        verify(customerService, times(1)).editAmount(cartId, productId, 5);
    }

    @Test
    public void testDeleteProductFromCart_Success() throws Exception {
        // Arrange
        Integer cartId = 1;
        Integer productId = 1;

        // Act & Assert
        mockMvc.perform(delete("/deleteProductFromCart/{cartId}/{productId}", cartId, productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteProductFromCart(cartId, productId);
    }

    @Test
    public void testDeleteProductFromCart_ProductOrderNotFound() throws Exception {
        // Arrange
        Integer cartId = 1;
        Integer productId = 1;
        doThrow(new IllegalArgumentException("Product order not found in cart.")).when(customerService).deleteProductFromCart(cartId, productId);

        // Act & Assert
        mockMvc.perform(delete("/deleteProductFromCart/{cartId}/{productId}", cartId, productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Product order not found in cart."));

        verify(customerService, times(1)).deleteProductFromCart(cartId, productId);
    }

    @Test
    public void testDeleteProductFromCart_ServiceThrowsException() throws Exception {
        // Arrange
        Integer cartId = 1;
        Integer productId = 1;
        doThrow(new RuntimeException("Database error")).when(customerService).deleteProductFromCart(cartId, productId);

        // Act & Assert
        mockMvc.perform(delete("/deleteProductFromCart/{cartId}/{productId}", cartId, productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));

        verify(customerService, times(1)).deleteProductFromCart(cartId, productId);
    }

    @Test
    public void testViewCart_Success() throws Exception {
        // Arrange
        Integer cartId = 1;
        ProductDto product1 = new ProductDto(new Product(1,"Product 1", Category.FACE, "Type 1", "Color 1", 100.0, 10));
        ProductDto product2 = new ProductDto(new Product(2,"Product 2", Category.EYES, "Type 2", "Color 2", 200.0, 5));
        List<ProductDto> productsInCart = Arrays.asList(product1, product2);

        when(customerService.viewProductsInCart(cartId)).thenReturn(productsInCart);

        // Act & Assert
        mockMvc.perform(get("/productsInCart/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[0].category").value("FACE"))
                .andExpect(jsonPath("$[0].type").value("Type 1"))
                .andExpect(jsonPath("$[0].color").value("Color 1"))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[1].name").value("Product 2"))
                .andExpect(jsonPath("$[1].category").value("EYES"))
                .andExpect(jsonPath("$[1].type").value("Type 2"))
                .andExpect(jsonPath("$[1].color").value("Color 2"))
                .andExpect(jsonPath("$[1].price").value(200.0));

        verify(customerService, times(1)).viewProductsInCart(cartId);
    }

    @Test
    public void testViewCart_EmptyCart() throws Exception {
        // Arrange
        Integer cartId = 1;
        when(customerService.viewProductsInCart(cartId)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/productsInCart/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(customerService, times(1)).viewProductsInCart(cartId);
    }

    @Test
    public void testViewCart_CartNotFound() throws Exception {
        // Arrange
        Integer cartId = 999;
        when(customerService.viewProductsInCart(cartId)).thenThrow(new IllegalArgumentException("Cart not found."));

        // Act & Assert
        mockMvc.perform(get("/productsInCart/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cart not found."));

        verify(customerService, times(1)).viewProductsInCart(cartId);
    }

    @Test
    public void testViewCart_ServiceThrowsException() throws Exception {
        // Arrange
        Integer cartId = 1;
        when(customerService.viewProductsInCart(cartId)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/productsInCart/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));

        verify(customerService, times(1)).viewProductsInCart(cartId);
    }

    @Test
    public void testTotalPrice_Success() throws Exception {
        // Arrange
        Integer cartId = 1;
        Double totalPrice = 300.0;

        when(customerService.totalPrice(cartId)).thenReturn(totalPrice);

        // Act & Assert
        mockMvc.perform(get("/totalPrice/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(totalPrice.toString()));

        verify(customerService, times(1)).totalPrice(cartId);
    }

    @Test
    public void testTotalPrice_CartNotFound() throws Exception {
        // Arrange
        Integer cartId = 999;
        when(customerService.totalPrice(cartId)).thenThrow(new IllegalArgumentException("Cart not found."));

        // Act & Assert
        mockMvc.perform(get("/totalPrice/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cart not found."));

        verify(customerService, times(1)).totalPrice(cartId);
    }

    @Test
    public void testTotalPrice_ServiceThrowsException() throws Exception {
        // Arrange
        Integer cartId = 1;
        when(customerService.totalPrice(cartId)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/totalPrice/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));

        verify(customerService, times(1)).totalPrice(cartId);
    }



}
