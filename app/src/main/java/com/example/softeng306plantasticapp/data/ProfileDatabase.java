package com.example.softeng306plantasticapp.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;

import com.example.softeng306plantasticapp.entities.Profile;

public class ProfileDatabase implements IProfileDatabase{

    private FirebaseFirestore database;
    private static ProfileDatabase singletonInstance = null;

    private ProfileDatabase() {
        database = FirebaseFirestore.getInstance();
    }

    /**
     * Passes a reference to the single instance of the ProfileDatabase
     *
     * @return the profile database
     */
    public static IProfileDatabase getSingleton() {
        if (singletonInstance == null) {
            singletonInstance = new ProfileDatabase();
        }
        return singletonInstance;
    }

    /**
     * Fetches the profile stored in Firestore
     *
     * @param callback Firestore callback interface for handling documents
     */
    @Override
    public void fetchProfile(FirestoreDocumentCallback callback) {
        CollectionReference profileCollectionReference = database.collection("profiles");

        profileCollectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Profile> allFetchedProfiles = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // converting the document to a Profile object
                                Profile fetchedProfile = new Profile(document.getId(), (String) document.get("firstName"), (String) document.get("lastName"), (String) document.get("email"), (String) document.get("address") , (List<Object>)document.get("viewHistoryItems"), (List<Object>)document.get("wishlistItems"), (List<Object>)document.get("shoppingCartItems"));
                                allFetchedProfiles.add(fetchedProfile);
                            }
                            callback.onDocumentFetched(allFetchedProfiles);
                        } else {
                            callback.onFailure(new Exception("No profiles :(("));
                        }
                    }
                });
    }

    /**
     * Fetches the wishlist stored in the profile's document in Firestore
     *
     * @param profileId id of the profile
     * @param callback Firestore callback interface for handling documents
     */
    @Override
    public void fetchWishlist(String profileId, FirestoreDocumentCallback callback) {
        CollectionReference profileCollectionReference = database.collection("profiles");
        DocumentReference profileRef = profileCollectionReference.document(profileId);
        profileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    List<Object> wishlist = (List<Object>) document.get("wishlistItems");
                    callback.onDocumentFetched(wishlist);
                } else {
                    callback.onFailure(new Exception("No wishlist :(("));
                }
            }
        });
    }

    /**
     * Add the item id to the profile's wishlist
     *
     * @param profileId id of the profile
     * @param itemId id of the item to add
     */
    @Override
    public void addToWishlist(String profileId, String itemId) {
        CollectionReference profileCollectionReference = database.collection("profiles");
        DocumentReference profileRef = profileCollectionReference.document(profileId);
        database.runTransaction(new Transaction.Function<Void>() {

            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot document = transaction.get(profileRef);

                List<String> currentWishlist = (List<String>) document.get("wishlistItems");
                List<String> newWishlist = new ArrayList<>();

                // if wishlist exists, add to the current wishlist
                if (currentWishlist != null) {
                    newWishlist.addAll(currentWishlist);
                }

                // wishlist should not contain duplicate item ids
                if (!newWishlist.contains(itemId)) {
                    newWishlist.add(itemId);
                }
                transaction.update(profileRef, "wishlistItems", newWishlist);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ADD TO WISHLIST: ", "Transaction was successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ADD TO WISHLIST: ", "Transaction failed", e);
                    }
                });
    }

    /**
     * Delete a specific item from the profile's wishlist
     *
     * @param profileId id of the profile
     * @param itemId id of the item to delete
     */
    @Override
    public void deleteFromWishlist(String profileId, String itemId) {
        CollectionReference profileCollectionReference = database.collection("profiles");
        DocumentReference profileRef = profileCollectionReference.document(profileId);
        database.runTransaction(new Transaction.Function<Void>() {

                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot document = transaction.get(profileRef);

                        List<String> currentWishlist = (List<String>) document.get("wishlistItems");
                        List<String> newWishlist = new ArrayList<>();

                        // if wishlist has items, remove from to current wishlist and wishlist should not have duplicate item ids
                        if (currentWishlist != null && currentWishlist.contains(itemId)) {
                            newWishlist.addAll(currentWishlist);
                            newWishlist.remove(itemId);
                        }

                        transaction.update(profileRef, "wishlistItems", newWishlist);

                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DELETE FROM WISHLIST: ", "Transaction was successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DELETE FROM WISHLIST: ", "Transaction failed", e);
                    }
                });
    }

    /**
     * Delete the whole wishlist from the profile's document in Firestore
     *
     * @param profileId id of the profile to delete from
     */
    @Override
    public void clearWishlist(String profileId) {
        CollectionReference profileCollectionReference = database.collection("profiles");
        DocumentReference profileRef = profileCollectionReference.document(profileId);

        database.runTransaction(new Transaction.Function<Void>() {

                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot document = transaction.get(profileRef);
                        List<String> newWishlist = new ArrayList<>();
                        transaction.update(profileRef, "wishlistItems", newWishlist);

                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("CLEAR WISHLIST: ", "Transaction was successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("CLEAR WISHLIST: ", "Transaction failed", e);
                    }
                });
    }

    /**
     * Fetches the shopping cart stored in the profile's document in Firestore
     *
     * @param profileId id of the profile
     * @param callback Firestore callback interface for handling documents
     */
    @Override
    public void fetchShoppingCart(String profileId, FirestoreDocumentCallback callback) {
        CollectionReference profileCollectionReference = database.collection("profiles");
        DocumentReference profileRef = profileCollectionReference.document(profileId);
        profileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    List<Object> shoppingCart = (List<Object>) document.get("shoppingCartItems");
                    callback.onDocumentFetched(shoppingCart);
                } else {
                    callback.onFailure(new Exception("No shopping cart :(("));
                }
            }
        });
    }

    /**
     * Add the item id to the profile's shopping cart
     *
     * @param profileId id of the profile
     * @param itemId id of the item to add
     */
    @Override
    public void addToShoppingCart(String profileId, String itemId) {
        CollectionReference profileCollectionReference = database.collection("profiles");
        DocumentReference profileRef = profileCollectionReference.document(profileId);
        database.runTransaction(new Transaction.Function<Void>() {

                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot document = transaction.get(profileRef);

                        List<String> currentShoppingCart = (List<String>) document.get("shoppingCartItems");
                        List<String> newShoppingCart = new ArrayList<>();

                        // handling when nothing is in the cart
                        if (currentShoppingCart != null) {
                            newShoppingCart.addAll(currentShoppingCart);
                        }
                        newShoppingCart.add(itemId);

                        transaction.update(profileRef, "shoppingCartItems", newShoppingCart);

                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ADD TO SHOPPING CART: ", "Transaction was successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ADD TO SHOPPING CART: ", "Transaction failed", e);
                    }
                });
    }

    /**
     * Delete a specific item from the profile's shopping cart
     *
     * @param profileId id of the profile
     * @param itemId id of the item to delete
     */
    @Override
    public void deleteFromShoppingCart(String profileId, String itemId) {
        CollectionReference profileCollectionReference = database.collection("profiles");
        DocumentReference profileRef = profileCollectionReference.document(profileId);
        database.runTransaction(new Transaction.Function<Void>() {

                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot document = transaction.get(profileRef);

                        List<String> currentShoppingCart = (List<String>) document.get("shoppingCartItems");
                        List<String> newShoppingCart = new ArrayList<>();

                        // can only remove from cart if there are items in it
                        if (currentShoppingCart != null) {
                            newShoppingCart.addAll(currentShoppingCart);
                            newShoppingCart.remove(itemId);
                        }

                        transaction.update(profileRef, "shoppingCartItems", newShoppingCart);

                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DELETE FROM SHOPPING CART: ", "Transaction was successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DELETE FROM SHOPPING CART: ", "Transaction failed", e);
                    }
                });
    }

    /**
     * Delete a specific item from the profile's shopping cart
     *
     * @param profileId id of the profile
     * @param itemId id of the item to delete
     */
    @Override
    public void deleteAllFromShoppingCart(String profileId, String itemId) {
        CollectionReference profileCollectionReference = database.collection("profiles");
        DocumentReference profileRef = profileCollectionReference.document(profileId);
        database.runTransaction(new Transaction.Function<Void>() {

                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot document = transaction.get(profileRef);

                        List<String> currentShoppingCart = (List<String>) document.get("shoppingCartItems");
                        List<String> newShoppingCart = new ArrayList<>();

                        // can only remove from cart if there are items in it
                        if (currentShoppingCart != null) {
                            newShoppingCart.addAll(currentShoppingCart);

                            // delete all instances of the same item
                            while (newShoppingCart.contains(itemId)) {
                                newShoppingCart.remove(itemId);
                            }
                        }

                        transaction.update(profileRef, "shoppingCartItems", newShoppingCart);

                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DELETE FROM SHOPPING CART: ", "Transaction was successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DELETE FROM SHOPPING CART: ", "Transaction failed", e);
                    }
                });
    }

    /**
     * Delete the whole shopping cart from the profile's document in Firestore
     *
     * @param profileId id of the profile to delete from
     */
    @Override
    public void clearShoppingCart(String profileId) {
        CollectionReference profileCollectionReference = database.collection("profiles");
        DocumentReference profileRef = profileCollectionReference.document(profileId);

        database.runTransaction(new Transaction.Function<Void>() {

                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot document = transaction.get(profileRef);
                        List<String> newShoppingCart = new ArrayList<>();
                        transaction.update(profileRef, "shoppingCartItems", newShoppingCart);
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("CLEAR SHOPPING CART: ", "Transaction was successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("CLEAR SHOPPING CART: ", "Transaction failed", e);
                    }
                });
    }

    /**
     * Fetches the view history items stored in the profile's document in Firestore
     *
     * @param profileId id of the profile
     * @param callback Firestore callback interface for handling documents
     */
    @Override
    public void fetchViewHistory(String profileId, FirestoreDocumentCallback callback) {
        CollectionReference profileCollectionReference = database.collection("profiles");
        DocumentReference profileRef = profileCollectionReference.document(profileId);
        profileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    List<Object> wishlist = (List<Object>) document.get("viewHistoryItems");
                    callback.onDocumentFetched(wishlist);
                } else {
                    callback.onFailure(new Exception("No view history :(("));
                }
            }
        });
    }

    /**
     * Add the item id to the profile's view history
     *
     * @param profileId id of the profile
     * @param itemId id of the item to add
     */
    @Override
    public void addToViewHistory(String profileId, String itemId) {
        CollectionReference profileCollectionReference = database.collection("profiles");
        DocumentReference profileRef = profileCollectionReference.document(profileId);
        database.runTransaction(new Transaction.Function<Void>() {

                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot document = transaction.get(profileRef);

                        List<String> currentViewHistoryItems = (List<String>) document.get("viewHistoryItems");
                        List<String> newViewHistoryItems = new ArrayList<>();

                        // if view history has items, remove from current view history and view history should not have duplicate item ids
                        if (currentViewHistoryItems != null) {
                            newViewHistoryItems.addAll(currentViewHistoryItems);
                            if (currentViewHistoryItems.contains(itemId)) {
                                // remove existing item and add to end
                                newViewHistoryItems.remove(itemId);
                            }
                        }

                        newViewHistoryItems.add(0, itemId);
                        transaction.update(profileRef, "viewHistoryItems", newViewHistoryItems);

                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ADD TO VIEW HISTORY: ", "Transaction was successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ADD TO VIEW HISTORY: ", "Transaction failed", e);
                    }
                });
    }

    /**
     * Delete the whole view history from the profile's document in Firestore
     *
     * @param profileId id of the profile to delete from
     */
    @Override
    public void clearViewHistory(String profileId) {
        CollectionReference profileCollectionReference = database.collection("profiles");
        DocumentReference profileRef = profileCollectionReference.document(profileId);

        database.runTransaction(new Transaction.Function<Void>() {

                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot document = transaction.get(profileRef);

//                        List<String> currentViewHistoryItems = (List<String>) document.get("viewHistoryItems");
                        List<String> newViewHistoryItems = new ArrayList<>();

                        transaction.update(profileRef, "viewHistoryItems", newViewHistoryItems);

                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("CLEAR VIEW HISTORY: ", "Transaction was successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("CLEAR VIEW HISTORY: ", "Transaction failed", e);
                    }
                });
    }
}
