package com.example.softeng306plantasticapp.models;

import android.content.Context;

import java.util.concurrent.CompletableFuture;

import com.example.softeng306plantasticapp.entities.IItem;

public interface IDetailsModel {
    public CompletableFuture<IItem> retrieveItem(Context context);
    public void addToCart(int quantity);
    public CompletableFuture<Boolean> inWishlist();
    public void addToWishlist();
    public void removeFromWishlist();
    public IItem getItem();
    public Boolean getInWishlist();
    public void addToViewHistory();

}
