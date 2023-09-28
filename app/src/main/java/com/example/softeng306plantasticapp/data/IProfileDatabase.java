package com.example.softeng306plantasticapp.data;

public interface IProfileDatabase {


    void fetchProfile(FirestoreDocumentCallback callback);

    void fetchWishlist(String profileId, FirestoreDocumentCallback callback);

    void addToWishlist(String profileId, String itemId);

    void deleteFromWishlist(String profileId, String itemId);

    void clearWishlist(String profileId);

    void fetchShoppingCart(String profileId, FirestoreDocumentCallback callback);

    void addToShoppingCart(String profileId, String itemId);

    void deleteFromShoppingCart(String profileId, String itemId);
    void deleteAllFromShoppingCart(String profileId, String itemId);

    void clearShoppingCart(String profileId);

    void fetchViewHistory(String profileId, FirestoreDocumentCallback callback);

    void addToViewHistory(String profileId, String itemId);

    void clearViewHistory(String profileId);
}
