package com.example.softeng306plantasticapp.entities;

import java.util.List;

public class Profile implements IProfile{

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private List<Object> viewHistoryItems;
    private List<Object> wishlistItems;
    private List<Object> shoppingCartItems;

    public Profile(){}
    public Profile(String id, String firstName, String lastName, String email, String address, List<Object> viewHistoryItems, List<Object> wishlistItems, List<Object> shoppingCartItems) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.viewHistoryItems = viewHistoryItems;
        this.wishlistItems = wishlistItems;
        this.shoppingCartItems = shoppingCartItems;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getAddress() {
        return this.address;
    }
}
