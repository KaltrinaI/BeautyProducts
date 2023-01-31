package com.example.enchanted.Repository;

import com.example.enchanted.Pojo.Category;
import com.example.enchanted.Pojo.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query(
            "SELECT p FROM Product p "
    )
    List<Product> findAll();

    @Query(
            "SELECT p FROM Product p where p.id= :id"
    )
    Product findProductById(@Param("id") Integer id);

    @Query(
            "SELECT p FROM Product p where p.category= :category"
    )
    List<Product> findProductByCategory(@Param("category") Category category);

    @Query(
            "SELECT p FROM Product p where p.color= :color"
    )
    List<Product> findProductByColor (@Param("color") String color);


    @Query(
            "SELECT p FROM Product p where p.price= :price"
    )
    List<Product> findProductByPrice (@Param("price") double price);

    @Query(
            "SELECT p FROM Product p where p.type= :type"
    )
    List<Product> findProductByType (@Param("type") String type);

    @Query(
            "SELECT p FROM Product p where p.category= :category AND  p.color= :color"
    )
    List<Product> findProductByCategoryAndColor(@Param("category") Category category, @Param("color") String color);


    @Query(
            "SELECT p FROM Product p where p.availableQuantity=0"
    )
    List<Product> outOfStock();




}

