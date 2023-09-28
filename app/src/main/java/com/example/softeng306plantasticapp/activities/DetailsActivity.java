package com.example.softeng306plantasticapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.softeng306plantasticapp.R;
import com.example.softeng306plantasticapp.common.ItemCounter;
import com.example.softeng306plantasticapp.models.DetailsModel;
import com.example.softeng306plantasticapp.models.IDetailsModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * This class interacts with the Details View of the application. It often calls on the DetailsModel class
 * for some functionalities. Other classes are also used for arithmetics and animations.
 */
public class DetailsActivity extends AppCompatActivity {

    private ImageView itemImage1, itemImage2, itemImage3;
    private ImageView arrowLeft;
    private ImageView arrowRight;
    private ImageView dot1;
    private ImageView dot2;
    private ImageView dot3;
    private ImageView plantTypeLabel;
    private TextView scientificNameLabel;
    private TextView plantNameLabel;
    private TextView priceLabel;
    private ImageView minusButton;
    private ImageView plusButton;
    private TextView quantityLabel;
    private ImageView addCartButton;
    private ImageView wishlistButton;
    private TextView descriptionLabel;


    private int position;
    private double price;
    private int quantity;
    private ItemCounter counter;
    private Boolean wish;
    private IDetailsModel detailsModel;

    /**
     * This is an override of the onCreate method. This acts to set up the details activity information and logic
     * that is appropriate for the item being displayed.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Initialize buttons and images and maps them appropriately
        itemImage1 = findViewById(R.id.itemImage1);
        itemImage2 = findViewById(R.id.itemImage2);
        itemImage3 = findViewById(R.id.itemImage3);
        arrowLeft = findViewById(R.id.leftArrowButton);
        arrowRight = findViewById(R.id.rightArrowButton);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        plantTypeLabel = findViewById(R.id.plantTypeLabel);
        scientificNameLabel = findViewById(R.id.scientificLabel);
        plantNameLabel = findViewById(R.id.plantNameLabel);
        priceLabel = findViewById(R.id.priceLabel);
        minusButton = findViewById(R.id.decrementButton);
        plusButton = findViewById(R.id.incrementButton);
        quantityLabel = findViewById(R.id.quantityLabel);
        addCartButton = findViewById(R.id.addCartButton);
        wishlistButton = findViewById(R.id.wishlistButton);
        descriptionLabel = findViewById(R.id.descriptionTextField);

        // This is passed from another view giving us the item ID for the required item
        Intent detailsIntent = getIntent();
        String itemID = detailsIntent.getStringExtra("ITEM_ID");
        System.out.println(itemID);

        // Sets the appropriate images for the items
        setImages(itemID);

        // Use the detailModel instance to populate the details page with the appropriate data
        detailsModel = new DetailsModel(itemID);
        detailsModel.retrieveItem(this).thenAccept(item -> {

            setCategoryTag(detailsModel.getItem().getCategory().getId());
            scientificNameLabel.setText(detailsModel.getItem().getScientificName());
            plantNameLabel.setText((detailsModel.getItem().getName()));
            this.price = detailsModel.getItem().getPrice();
            this.quantity = 1;
            updatePrice();
            descriptionLabel.setText(detailsModel.getItem().getDescription());
            detailsModel.inWishlist().thenAccept(result -> {
                if(detailsModel.getInWishlist()) {
                    wishlistButton.setImageResource(R.drawable.remove_from_wishlist_button);
                    wish = true;
                } else {
                    wish = false;
                }
            });

        });

        this.counter = new ItemCounter();

        position = 1;
        updateNavigationBar(position);

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

        // add item to user's view history
        detailsModel.addToViewHistory();
    }

    /**
     * This method animated the three dots in the middle of the screen as to when they should be filled or empty
     * according to which arrow is pressed. Additionally it also hollows the arrow if it cannot be pressed further.
     *
     * @param position is the input position of where the filled dot is currently
     */
    public void updateNavigationBar(int position) {
        // Clear all dots
        dot1.setImageResource(R.drawable.dot_clear);
        dot2.setImageResource(R.drawable.dot_clear);
        dot3.setImageResource(R.drawable.dot_clear);

        // Set the current position dot to filled
        switch (position) {
            case 1:
                dot1.setImageResource(R.drawable.dot_solid);
                arrowLeft.setImageResource(R.drawable.arrow_left_clear);
                arrowRight.setImageResource(R.drawable.arrow_right_solid);
                break;
            case 2:
                dot2.setImageResource(R.drawable.dot_solid);
                arrowLeft.setImageResource(R.drawable.arrow_left_solid);
                arrowRight.setImageResource(R.drawable.arrow_right_solid);
                break;
            case 3:
                dot3.setImageResource(R.drawable.dot_solid);
                arrowLeft.setImageResource(R.drawable.arrow_left_solid);
                arrowRight.setImageResource(R.drawable.arrow_right_clear);
                break;
        }
    }

