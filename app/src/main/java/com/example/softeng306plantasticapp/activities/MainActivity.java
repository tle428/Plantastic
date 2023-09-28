package com.example.softeng306plantasticapp.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.softeng306plantasticapp.R;
import com.example.softeng306plantasticapp.adaptors.MainActivityAdaptor;
import com.example.softeng306plantasticapp.models.IMainModel;
import com.example.softeng306plantasticapp.models.MainModel;
import com.example.softeng306plantasticapp.models.ProfileModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    IMainModel mainModel = new MainModel();

    /**
     * This is an override of the onCreate method. This acts to set up the main activity information and logic
     * that is appropriate for displaying the featured items and categories
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the navigation bar at the bottom of the view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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

        int[] categoryCardIds = {R.id.categoryCardFlowering, R.id.categoryCardCacti, R.id.categoryCardFoliage};
        View.OnClickListener categoryCardListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCategoryCardClick(view);
            }
        };
        // Assign the listener to each Category Card
        for (int id : categoryCardIds) {
            FrameLayout frameLayout = findViewById(id);
            frameLayout.setOnClickListener(categoryCardListener);
        }

        // Create profile for access to wishlist and other features
        ProfileModel profile = new ProfileModel("13gNzygDVYm8BVjLgKPN");

        RecyclerView featuredItemsRecyclerView = findViewById(R.id.featuredItemsRecycler);
        mainModel.getFeaturedItems(this).thenAccept(featuredItems -> {
            MainActivityAdaptor adaptor = new MainActivityAdaptor(this, featuredItems);
            adaptor.setOnItemClickListener(new MainActivityAdaptor.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent detailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
                    detailsIntent.putExtra("ITEM_ID", featuredItems.get(position).getId());
                    startActivity(detailsIntent);
                }
            });
            featuredItemsRecyclerView.setAdapter(adaptor);
            featuredItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        });
    }

    /**
     * When a category card is pressed, the id of that category is sent to the ListActivity and the
     * transition to the ListActivity is handled
     *
     * @param clickedCategoryCard the view of the selected category
     */
    private void handleCategoryCardClick(View clickedCategoryCard) {
        // gets tag from the category card that was clicked, sends it to the List Activity as an Intent
        String tag = (String) clickedCategoryCard.getTag();
        Intent listActivityIntent = new Intent(getBaseContext(), ListActivity.class);
        listActivityIntent.putExtra("category", tag);
        startActivity(listActivityIntent);
    }
}