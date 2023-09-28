package com.example.softeng306plantasticapp.models;

import android.content.Context;

import com.example.softeng306plantasticapp.data.FirestoreDocumentCallback;
import com.example.softeng306plantasticapp.data.IItemDatabase;
import com.example.softeng306plantasticapp.data.IProfileDatabase;
import com.example.softeng306plantasticapp.data.ItemDatabase;
import com.example.softeng306plantasticapp.data.ProfileDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

import com.example.softeng306plantasticapp.entities.IItem;

public class CartModel implements ICartModel{
    private IProfileDatabase profileDB = ProfileDatabase.getSingleton();
    private String userId = new ProfileModel().getId();
    private double totalCost = 0;
    private CompletableFuture<Double> totalCostFuture = new CompletableFuture<>();
    public CompletableFuture<ArrayList<IItem>> getCartItems(Context context) {
        CompletableFuture<ArrayList<IItem>> futureItems = new CompletableFuture<>();
        ArrayList<IItem> cartItems = new ArrayList<>();

        profileDB.fetchShoppingCart(userId, new FirestoreDocumentCallback() {
            @Override
            public void onDocumentFetched(Object cartItemIds) {
                if (cartItemIds == null) {
                    futureItems.complete(cartItems);
                    totalCostFuture.complete(totalCost);
                    return;
                }

                IItemDatabase itemDB = ItemDatabase.getSingleton();
                ArrayList<String> itemIds = (ArrayList<String>) cartItemIds;
                Iterator<String> itemIdIterator = itemIds.iterator();
                processItems(context, itemIdIterator, cartItems, itemDB, futureItems);
            }

            @Override
            public void onFailure(Exception e) {
                futureItems.completeExceptionally(e);
            }
        });

        return futureItems;
    }

    private void processItems(Context context, Iterator<String> itemIdIterator, ArrayList<IItem> cartItems, IItemDatabase itemDB, CompletableFuture<ArrayList<IItem>> futureItems) {
        if (!itemIdIterator.hasNext()) {
            futureItems.complete(cartItems);
            totalCostFuture.complete(totalCost);
            return;
        }

        String itemId = itemIdIterator.next();

        boolean itemExists = false;
        for (IItem item : cartItems) {
            if (item.getId().equals(itemId)) {
                item.incrementQuantity();
                totalCost += item.getPrice();
                itemExists = true;
                break;
            }
        }

        if (itemExists) {
            // Move to the next item directly
            processItems(context, itemIdIterator, cartItems, itemDB, futureItems);
            return;
        }

        itemDB.fetchItem(context, itemId, new FirestoreDocumentCallback() {
            @Override
            public void onDocumentFetched(Object currentItem) {
                cartItems.add((IItem) currentItem);
                totalCost += ((IItem) currentItem).getPrice();
                processItems(context, itemIdIterator, cartItems, itemDB, futureItems);
            }

            @Override
            public void onFailure(Exception e) {
                futureItems.completeExceptionally(e);
            }
        });
    }

    public void removeCartItem(IItem item) {
        profileDB.deleteAllFromShoppingCart(userId, item.getId());
        totalCost = totalCost - (item.getQuantity()* item.getPrice());
    }

    public CompletableFuture<Double> getTotalCost() {
        return totalCostFuture;
    }

    public double getCachedTotalCost() { return totalCost; }

    public void decrementCartItemQuantity(IItem item) {
        profileDB.deleteFromShoppingCart(userId, item.getId());
        totalCost = totalCost - item.getPrice();
    }

    public void incrementCartItemQuantity(IItem item) {
        profileDB.addToShoppingCart(userId, item.getId());
        totalCost = totalCost + item.getPrice();
    }

    public void clearCart() {
        profileDB.clearShoppingCart(userId);
        totalCost = 0;
    }
}
