package com.example.enchanted;

import com.example.enchanted.Pojo.Category;
import com.example.enchanted.Pojo.Product;
import com.example.enchanted.Repository.ProductRepository;
import com.example.enchanted.Service.DefaultCustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;


@SpringBootTest
public class CustomerServiceTests {

    @InjectMocks
    private DefaultCustomerService sut; //system under test

    @Mock
    private ProductRepository productRepository;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void getAll_ReturnsAllProducts(){

        //arrange
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product(1,"rare",Category.FACE,"foundation","beige",580.0,24));
        allProducts.add(new Product(2,"kylie",Category.LIPS,"lipLiner","red",250.0,29));
        allProducts.add(new Product(3,"maybelline",Category.EYES,"mascara","blue",670.0,28));
        allProducts.add(new Product(4,"dior",Category.LIPS,"lipOil","violet",2500.0,18));

        Mockito.when(productRepository.findAll()).thenReturn(allProducts);

        //act
        sut = new DefaultCustomerService(productRepository);
        List<Product> result = sut.findAll();

        //assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(),allProducts.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    public void findProductById_ReturnsProductWithSameId(int number){

        //arrange
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product(1,"rare",Category.FACE,"foundation","beige",580.0,24));
        allProducts.add(new Product(2,"kylie",Category.LIPS,"lipLiner","red",250.0,29));
        allProducts.add(new Product(3,"maybelline",Category.EYES,"mascara","blue",670.0,28));

        Mockito.when(productRepository.findProductById(anyInt())).thenReturn( allProducts.stream().filter(product -> product.getId()==number).collect(Collectors.toList()).get(0));

        //act
        sut = new DefaultCustomerService(productRepository);
        Product result = sut.findProductById(number);

        //assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result,allProducts.get(number-1));
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

        Mockito.when(productRepository.findProductByColor(anyString())).thenReturn(allProducts.stream().filter(product -> product.getColor().equals(color)).collect(Collectors.toList()));

        //act
        sut = new DefaultCustomerService(productRepository);
        List<Product> result = sut.findProductByColor(color);

        //assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(),1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"eyeliner","eyeshadow","blush"})
    public void findProductByType_ReturnsProductWithSameType(String type){

        //arrange
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product(1,"rare",Category.FACE,"foundation","beige",580.0,24));
        allProducts.add(new Product(2,"kylie",Category.LIPS,"lipLiner","red",250.0,29));
        allProducts.add(new Product(3,"maybelline",Category.EYES,"mascara","blue",670.0,28));
        allProducts.add(new Product(4,"dior",Category.LIPS,"lipOil","violet",2500.0,18));

        Mockito.when(productRepository.findProductByType(anyString())).thenReturn(allProducts.stream().filter(product -> product.getType().equals(type)).collect(Collectors.toList()));

        //act
        sut = new DefaultCustomerService(productRepository);
        List<Product> result = sut.findProductByType(type);

        //assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(),0);
    }

}

