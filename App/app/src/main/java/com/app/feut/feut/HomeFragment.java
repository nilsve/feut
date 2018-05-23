package com.app.feut.feut;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    int position;
    private ListView homeListView;
    private static TextView titleText, homePresentText;
    private String firstname, lastname, street, streetnumber;

    public static Fragment getInstance() {
        Bundle bundle = new Bundle();
        // bundle.putInt("pos", position); Miss ooit.
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // position = getArguments().getInt("pos"); Misschien hebben we dit ooit nodig.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        titleText = (TextView) view.findViewById(R.id.titleText);
        homePresentText = (TextView) view.findViewById(R.id.homePresentText);
        homeListView = (ListView) view.findViewById(R.id.homeListView);

        // Placeholder data for ListView. Hashmap moet en beetje de db voorstellen en de ArrayList hebben we nodig voor de ArrayAdapter voor de Listview.
        HashMap<String, Boolean> presentPeople = new HashMap<String, Boolean>();
        presentPeople.put("Indy", true);
        presentPeople.put("Nils", true);
        presentPeople.put("Michelle", true);
        presentPeople.put("Laura", true);

        // Deze hebben we nodig, de ArrayAdapter kan geen Hashmap lezen.
        ArrayList<String> people = new ArrayList<>();
        for (String s : presentPeople.keySet()) {
            people.add(s);
        }
        // Hier wordt de listview uiteindelijk gevuld met de data.
        ArrayAdapter<String> homeListViewAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, people);
        homeListView.setAdapter(homeListViewAdapter);

    }

}
