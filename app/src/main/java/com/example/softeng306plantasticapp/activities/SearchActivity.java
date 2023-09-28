package com.example.softeng306plantasticapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.softeng306plantasticapp.R;
import com.example.softeng306plantasticapp.adaptors.SearchActivityAdaptor;
import com.example.softeng306plantasticapp.models.ISearchModel;
import com.example.softeng306plantasticapp.models.SearchModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    ISearchModel searchModel = new SearchModel();

    /**
     * This is an override of the onCreate method. This acts to set up the search activity information and logic
     * that is appropriate for handling search functionality and displaying items
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Create the navigation bar at the bottom of the view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.invisible);
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

        Intent searchIntent = getIntent();
        String searchTerm = searchIntent.getStringExtra("SEARCH_TERM");

        searchModel.setSearchTerm(searchTerm);
        // set up and populate recycler view
        RecyclerView itemsRecyclerView = findViewById(R.id.searchActivityRecycler);
        searchModel.getSearchItems(this).thenAccept(items -> {
            SearchActivityAdaptor adaptor = new SearchActivityAdaptor(this, items);
            adaptor.setOnItemClickListener(new SearchActivityAdaptor.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent detailsIntent = new Intent(SearchActivity.this, DetailsActivity.class);
                    detailsIntent.putExtra("ITEM_ID", items.get(position).getId());
                    startActivity(detailsIntent);
                }
            });

            // fill text views for search results
            TextView searchAmountText = findViewById(R.id.searchAmountText);
            searchAmountText.setText(items.size() + "");

            TextView searchItemText = findViewById(R.id.searchItemText);
            searchItemText.setText("\"" + searchTerm + "\"");

            if (items.size() == 0) {
                findViewById(R.id.noSearchItemMessage).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.noSearchItemMessage).setVisibility(View.GONE);
            }

            itemsRecyclerView.setAdapter(adaptor);
            itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        });
    }
}