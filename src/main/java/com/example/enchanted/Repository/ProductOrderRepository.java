package com.example.enchanted.Repository;

import com.example.enchanted.Pojo.ProductOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderRepository extends CrudRepository<ProductOrder,Integer> {

    @Query(
            "SELECT p FROM ProductOrder p where p.cart.id= :cartId AND p.product.id= :productId"
    )
    ProductOrder findProductByCartIdAndProductId(@Param("cartId") Integer cartId, @Param("productId") Integer productId);

    @Query(
            "SELECT p FROM ProductOrder p where p.cart.id= :cartId"
    )
    List<ProductOrder> findProductByCartId(Integer cartId);

    @Query(
            "SELECT p FROM ProductOrder p where p.cart.id= :cartId  "
    )
    List<ProductOrder> ProductsInCart(@Param("cartId") Integer cartId);
}
