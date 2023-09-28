package com.example.softeng306plantasticapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.softeng306plantasticapp.R;
import com.example.softeng306plantasticapp.adaptors.CartActivityAdaptor;
import com.example.softeng306plantasticapp.models.CartModel;
import com.example.softeng306plantasticapp.models.ICartModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.softeng306plantasticapp.entities.IItem;

public class CartActivity extends AppCompatActivity {

    ICartModel cartModel = new CartModel();
    TextView totalCostText;

    /**
     * This is an override of the onCreate method. This acts to set up the cart activity information and logic
     * that is appropriate for the cart items and details that are being displayed.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        totalCostText = findViewById(R.id.totalCost);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);
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
        RecyclerView cartItemsRecyclerView = findViewById(R.id.cartActivityRecycler);
        cartModel.getCartItems(this).thenAccept(items -> {
            CartActivityAdaptor cartActivityAdaptor = new CartActivityAdaptor(this, items);
            cartActivityAdaptor.setOnItemClickListener(new CartActivityAdaptor.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent detailsIntent = new Intent(CartActivity.this, DetailsActivity.class);
                    detailsIntent.putExtra("ITEM_ID", items.get(position).getId());
                    startActivity(detailsIntent);
                }

                @Override
                public void onDeleteClick(int position) {
                    cartModel.removeCartItem(items.get(position));
                    // Remove from the local list
                    items.remove(position);
                    cartActivityAdaptor.notifyItemRemoved(position);
                    updateCachedTotalCost();
                }

                @Override
                public void onDecrementQuantity(int position) {
                    IItem currentItem = items.get(position);
                    cartModel.decrementCartItemQuantity(currentItem);
                    // Update local list
                    currentItem.setQuantity(currentItem.getQuantity() - 1);
                    cartActivityAdaptor.notifyItemChanged(position);
                    updateCachedTotalCost();
                }

                @Override
                public void onIncrementQuantity(int position) {
                    IItem currentItem = items.get(position);
                    cartModel.incrementCartItemQuantity(currentItem);
                    // Update local list
                    currentItem.setQuantity(currentItem.getQuantity() + 1);
                    cartActivityAdaptor.notifyItemChanged(position);
                    updateCachedTotalCost();
                }
            });
            cartItemsRecyclerView.setAdapter(cartActivityAdaptor);
            cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        });

        ImageView clearCartButton = findViewById(R.id.clearButton);
        clearCartButton.setOnClickListener(v -> {
            cartModel.clearCart();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });

        updateTotalCost();
    }

    /**
     * Setting content of the Total Cost TextView to the latest total from the model class
     */
    private void updateTotalCost() {
        cartModel.getTotalCost().thenAccept(totalCost -> {
            totalCostText.setText("$" + String.format("%.2f", totalCost));
        });
    }

    /**
     * Setting content of the Total Cost TextView to the latest cached total cost from the model class
     */
    private void updateCachedTotalCost() {
        totalCostText.setText("$" + String.format("%.2f", cartModel.getCachedTotalCost()));
    }
}