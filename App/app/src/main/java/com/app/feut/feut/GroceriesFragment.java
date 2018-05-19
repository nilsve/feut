package com.app.feut.feut;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class GroceriesFragment extends Fragment {

    int position;
    private ListView groceryListView;

    public static Fragment getInstance() {
        Bundle bundle = new Bundle();
        // bundle.putInt("pos", position); Miss ooit.
        GroceriesFragment groceriesFragment = new GroceriesFragment();
        groceriesFragment.setArguments(bundle);
        return groceriesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // position = getArguments().getInt("pos"); Misschien hebben we dit ooit nodig.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groceries, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groceryListView = (ListView) view.findViewById(R.id.groceryListView);

        ArrayList<String> groceryContent = new ArrayList<>();
        groceryContent.add("Bananen");
        groceryContent.add("Appels");
        groceryContent.add("Kipfilet");
        groceryContent.add("Hummus");
        groceryContent.add("Wc papier");
        groceryContent.add("Chips");
        groceryContent.add("Cola");
        groceryContent.add("Sponsjes");

        ArrayAdapter<String> groceryListViewAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, groceryContent);
        groceryListView.setAdapter(groceryListViewAdapter);

    }
}
