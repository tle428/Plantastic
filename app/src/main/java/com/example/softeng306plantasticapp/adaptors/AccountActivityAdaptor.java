package com.example.softeng306plantasticapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306plantasticapp.R;

import java.util.ArrayList;

import com.example.softeng306plantasticapp.entities.IItem;

public class AccountActivityAdaptor extends RecyclerView.Adapter<AccountActivityAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<IItem> items;

    public AccountActivityAdaptor(Context context, ArrayList<IItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_history_item_row, parent, false);

        return new AccountActivityAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;

        public ViewHolder(@NonNull View historyView) {
            super(historyView);

            // setting text for viewing
            itemName = historyView.findViewById(R.id.historyItemName);
        }
    }
}
