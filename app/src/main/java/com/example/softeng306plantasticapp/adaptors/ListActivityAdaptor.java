package com.example.softeng306plantasticapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306plantasticapp.R;

import java.util.ArrayList;

import com.example.softeng306plantasticapp.entities.IItem;

public class ListActivityAdaptor extends RecyclerView.Adapter<ListActivityAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<IItem> items;
    private OnItemClickListener listener;

    public ListActivityAdaptor(Context context, ArrayList<IItem> items) {
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public ListActivityAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout here (give it the look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item_row, parent, false);

        return new ListActivityAdaptor.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListActivityAdaptor.ViewHolder holder, int position) {
        // assign values to the row layout
        holder.itemImage.setImageResource((int) items.get(position).getListImage());
        holder.itemBackground.setImageResource((int) items.get(position).getBackgroundImage());
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
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // gets view from row layout xml

        ImageView itemImage, itemBackground;
        TextView scientificName, flowerName, price;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImage);
            itemBackground = itemView.findViewById(R.id.itemBackground);
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
        }
    }
}
