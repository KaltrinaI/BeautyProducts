package com.example.enchanted.Repository;


import com.example.enchanted.Pojo.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    List<Customer> findAll();

    @Query(
            "SELECT c FROM Customer c where c.id= :id"
    )
    Customer findCustomerById(@Param("id") Integer id);

    @Query(
            "SELECT c FROM Customer c where c.cart.id= :id"
    )
    Customer findCustomerByCartId(@Param("id") Integer id);




}
