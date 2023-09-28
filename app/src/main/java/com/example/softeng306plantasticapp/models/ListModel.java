package com.example.softeng306plantasticapp.models;

import android.content.Context;

import com.example.softeng306plantasticapp.data.FirestoreDocumentCallback;
import com.example.softeng306plantasticapp.data.IItemDatabase;
import com.example.softeng306plantasticapp.data.ItemDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.softeng306plantasticapp.entities.IItem;

public class ListModel implements IListModel {
    String categoryString;

    public void setCategoryString(String categoryString) {
        this.categoryString = categoryString;
    }

    public CompletableFuture<ArrayList<IItem>> getCategoryItems(Context context) {
        CompletableFuture<ArrayList<IItem>> futureItems = new CompletableFuture<>();
        ArrayList<IItem> categoryItems = new ArrayList<>();
        IItemDatabase itemDB = ItemDatabase.getSingleton();

        itemDB.fetchAllItems(context, new FirestoreDocumentCallback() {
            @Override
            public void onDocumentFetched(Object allItems) {
                if (categoryString.equals("")) {
                    futureItems.complete((ArrayList<IItem>) allItems);
                    return;
                }
                for (IItem item : (List<IItem>) allItems) {
                    if (item.getCategory().getId().equals(categoryString)) {
                        categoryItems.add(item);
                    }
                }
                futureItems.complete(categoryItems);
            }
            @Override
            public void onFailure(Exception e) {futureItems.completeExceptionally(e);}
        });

        return futureItems;
    }
}
