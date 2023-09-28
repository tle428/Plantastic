package com.example.softeng306plantasticapp.models;

import android.content.Context;

import com.example.softeng306plantasticapp.data.FirestoreDocumentCallback;
import com.example.softeng306plantasticapp.data.ItemDatabase;
import com.example.softeng306plantasticapp.data.ProfileDatabase;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.softeng306plantasticapp.entities.IItem;
import com.example.softeng306plantasticapp.entities.Item;

/**
 * This class acts as a layer between the database class and the activity class. It contains functionalities to fetch the item information
 * from the database to be used for the activity class. It also contains some other logics and functionalities that operates the activity class.
 */
public class DetailsModel implements IDetailsModel {
    private String itemId;
    private IItem item;
    private ItemDatabase itemDB;
    private ProfileDatabase profileDB;
    private IProfileModel profileModel;
    private Boolean inWishlist;

    /**
     * This is a constructor of the class and sets up the details and database singletons required for operations of the class.
     * @param id the ID of the item being displayed. .
     */
    public DetailsModel(String id) {
        this.itemId = id;
        profileModel = new ProfileModel();
        profileDB = (ProfileDatabase) ProfileDatabase.getSingleton();
        itemDB = (ItemDatabase) ItemDatabase.getSingleton();
    }

    /**
     * This method communicates with the database to fetch the item information to e used in the activity class.
     * The CompletableFuture is a way to ensure the database is loaded first before using the parameters,
     * otherwise there will be a NULL pointer exception.
     * @param context
     * @return item object that matches the item being asked for.
     */
    public CompletableFuture<IItem> retrieveItem(Context context) {
        CompletableFuture<IItem> futureItem = new CompletableFuture<>();
        itemDB.fetchItem(context, itemId, new FirestoreDocumentCallback() {
            @Override
            public void onDocumentFetched(Object fetchedItem) {
                item = (Item) fetchedItem;
                futureItem.complete((Item) item);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Could not find item.");
            }
        });
        return futureItem;
    }

    /**
     * This method adds the amount of the given item to the cart list in the database.
     * Multiples of the same item are treated as duplicates.
     * @param quantity The amount of the item being added to the cart
     */
    public void addToCart(int quantity) {
        String userId = profileModel.getId();
        for(int i = 0; i<quantity; i++) {
            profileDB.addToShoppingCart(userId, itemId);
        }
    }

    /**
     * This method checks if the item is already in the wishlist and so can be used to later indicate which image
     * view is suitable for other methods.
     * The CompletableFuture is a way to ensure the database is loaded first before using the parameters,
     * otherwise there will be a NULL pointer exception.
     * @return TRUE if the item is in the wishlist, FALSE for if it is not
     */
    public CompletableFuture<Boolean> inWishlist() {
        CompletableFuture<Boolean> futureResult = new CompletableFuture<>();
        String userId = profileModel.getId();
        inWishlist = false;
        profileDB.fetchWishlist(userId, new FirestoreDocumentCallback() {
            @Override
            public void onDocumentFetched(Object wishlist) {
                if (wishlist == null) {
                    futureResult.complete(inWishlist);
                    return;
                }
                for(String item: (List<String>) wishlist) {
                    if(item.equals(itemId)) {
                        inWishlist = true;
                    }
                }
                futureResult.complete(inWishlist);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        return futureResult;
    }

    /**
     * This method adds the given item to the wishlist on the database of a particular user
     */
    public void addToWishlist() {
        String userId = profileModel.getId();
        profileDB.addToWishlist(userId, itemId);
    }

    /**
     * This method removes the given item to the wishlist on the database of a particular user
     */
    public void removeFromWishlist() {
        String userId = profileModel.getId();
        profileDB.deleteFromWishlist(userId, itemId);
    }

    /**
     * This method is a getter for the attached item object
     * @return item object
     */
    public IItem getItem() {
        return item;
    }

    /**
     * This method returns a boolean indicating if the item is in the wishlist
     * @return TRUE if the item is in the wishlist, otherwise FALSE
     */
    public Boolean getInWishlist() {
        return inWishlist;
    }

    /**
     * This method add the item being b=viewed to the view history in the profile view of the user.
     */
    public void addToViewHistory() {
        String userId = profileModel.getId();
        profileDB.addToViewHistory(userId, itemId);
    }
}
