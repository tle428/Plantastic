package com.example.softeng306plantasticapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306plantasticapp.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import com.example.softeng306plantasticapp.entities.IItem;

public class MainActivityAdaptor extends RecyclerView.Adapter<MainActivityAdaptor.ViewHolder> {

    private Context context;
    private ArrayList<IItem> featuredItems;

    private OnItemClickListener listener;

    public MainActivityAdaptor(Context context, ArrayList<IItem> featuredItems) {
        this.context = context;
        this.featuredItems = featuredItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_featured_item_card, parent, false);

        return new MainActivityAdaptor.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.scientificName.setText(featuredItems.get(position).getScientificName());
        holder.plantName.setText(featuredItems.get(position).getName());
        holder.featuredItemImage.setImageResource(featuredItems.get(position).getListImage());

        System.out.println("current item: " + featuredItems.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return featuredItems.size();
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MainActivityAdaptor.OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView featuredItemImage;
        TextView scientificName, plantName;

        public ViewHolder(@NonNull View featuredItemView, final OnItemClickListener listener) {
            super(featuredItemView);
            featuredItemImage = featuredItemView.findViewById(R.id.featurePlantImage);
            scientificName = featuredItemView.findViewById(R.id.featuredPlantScientificName);
            plantName = featuredItemView.findViewById(R.id.featuredPlantName);

            // on click listener for the card
            featuredItemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
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
