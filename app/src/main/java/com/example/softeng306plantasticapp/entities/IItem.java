package com.example.softeng306plantasticapp.entities;

import com.google.firebase.firestore.DocumentReference;

public interface IItem {
    public int getListImage();
    public int getBackgroundImage();
    public String getId();
    public String getName();
    public String getScientificName();
    public DocumentReference getCategory();
    public String getDescription();
    public double getPrice();
    public int getQuantity();
    public void setQuantity(int i);
    public void incrementQuantity();
}
