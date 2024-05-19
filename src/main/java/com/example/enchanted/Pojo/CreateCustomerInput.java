package com.example.enchanted.Pojo;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class CreateCustomerInput {
    @NotEmpty(message = "Name must not be empty")
    private String name;
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @NotEmpty(message = "Phone number must not be empty")
    private String address;
    @NotEmpty(message = "Address must not be empty")
    private String phoneNumber;

    public CreateCustomerInput(){

    }

    public CreateCustomerInput(String name, String email, String address, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
