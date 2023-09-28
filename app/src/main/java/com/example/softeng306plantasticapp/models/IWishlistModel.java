package com.example.softeng306plantasticapp.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.example.softeng306plantasticapp.entities.IItem;

public interface IWishlistModel {
    public CompletableFuture<ArrayList<IItem>> getWishlistItems(Context context);
    public void removeWishlistItem(String itemId);
    public void addToCart(String itemId);
    public void clearWishlist();
}
