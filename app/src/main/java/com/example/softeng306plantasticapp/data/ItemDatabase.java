package com.example.softeng306plantasticapp.data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.softeng306plantasticapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import com.example.softeng306plantasticapp.entities.CactiAndSucculentPlant;
import com.example.softeng306plantasticapp.entities.FloweringPlant;
import com.example.softeng306plantasticapp.entities.FoliagePlant;
import com.example.softeng306plantasticapp.entities.IItem;

public class ItemDatabase implements IItemDatabase {

    private FirebaseFirestore database;
    private static ItemDatabase singletonInstance = null;

    private ItemDatabase() {
        database = FirebaseFirestore.getInstance();
    }

    /**
     * Passes a reference to the single instance of the ItemDatabase
     *
     * @return the item database
     */
    public static IItemDatabase getSingleton() {
        if (singletonInstance == null) {
            singletonInstance = new ItemDatabase();
        }
        return singletonInstance;
    }

    /**
     * Fetches the item which is queried for by the item's id from Firestore
     *
     * @param itemId id of the queried item
     * @param callback Firestore callback interface for handling documents
     */
    @Override
    public void fetchItem(Context context, String itemId, FirestoreDocumentCallback callback) {

        CollectionReference itemCollectionReference = database.collection("item");

        DocumentReference itemRef = itemCollectionReference.document(itemId);
        itemRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // converting document to an Item object
                        int listImage = context.getResources().getIdentifier("i_" + document.getId().toLowerCase() + "_1", "drawable", context.getPackageName());

                        IItem fetchedItem;
                        if (((DocumentReference) document.get("category")).getId().equals("floweringPlants")) {
                            fetchedItem = new FloweringPlant(document.getId(), (String) document.get("name"), (String) document.get("scientificName"), (DocumentReference) document.get("category"), ((Long) document.get("price")).doubleValue() ,listImage ,(String) document.get("description"), R.drawable.pink_box);
                        } else if (((DocumentReference) document.get("category")).getId().equals("foliage")) {
                            fetchedItem = new FoliagePlant(document.getId(), (String) document.get("name"), (String) document.get("scientificName"), (DocumentReference) document.get("category"), ((Long) document.get("price")).doubleValue() ,listImage ,(String) document.get("description"), R.drawable.green_box);
                        } else {
                            fetchedItem = new CactiAndSucculentPlant(document.getId(), (String) document.get("name"), (String) document.get("scientificName"), (DocumentReference) document.get("category"), ((Long) document.get("price")).doubleValue() ,listImage , (String) document.get("description"), R.drawable.yellow_box);
                        }

                        callback.onDocumentFetched(fetchedItem);
                    } else {
                        callback.onFailure(new Exception("Item doesn't exist :(("));
                    }
                } else {
                    callback.onFailure(task.getException());
                }
            }
        });
    }

    /**
     * Fetches all the items stored in the item collection in Firestore
     *
     * @param callback Firestore callback interface for handling documents
     */
    @Override
    public void fetchAllItems(Context context, FirestoreDocumentCallback callback) {
        CollectionReference itemCollectionReference = database.collection("item");

        itemCollectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<IItem> allFetchedItems = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // converting documents to Item objects
                        int listImage = context.getResources().getIdentifier("i_" + document.getId().toLowerCase() + "_1", "drawable", context.getPackageName());

                        IItem fetchedItem;
                        if (((DocumentReference) document.get("category")).getId().equals("floweringPlants")) {
                            fetchedItem = new FloweringPlant(document.getId(), (String) document.get("name"), (String) document.get("scientificName"), (DocumentReference) document.get("category"), ((Long) document.get("price")).doubleValue() ,listImage ,(String) document.get("description"), R.drawable.pink_box);
                        } else if (((DocumentReference) document.get("category")).getId().equals("foliage")) {
                            fetchedItem = new FoliagePlant(document.getId(), (String) document.get("name"), (String) document.get("scientificName"), (DocumentReference) document.get("category"), ((Long) document.get("price")).doubleValue() ,listImage ,(String) document.get("description"), R.drawable.green_box);
                        } else {
                            fetchedItem = new CactiAndSucculentPlant(document.getId(), (String) document.get("name"), (String) document.get("scientificName"), (DocumentReference) document.get("category"), ((Long) document.get("price")).doubleValue() ,listImage , (String) document.get("description"), R.drawable.yellow_box);
                        }

                        allFetchedItems.add(fetchedItem);
                    }
                    callback.onDocumentFetched(allFetchedItems);
                } else {
                    callback.onFailure(new Exception("No items :(("));
                }
            }
        });
    }
}