    /**
     * This method sets the THREE images according to which item need to be displayed in the Details View
     *
     * @param itemId is the ID string of a particular item
     */
    private void setImages(String itemId) {
        String imageName1, imageName2, imageName3;
        int resId1, resId2, resId3;

        // deriving file name from the item's id
        imageName1 = "i_" + itemId.toLowerCase() + "_1";
        imageName2 = "i_" + itemId.toLowerCase() + "_2";
        imageName3 = "i_" + itemId.toLowerCase() + "_3";

        // getting image resources
        resId1 = getResources().getIdentifier(imageName1, "drawable", getPackageName());
        resId2 = getResources().getIdentifier(imageName2, "drawable", getPackageName());
        resId3 = getResources().getIdentifier(imageName3, "drawable", getPackageName());

        // setting images
        itemImage1.setImageResource(resId1);
        itemImage2.setImageResource(resId2);
        itemImage3.setImageResource(resId3);

    }

    /**
     * This method updates the price shown in the details view by matching the cost per unit with the
     * current number of units
     */
    private void updatePrice() {
        double newPrice = price*quantity;
        priceLabel.setText(String.format("$%.2f", newPrice));
    }

    /**
     * This method increment the item quantity shown in the Details View and call for a method to update
     * the price shown
     *
     * @param view the pressed view
     */
    public void incrementQuantity(View view) {
        counter.increment();
        quantity = counter.getCount();
        quantityLabel.setText(String.format("%d", quantity));
        updatePrice();
    }

    /**
     * This method decrement the item quantity shown in the Details View and call for a method to update
     * the price shown
     *
     * @param view the pressed view
     */
    public void decrementQuantity(View view) {
        counter.decrement();
        quantity = counter.getCount();
        quantityLabel.setText(String.format("%d", quantity));
        updatePrice();
    }

    /**
     * Depending on which of the THREE images are currently shown, this method switch the image to
     * the next image of the item. This calls on the swipeImage() method for the animation.
     *
     * @param view the pressed view
     */
    public void nextImage(View view) {
        if(position<3) {

            if(position==1) {
                swipeImages(itemImage1, itemImage2, true);
            } else {
                swipeImages(itemImage2, itemImage3, true);
            }
            position++;
            updateNavigationBar(position);
        }
    }

    /**
     * Depending on which of the THREE images are currently shown, this method switch the image to
     * the previous image of the item. This calls on the swipeImage() method for the animation.
     *
     * @param view the pressed view
     */
    public void previousImage(View view) {
        if(position>1) {

            if(position==3) {
                swipeImages(itemImage3, itemImage2, false);
            } else {
                swipeImages(itemImage2, itemImage1, false);
            }
            position--;
            updateNavigationBar(position);
        }
    }

    /**
     * This method adds the item to the cart in the database.
     *
     * @param view the pressed view
     */
    public void addCart(View view) {
        detailsModel.addToCart(quantity);
    }

    /**
     * This method adds the item to the wishlist database.
     *
     * @param view the pressed view
     */
    public void wishlist(View view) {
        if(!this.wish) {
            wishlistButton.setImageResource(R.drawable.remove_from_wishlist_button);
            detailsModel.addToWishlist();
            wish = true;
        } else {
            wishlistButton.setImageResource(R.drawable.add_to_wishlist_button);
            detailsModel.removeFromWishlist();
            wish = false;
        }
    }

    /**
     * This is an animation method that 'swipes' a given image view into the screen and replace it
     * with another given image view.
     *
     * @param outView The image view that is being animated out of the screen
     * @param inView The image view that is being animated into the screen
     * @param direction TRUE for swiping to the right, FALSE for swiping to the left
     */
    private void swipeImages(ImageView outView, ImageView inView, Boolean direction) {
        int width = outView.getWidth();

        TranslateAnimation animOut;
        TranslateAnimation animIn;

        if (direction) {
            animOut = new TranslateAnimation(0, -width, 0, 0);
            animIn = new TranslateAnimation(width, 0, 0, 0);
        } else {
            animOut = new TranslateAnimation(0, width, 0, 0);
            animIn = new TranslateAnimation(-width, 0, 0, 0);
        }

        animOut.setDuration(400);
        animOut.setFillAfter(true);

        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                outView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        outView.startAnimation(animOut);

        inView.setVisibility(View.VISIBLE);
        animIn.setDuration(400);
        inView.startAnimation(animIn);
    }

    /**
     * This method set the TYPE tag on the details view to the category that the item belongs to.
     *
     * @param categoryId id of the category
     */
    private void setCategoryTag(String categoryId) {
        switch (categoryId) {
            case "floweringPlants":
                plantTypeLabel.setImageResource(R.drawable.category_tag_flowering);
                break;
            case "cactiAndSucculents":
                plantTypeLabel.setImageResource(R.drawable.category_tag_cacti);
                break;
            case "foliage":
                plantTypeLabel.setImageResource(R.drawable.category_tag_foliage);
                break;
            default:
                break;
        }
    }

}