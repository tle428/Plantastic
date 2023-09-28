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

public class MainModel implements IMainModel {
    private IProfileDatabase profileDatabase = ProfileDatabase.getSingleton();

    public CompletableFuture<ArrayList<IItem>> getFeaturedItems(Context context) {
        String profileId = new ProfileModel().getId();
        CompletableFuture<ArrayList<IItem>> futureItems = new CompletableFuture<>();
        ArrayList<IItem> featuredItems = new ArrayList<>();

        profileDatabase.fetchViewHistory(profileId, new FirestoreDocumentCallback() {
            @Override
            public void onDocumentFetched(Object viewHistoryitemids) {
                IItemDatabase itemDatabase = ItemDatabase.getSingleton();
                ArrayList<String> itemIds = (ArrayList<String>) viewHistoryitemids;
                AtomicInteger count = new AtomicInteger(itemIds.size());

                if (count.get() > 1) { // more than 1 item in viewhistory
                    for (String itemId : itemIds) {
                        itemDatabase.fetchItem(context, itemId, new FirestoreDocumentCallback() {
                            @Override
                            public void onDocumentFetched(Object currentItem) {
                                featuredItems.add((IItem) currentItem);
                                if (count.decrementAndGet() == 0) {
                                    futureItems.complete(featuredItems);
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                futureItems.completeExceptionally(e);
                            }
                        });
                    }
                }
                else { // less than 2 items in viewhistory
                    itemDatabase.fetchAllItems(context, new FirestoreDocumentCallback() {
                        @Override
                        public void onDocumentFetched(Object allItems) {
                            ArrayList<IItem> allItemsList = (ArrayList<IItem>) allItems;
                            // randomly choose 3 items from allItemsList
                            for (int i = 0; i < 3; i++) {
                                int randomIndex = (int) (Math.random() * allItemsList.size());
                                featuredItems.add(allItemsList.get(randomIndex));
                                allItemsList.remove(randomIndex);
                            }
                            futureItems.complete(featuredItems);
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
}
