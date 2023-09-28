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

public class CartActivityAdaptor extends RecyclerView.Adapter<CartActivityAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<IItem> items;
    private CartActivityAdaptor.OnItemClickListener listener;

    public CartActivityAdaptor(Context context, ArrayList<IItem> items) {
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public CartActivityAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout here (give it the look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_cart_item_row, parent, false);

        return new CartActivityAdaptor.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartActivityAdaptor.ViewHolder holder, int position) {
        // assign values to the row layout
        holder.itemImage.setImageResource((int) items.get(position).getListImage());
        holder.backgroundImage.setImageResource((int) items.get(position).getBackgroundImage());
        holder.scientificName.setText(items.get(position).getScientificName());
        holder.flowerName.setText(items.get(position).getName());
        holder.price.setText("$" + String.format("%.2f", items.get(position).getPrice()));
        holder.quantity.setText(Integer.toString(items.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        // num of items in recycleview
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onDecrementQuantity(int position);
        void onIncrementQuantity(int position);
    }

    public void setOnItemClickListener(CartActivityAdaptor.OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // gets view from row layout xml

        ImageButton deleteButton;
        ImageView itemImage, backgroundImage, decrementButton, incrementButton;
        TextView scientificName, flowerName, price, quantity;
        public ViewHolder(@NonNull View itemView, final CartActivityAdaptor.OnItemClickListener listener) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImage);
            backgroundImage = itemView.findViewById(R.id.itemBackground);
            scientificName = itemView.findViewById(R.id.scientificName);
            flowerName = itemView.findViewById(R.id.flowerName);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantityLabel);

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

            decrementButton = itemView.findViewById(R.id.decrementButton);
            decrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && Integer.parseInt(quantity.getText().toString()) > 1) {
                        listener.onDecrementQuantity(position);
                    }
                }
            });

            incrementButton = itemView.findViewById(R.id.incrementButton);
            incrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && Integer.parseInt(quantity.getText().toString()) < 99) {
                        listener.onIncrementQuantity(position);
                    }
                }
            });
        }
    }
}
