package com.example.softeng306plantasticapp.entities;

import com.google.firebase.firestore.DocumentReference;

public class CactiAndSucculentPlant extends Item {
    public CactiAndSucculentPlant(String id, String name, String scientificName, DocumentReference category, double price, int listImage, String description, int backgroundImage) {
        super(id, name, scientificName, category, price, listImage, description, backgroundImage);
    }
}
