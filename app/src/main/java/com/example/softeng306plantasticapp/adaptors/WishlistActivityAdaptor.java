package com.example.softeng306plantasticapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306plantasticapp.R;

import java.util.ArrayList;

import com.example.softeng306plantasticapp.entities.IItem;

public class WishlistActivityAdaptor extends RecyclerView.Adapter<WishlistActivityAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<IItem> items;
    private WishlistActivityAdaptor.OnItemClickListener listener;

    public WishlistActivityAdaptor(Context context, ArrayList<IItem> items) {
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public WishlistActivityAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout here (give it the look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_wishlist_item_row, parent, false);

        return new WishlistActivityAdaptor.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistActivityAdaptor.ViewHolder holder, int position) {
        // assign values to the row layout
        holder.itemImage.setImageResource((int) items.get(position).getListImage());
        holder.backgroundImage.setImageResource((int) items.get(position).getBackgroundImage());
        holder.scientificName.setText(items.get(position).getScientificName());
        holder.flowerName.setText(items.get(position).getName());
        holder.price.setText("$" + String.format("%.2f", items.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        // num of items in recycleview
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onAddToCartClick(int position);
    }

    public void setOnItemClickListener(WishlistActivityAdaptor.OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // gets view from row layout xml

        ImageButton deleteButton, addToCartButton;
        ImageView itemImage, backgroundImage;
        TextView scientificName, flowerName, price;
        public ViewHolder(@NonNull View itemView, final WishlistActivityAdaptor.OnItemClickListener listener) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImage);
            backgroundImage = itemView.findViewById(R.id.itemBackground);
            scientificName = itemView.findViewById(R.id.scientificName);
            flowerName = itemView.findViewById(R.id.flowerName);
            price = itemView.findViewById(R.id.price);

            // Add OnClickListener to the entire item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            deleteButton = itemView.findViewById(R.id.deleteFromCart);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });

            addToCartButton = itemView.findViewById(R.id.addToCart);
            addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.onAddToCartClick(position);
                }
            });
        }
    }
}
