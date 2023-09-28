package com.example.softeng306plantasticapp.models;

import android.content.Context;

import com.example.softeng306plantasticapp.data.FirestoreDocumentCallback;
import com.example.softeng306plantasticapp.data.IItemDatabase;
import com.example.softeng306plantasticapp.data.IProfileDatabase;
import com.example.softeng306plantasticapp.data.ItemDatabase;
import com.example.softeng306plantasticapp.data.ProfileDatabase;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.softeng306plantasticapp.entities.IItem;

public class WishlistModel implements IWishlistModel {

    IProfileDatabase profileDB = ProfileDatabase.getSingleton();
    String userId = new ProfileModel().getId();
    public CompletableFuture<ArrayList<IItem>> getWishlistItems(Context context) {
        CompletableFuture<ArrayList<IItem>> futureItems = new CompletableFuture<>();
        ArrayList<IItem> wishlistItems = new ArrayList<>();
        profileDB.fetchWishlist(userId, new FirestoreDocumentCallback() {
            @Override
            public void onDocumentFetched(Object wishlistItemIds) {
                IItemDatabase itemDB = ItemDatabase.getSingleton();
                ArrayList<String> itemIds = (ArrayList<String>) wishlistItemIds;
                AtomicInteger count = new AtomicInteger(itemIds.size());

                for (String itemId : itemIds) {
                    itemDB.fetchItem(context, itemId, new FirestoreDocumentCallback() {
                        @Override
                        public void onDocumentFetched(Object currentItem) {
                            wishlistItems.add((IItem) currentItem);
                            if (count.decrementAndGet() == 0) {
                                futureItems.complete(wishlistItems);
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            futureItems.completeExceptionally(e);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                futureItems.completeExceptionally(e);
            }
        });

        return futureItems;
    }

    public void removeWishlistItem(String itemId) {
        profileDB.deleteFromWishlist(userId, itemId);
    }

    public void addToCart(String itemId) {
        profileDB.addToShoppingCart(userId, itemId);
    }

    public void clearWishlist() {
        profileDB.clearWishlist(userId);
    }
}
