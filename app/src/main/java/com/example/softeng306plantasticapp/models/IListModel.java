package com.example.softeng306plantasticapp.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.example.softeng306plantasticapp.entities.IItem;

public interface IListModel {
    public void setCategoryString(String categoryString);
    public CompletableFuture<ArrayList<IItem>> getCategoryItems(Context context);

}
