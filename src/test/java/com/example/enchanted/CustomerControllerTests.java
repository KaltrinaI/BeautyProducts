package com.example.enchanted;

import com.example.enchanted.Controller.CustomerController;
import com.example.enchanted.Pojo.Category;
import com.example.enchanted.Pojo.Product;
import com.example.enchanted.Service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
public class CustomerControllerTests {

    @InjectMocks
    private CustomerController sut; // system under test


    @Mock
    private CustomerService customerService;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAll_ReturnsAllProducts(){

        //arrange
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product(1,"test", Category.FACE, "type", "red", 210.0,21));
        allProducts.add(new Product(2,"test", Category.EYES, "type", "green", 120.0,21));
        allProducts.add(new Product(3,"test", Category.LIPS, "type", "blue", 300.0,21));

        Mockito.when(customerService.findAll()).thenReturn(allProducts);

        //act
        sut = new CustomerController(customerService);
        List<Product> result = sut.getAll();

        //assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(),allProducts.size());
    }


    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    public void findProductById_ReturnsProductWithSameId(int number){

        //arrange
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product(1,"test", Category.FACE, "type", "red", 210.0,21));
        allProducts.add(new Product(2,"test", Category.EYES, "type", "green", 120.0,21));
        allProducts.add(new Product(3,"test", Category.LIPS, "type", "blue", 300.0,21));

        Mockito.when(customerService.findProductById(any())).thenReturn( allProducts.stream().filter(product -> product.getId()==number).collect(Collectors.toList()).get(0));

        //act
        sut = new CustomerController(customerService);
        Product result = sut.findProductById(number);

        //assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result,allProducts.get(number-1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"red","green","blue","brown"})
    public void findProductByColor_ReturnsProductWithSameColor(String color){

        //arrange
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product(1,"test", Category.FACE, "type", "red", 210.0,21));
        allProducts.add(new Product(2,"test", Category.EYES, "type", "green", 120.0,21));
        allProducts.add(new Product(3,"test", Category.LIPS, "type", "blue", 300.0,21));
        allProducts.add(new Product(4,"test", Category.TOOLS, "type", "brown", 300.0,21));

        Mockito.when(customerService.findProductByColor(anyString())).thenReturn(allProducts.stream().filter(product -> product.getColor().equals(color)).collect(Collectors.toList()));

        //act
        sut = new CustomerController(customerService);
        List<Product> result = sut.findProductByColor(color);

        //assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(),1);
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    public void findProductByCategory_ReturnsProductWithSameCategory(Category category){

        //arrange
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product(1,"test", Category.FACE, "type", "red", 210.0,21));
        allProducts.add(new Product(2,"test", Category.EYES, "type", "green", 120.0,21));
        allProducts.add(new Product(3,"test", Category.LIPS, "type", "blue", 300.0,21));
        allProducts.add(new Product(4,"test", Category.TOOLS, "type", "blue", 300.0,21));

        Mockito.when(customerService.findProductByCategory(any())).thenReturn(allProducts.stream().filter(product -> product.getCategory()==category).collect(Collectors.toList()));

        //act
        sut = new CustomerController(customerService);
        List<Product> result = sut.findProductByCategory(category);

        //assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(),1);
    }

    @ParameterizedTest
    @ValueSource(doubles = {300.0})
    public void findProductByPrice_ReturnsProductWithSamePrice(Double price){

        //arrange
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product(1,"test", Category.FACE, "type", "red", 210.0,21));
        allProducts.add(new Product(2,"test", Category.EYES, "type", "green", 120.0,21));
        allProducts.add(new Product(3,"test", Category.LIPS, "type", "blue", 300.0,21));
        allProducts.add(new Product(4,"test", Category.TOOLS, "type", "brown", 300.0,21));

        Mockito.when(customerService.findProductByPrice(anyDouble())).thenReturn(allProducts.stream().filter(product -> product.getPrice()==price).collect(Collectors.toList()));

        //act
        sut = new CustomerController(customerService);
        List<Product> result = sut.findProductByPrice(price);

        //assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(),2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"eyeliner","lipliner","blush"})
    public void findProductByType_ReturnsProductWithSameType(String type){

        //arrange
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product(1,"test", Category.FACE, "type", "red", 210.0,21));
        allProducts.add(new Product(2,"test", Category.EYES, "type", "green", 120.0,21));
        allProducts.add(new Product(3,"test", Category.LIPS, "type", "blue", 300.0,21));
        allProducts.add(new Product(4,"test", Category.TOOLS, "type", "brown", 300.0,21));

        Mockito.when(customerService.findProductByType(anyString())).thenReturn(allProducts.stream().filter(product -> product.getType().equals(type)).collect(Collectors.toList()));

        //act
        sut = new CustomerController(customerService);
        List<Product> result = sut.findProductByType(type);

        //assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(),0);
    }

    @ParameterizedTest
    @MethodSource("categoryAndColor")
    public void findProductByCategoryAndColor_ReturnsProductWithSameCategoryAndColor(Category category,String color){

        //arrange
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product(1,"test", Category.FACE, "type", "red", 210.0,21));
        allProducts.add(new Product(2,"test", Category.EYES, "type", "green", 120.0,21));
        allProducts.add(new Product(3,"test", Category.LIPS, "type", "blue", 300.0,21));
        allProducts.add(new Product(4,"test", Category.TOOLS, "type", "brown", 300.0,21));

        Mockito.when(customerService.findProductByCategoryAndColor(any(),anyString())).thenReturn(allProducts.stream().filter(product -> product.getColor().equals(color) && product.getCategory().equals(category) ).collect(Collectors.toList()));

        //act
        sut = new CustomerController(customerService);
        List<Product> result = sut.findProductByCategoryAndColor(category,color);

        //assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(),1);
    }

    private static Stream<Arguments> categoryAndColor() {
        return Stream.of(
                arguments(Category.FACE, "red")
        );
    }






}
