package com.example.softeng306plantasticapp.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.example.softeng306plantasticapp.entities.IItem;

public interface IMainModel {
    public CompletableFuture<ArrayList<IItem>> getFeaturedItems(Context context);
}
