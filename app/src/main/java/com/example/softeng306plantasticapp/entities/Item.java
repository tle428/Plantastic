package com.example.softeng306plantasticapp.entities;

import com.google.firebase.firestore.DocumentReference;

public abstract class Item implements IItem{

    private String id;
    private String name;
    private String scientificName;
    private DocumentReference category;
    private String description;
    private double price;
    private int listImage;
    private int backgroundImage;
    private int quantity = 1;

    public Item(String id, String name, String scientificName, DocumentReference category, double price, int listImage, String description, int backgroundImage) {
        this.id = id;
        this.name = name;
        this.scientificName = scientificName;
        this.category = category;
        this.price = price;
        this.listImage = listImage;
        this.description = description;
        this.backgroundImage = backgroundImage;
    }
    @Override
    public int getListImage() {
        return this.listImage;
    }

    @Override
    public int getBackgroundImage() {
        return this.backgroundImage;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getScientificName() {
        return this.scientificName;
    }

    @Override
    public DocumentReference getCategory() {
        return this.category;
    }

    @Override
    public double getPrice() {
        return this.price;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int i) {
        this.quantity = i;
    }

    @Override
    public void incrementQuantity() {
        this.quantity++;
    }
}
