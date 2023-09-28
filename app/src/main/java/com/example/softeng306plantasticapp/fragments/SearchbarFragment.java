package com.example.softeng306plantasticapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.softeng306plantasticapp.R;
import com.example.softeng306plantasticapp.activities.SearchActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchbarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchbarFragment extends Fragment {

    private TextInputEditText editTextSearchMaterial;
    private TextInputLayout searchInputLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_searchbar, container, false);
        editTextSearchMaterial = rootView.findViewById(R.id.editText_search_material);
        searchInputLayout = rootView.findViewById(R.id.search_input_layout);

        searchInputLayout.setEndIconOnClickListener(v -> performSearch());

        return rootView;
    }

    private void performSearch() {
        String searchTerm = editTextSearchMaterial.getText().toString().trim();
        Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
        searchIntent.putExtra("SEARCH_TERM", searchTerm);
        startActivity(searchIntent);
    }
}