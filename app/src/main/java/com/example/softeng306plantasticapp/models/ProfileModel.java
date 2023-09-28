package com.example.softeng306plantasticapp.models;

import android.content.Context;
import com.example.softeng306plantasticapp.data.FirestoreDocumentCallback;
import com.example.softeng306plantasticapp.data.IItemDatabase;
import com.example.softeng306plantasticapp.data.IProfileDatabase;
import com.example.softeng306plantasticapp.data.ItemDatabase;
import com.example.softeng306plantasticapp.data.ProfileDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.softeng306plantasticapp.entities.IItem;
import com.example.softeng306plantasticapp.entities.IProfile;
import com.example.softeng306plantasticapp.entities.Profile;

public class ProfileModel implements IProfileModel {
    private IProfile profile;
    private static String id;

    private IProfileDatabase profileDatabase = ProfileDatabase.getSingleton();

    public ProfileModel(){}
    public ProfileModel(String id) {
        this.id = id;
        retrieveProfile();
    }

    public CompletableFuture<IProfile> retrieveProfile() {
        CompletableFuture<IProfile> futureProfile = new CompletableFuture<>();
        profileDatabase.fetchProfile(new FirestoreDocumentCallback() {
            @Override
            public void onDocumentFetched(Object allProfiles) {
                for(Profile fetchedProfile:(List<Profile>)allProfiles) {
                    if(fetchedProfile.getId().equals(id)) {
                        profile = fetchedProfile;
                        System.out.println(profile.getFirstName());
                    }
                    futureProfile.complete((Profile) profile);
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Could not find profile.");
            }
        });
        return futureProfile;
    }

    public IProfile getProfile() {
        return profile;
    }
    public String getId() {return id;}

    public CompletableFuture<ArrayList<IItem>> getViewHistoryItems(Context context) {
        CompletableFuture<ArrayList<IItem>> futureItems = new CompletableFuture<>();
        ArrayList<IItem> viewHistoryItems = new ArrayList<>();
        profileDatabase.fetchViewHistory("13gNzygDVYm8BVjLgKPN", new FirestoreDocumentCallback() {
            @Override
            public void onDocumentFetched(Object viewHistoryitemids) {
                IItemDatabase itemDatabase = ItemDatabase.getSingleton();
                ArrayList<String> itemIds = (ArrayList<String>) viewHistoryitemids;
                AtomicInteger count = new AtomicInteger(itemIds.size());
                for (String itemId : itemIds) {
                    itemDatabase.fetchItem(context, itemId, new FirestoreDocumentCallback() {
                        @Override
                        public void onDocumentFetched(Object currentItem) {
                            viewHistoryItems.add(0, (IItem) currentItem);
                            if(count.decrementAndGet() == 0) {
                                futureItems.complete(viewHistoryItems);
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

    public void clearViewHistory() {
        profileDatabase.clearViewHistory("13gNzygDVYm8BVjLgKPN");
    }
}
