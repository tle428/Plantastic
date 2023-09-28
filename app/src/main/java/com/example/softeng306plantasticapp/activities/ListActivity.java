package com.example.softeng306plantasticapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.softeng306plantasticapp.R;
import com.example.softeng306plantasticapp.adaptors.ListActivityAdaptor;
import com.example.softeng306plantasticapp.models.IListModel;
import com.example.softeng306plantasticapp.models.ListModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import com.example.softeng306plantasticapp.entities.IItem;

public class ListActivity extends AppCompatActivity {

    IListModel listModel = new ListModel();
    Map<Integer, String> viewIdToCategoryId = new HashMap<>();

    /**
     * This is an override of the onCreate method. This acts to set up the list activity information and logic
     * that is appropriate for displaying categories and the items belonging to those categories
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

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

        // get category name from intent
        Intent categoryIntent = getIntent();
        String categoryId = categoryIntent.getStringExtra("category");

        // Access to category tabs
        int[] categoryTabIds = new int[]{R.id.activeViewAllPlants, R.id.activeViewFlowering, R.id.activeViewCacti, R.id.activeViewFoliage};

        // set up mapping from view id to category id
        viewIdToCategoryId.put(R.id.activeViewAllPlants, "");
        viewIdToCategoryId.put(R.id.activeViewFlowering, "floweringPlants");
        viewIdToCategoryId.put(R.id.activeViewCacti, "cactiAndSucculents");
        viewIdToCategoryId.put(R.id.activeViewFoliage, "foliage");

        // Handling when category is selected
        View.OnClickListener onCategorySelect = new View.OnClickListener() {
            @Override
            public void onClick(View selectedCategoryView) {
                setActiveCategory(selectedCategoryView);
            }
        };

        // Assign listener to category tab
        for (int viewId : categoryTabIds) {
            FrameLayout categoryLayout = findViewById(viewId);
            categoryLayout.setOnClickListener(onCategorySelect);
        }

        // set up contents based on selected category
        listModel.setCategoryString(categoryId);
        setActiveCategory(getViewByCategory(categoryId));
    }

    /**
     * Given the id of the category, retrieve the id of the UI component representing that category
     * in the tabs
     * @param categoryId id of the selected category
     * @return the id of the view corresponding to the category's tab
     */
    private View getViewByCategory(String categoryId) {
        View categoryView;
        switch (categoryId) {
            case "floweringPlants":
                categoryView = findViewById(R.id.activeViewFlowering);
                break;
            case "cactiAndSucculents":
                categoryView = findViewById(R.id.activeViewCacti);
                break;
            case "foliage":
                categoryView = findViewById(R.id.activeViewFoliage);
                break;
            default:
                categoryView = findViewById(R.id.activeViewAllPlants);
                break;
        }
        return categoryView;
    }

    /**
     * When the category tab is pressed, set the UI styles and provide the recycler view the
     * category to display
     * @param selectedCategoryView the view of the category tab that is selected
     */
    private void setActiveCategory(View selectedCategoryView) {
        int selectedCategoryViewId = selectedCategoryView.getId();
        handleCategoryStyles(selectedCategoryViewId);

        // setting up the recyclerview to display items
        listModel.setCategoryString(viewIdToCategoryId.get(selectedCategoryViewId));
        RecyclerView itemsRecyclerView = findViewById(R.id.listActivityRecycler);
        populateRecyclerView(itemsRecyclerView);
    }

    /**
     * Setting highlight and border for the active category and removing them from the ones that
     * aren't selected
     * @param categoryViewId the id of the selected category view tab
     */
    private void handleCategoryStyles(int categoryViewId) {
        View[] categoryTabs = {findViewById(R.id.activeViewAllPlants), findViewById(R.id.activeViewFlowering), findViewById(R.id.activeViewCacti), findViewById(R.id.activeViewFoliage)};
        for (View categoryTab : categoryTabs) {
            int currentCategoryTabId = categoryTab.getId();
            if (currentCategoryTabId == categoryViewId) {
                // apply active border and highlight styles
                categoryTab.setBackgroundResource(R.drawable.categories_active_bottom_border);
                addCategoryHighlight(currentCategoryTabId);
            } else {
                // remove active border and highlight styles
                categoryTab.setBackgroundResource(R.drawable.categories_clear);
                removeCategoryHighlight(currentCategoryTabId);
            }
        }
    }

    /**
     * Applying highlight to the text of the active category tab
     * @param categoryViewId the id of the selected category view tab
     */
    private void addCategoryHighlight(int categoryViewId) {
        switch (categoryViewId) {
            case 2131296859:
                findViewById(R.id.AllPlantsCategoryLabel).setBackgroundResource(R.drawable.categories_pink_highlight);
                break;
            case 2131296852:
                findViewById(R.id.FloweringPlantsLabel).setBackgroundResource(R.drawable.categories_pink_highlight);
                findViewById(R.id.PlantsLabel).setBackgroundResource(R.drawable.categories_pink_small_highlight);
                break;
            case 2131296851:
                findViewById(R.id.CactiLabel).setBackgroundResource(R.drawable.categories_pink_highlight);
                findViewById(R.id.SucculentsLabel).setBackgroundResource(R.drawable.categories_pink_small_highlight);
                break;
            case 2131296853:
                findViewById(R.id.FoliageCategoryLabel).setBackgroundResource(R.drawable.categories_pink_highlight);
                break;
            default:
                break;
        }
    }

    /**
     * Removing highlight to the text of the active category tab
     * @param categoryViewId the id of the selected category view tab
     */
    private void removeCategoryHighlight(int categoryViewId) {

        switch (categoryViewId) {
            case 2131296859:
                findViewById(R.id.AllPlantsCategoryLabel).setBackgroundResource(R.drawable.categories_clear);
                break;
            case 2131296852:
                findViewById(R.id.FloweringPlantsLabel).setBackgroundResource(R.drawable.categories_clear);
                findViewById(R.id.PlantsLabel).setBackgroundResource(R.drawable.categories_clear);
                break;
            case 2131296851:
                findViewById(R.id.CactiLabel).setBackgroundResource(R.drawable.categories_clear);
                findViewById(R.id.SucculentsLabel).setBackgroundResource(R.drawable.categories_clear);
                break;
            case 2131296853:

                findViewById(R.id.FoliageCategoryLabel).setBackgroundResource(R.drawable.categories_clear);
                break;
            default:
                break;
        }
    }

    /**
     * Filling the list recycler view with the items of the selected category
     * @param itemsRecyclerView Recycler view of the list items
     */
    private void populateRecyclerView(RecyclerView itemsRecyclerView) {
        listModel.getCategoryItems(this).thenAccept(items -> {
            ListActivityAdaptor adaptor = new ListActivityAdaptor(this, items);
            // setting functionality for selecting an item
            adaptor.setOnItemClickListener(new ListActivityAdaptor.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    IItem currentItem = items.get(position);
                    Intent detailIntent = new Intent(getBaseContext(), DetailsActivity.class);
                    detailIntent.putExtra("ITEM_ID", currentItem.getId());
                    startActivity(detailIntent);
                }
            });

            itemsRecyclerView.setAdapter(adaptor);
            itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        });
    }
}