package com.example.enchanted.Repository;

import com.example.enchanted.Pojo.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface CartRepository extends CrudRepository<Cart, Integer> {
    @Query(
            "SELECT c FROM Cart c where c.id= :id"
    )
    Cart findCartById(@Param("id") Integer id);

}
