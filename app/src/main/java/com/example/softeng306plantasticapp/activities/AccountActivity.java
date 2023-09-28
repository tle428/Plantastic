package com.example.softeng306plantasticapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.softeng306plantasticapp.R;
import com.example.softeng306plantasticapp.adaptors.AccountActivityAdaptor;
import com.example.softeng306plantasticapp.common.StringConverter;
import com.example.softeng306plantasticapp.models.IProfileModel;
import com.example.softeng306plantasticapp.models.ProfileModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {

    IProfileModel profileModel = new ProfileModel();

    /**
     * This is an override of the onCreate method. This acts to set up the account activity information and logic
     * that is appropriate for the account details that are being displayed.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // set up for bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_account);
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

        // Setting account information to the UI elements
        profileModel = new ProfileModel("13gNzygDVYm8BVjLgKPN");

        TextView firstNameView = findViewById(R.id.FNameTextView);
        TextView lastNameView = findViewById(R.id.LNameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);

        profileModel.retrieveProfile().thenAccept(profile -> {
            firstNameView.setText(profileModel.getProfile().getFirstName());
            lastNameView.setText(profileModel.getProfile().getLastName());
            emailTextView.setText(profileModel.getProfile().getEmail());
            StringConverter converter = new StringConverter();
            addressTextView.setText(converter.convertCommasToNewlines(profileModel.getProfile().getAddress()));
        });

        RecyclerView viewHistoryRecyclerView = findViewById(R.id.historyRecylerView);
        populateViewHistoryRecyclerView(viewHistoryRecyclerView);


    }

    /**
     * Filling the view history recycler view with the user's view history and handing UI for empty cases
     *
     * @param viewHistoryRecyclerView Recycler view of the view history
     */
    private void populateViewHistoryRecyclerView(RecyclerView viewHistoryRecyclerView) {
        profileModel.getViewHistoryItems(this).thenAccept(items -> {

            // set up UI for items in the table
            findViewById(R.id.noHistoryCard).setVisibility(View.GONE);
            findViewById(R.id.clearHistoryButton).setVisibility(View.VISIBLE);

            AccountActivityAdaptor accountActivityAdaptor = new AccountActivityAdaptor(this, items);
            viewHistoryRecyclerView.setAdapter(accountActivityAdaptor);
            viewHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // functionality for the clear history button
            FrameLayout clearHistoryButton = findViewById(R.id.clearHistoryButton);
            clearHistoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profileModel.clearViewHistory();
                    items.clear();
                    accountActivityAdaptor.notifyDataSetChanged();
                    // set up for no items in the table
                    findViewById(R.id.noHistoryCard).setVisibility(View.VISIBLE);
                    findViewById(R.id.clearHistoryButton).setVisibility(View.GONE);
                }
            });
        });
    }
}