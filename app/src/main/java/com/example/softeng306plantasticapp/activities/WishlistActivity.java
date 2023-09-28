package com.example.softeng306plantasticapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.softeng306plantasticapp.R;
import com.example.softeng306plantasticapp.adaptors.WishlistActivityAdaptor;
import com.example.softeng306plantasticapp.models.IWishlistModel;
import com.example.softeng306plantasticapp.models.WishlistModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WishlistActivity extends AppCompatActivity {

    IWishlistModel wishlistModel = new WishlistModel();

    /**
     * This is an override of the onCreate method. This acts to set up the wishlist activity information and logic
     * that is appropriate for handling and displaying the user's wishlist items
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        // Create the navigation bar at the bottom of the view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_wishlist);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
            } else if (itemId == R.id.nav_account) {
                startActivity(new Intent(this, AccountActivity.class));
            } else if (itemId == R.id.nav_wishlist) {
                startActivity(new Intent(this, WishlistActivity.class));
            }
            return false;
        });

        // set up and populate recycler view
        RecyclerView wishlistItemsRecyclerView = findViewById(R.id.wishlistActivityRecycler);
        wishlistModel.getWishlistItems(this).thenAccept(items -> {
            WishlistActivityAdaptor wishlistActivityAdaptor = new WishlistActivityAdaptor(this, items);
            wishlistActivityAdaptor.setOnItemClickListener(new WishlistActivityAdaptor.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent detailsIntent = new Intent(WishlistActivity.this, DetailsActivity.class);
                    detailsIntent.putExtra("ITEM_ID", items.get(position).getId());
                    startActivity(detailsIntent);
                }

                @Override
                public void onDeleteClick(int position) {
                    // Handle delete button click
                    wishlistModel.removeWishlistItem(items.get(position).getId());
                    items.remove(position);
                    wishlistActivityAdaptor.notifyItemRemoved(position);
                }

                @Override
                public void onAddToCartClick(int position) {
                    // Handle add to cart button click
                    wishlistModel.addToCart(items.get(position).getId());
                }
            });
            wishlistItemsRecyclerView.setAdapter(wishlistActivityAdaptor);
            wishlistItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            ImageView clearButton = findViewById(R.id.clearButton);
            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wishlistModel.clearWishlist();
                    // remove all items from recycler view
                    items.clear();
                    wishlistActivityAdaptor.notifyDataSetChanged();
                    // restart activity
                    finish();
                    startActivity(getIntent());
                }
            });
        });
    }

}