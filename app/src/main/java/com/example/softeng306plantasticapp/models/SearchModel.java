package com.example.softeng306plantasticapp.models;

import android.content.Context;

import com.example.softeng306plantasticapp.data.FirestoreDocumentCallback;
import com.example.softeng306plantasticapp.data.IItemDatabase;
import com.example.softeng306plantasticapp.data.ItemDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.softeng306plantasticapp.entities.IItem;
import com.example.softeng306plantasticapp.entities.Item;

public class SearchModel implements ISearchModel {
    String searchTerm;

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public CompletableFuture<ArrayList<IItem>> getSearchItems(Context context) {
        CompletableFuture<ArrayList<IItem>> futureItems = new CompletableFuture<>();
        ArrayList<IItem> searchItems = new ArrayList<>();
        IItemDatabase itemDB = ItemDatabase.getSingleton();

        itemDB.fetchAllItems(context, new FirestoreDocumentCallback() {
            @Override
            public void onDocumentFetched(Object allItems) {
                for (Item item : (List<Item>) allItems) {
                    // add to search results if item name contains search term, ignore spaces and case
                    if (item.getName().replaceAll("\\s+", " ").trim().toLowerCase().contains(searchTerm.replaceAll("\\s+", " ").toLowerCase())) {
                        searchItems.add(item);
                    }
                }
                futureItems.complete(searchItems);
            }
            @Override
            public void onFailure(Exception e) {futureItems.completeExceptionally(e);}
        });

        return futureItems;
    }
}
