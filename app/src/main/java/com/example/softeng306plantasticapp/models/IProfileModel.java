package com.example.softeng306plantasticapp.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.example.softeng306plantasticapp.entities.IItem;
import com.example.softeng306plantasticapp.entities.IProfile;

public interface IProfileModel {
    public CompletableFuture<IProfile> retrieveProfile();
    public IProfile getProfile();
    public String getId();
    public CompletableFuture<ArrayList<IItem>> getViewHistoryItems(Context context);
    public void clearViewHistory();
}
