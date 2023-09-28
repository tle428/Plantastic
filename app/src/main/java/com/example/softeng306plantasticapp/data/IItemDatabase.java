package com.example.softeng306plantasticapp.data;

import android.content.Context;

public interface IItemDatabase {

    void fetchItem(Context context, String itemId, FirestoreDocumentCallback callback);

    void fetchAllItems(Context context, FirestoreDocumentCallback callback);
}
