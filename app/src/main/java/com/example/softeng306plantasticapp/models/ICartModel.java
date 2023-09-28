package com.example.softeng306plantasticapp.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.example.softeng306plantasticapp.entities.IItem;

public interface ICartModel {
    public CompletableFuture<ArrayList<IItem>> getCartItems(Context context);
    public void removeCartItem(IItem item);
    public CompletableFuture<Double> getTotalCost();
    public double getCachedTotalCost();
    public void decrementCartItemQuantity(IItem item);
    public void incrementCartItemQuantity(IItem item);
    public void clearCart();
}
