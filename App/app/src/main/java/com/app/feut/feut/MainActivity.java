package com.app.feut.feut;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.feut.feut.connection.Connection;
import com.app.feut.feut.connection.SendPacketTask;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.Packet;
import com.feut.shared.connection.packets.UserRequest;
import com.feut.shared.connection.packets.UserRequestResponse;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView titleText;
    private ListView homeListView, groceryListView, chatListView;
    private ConstraintLayout groceryLayout, chatLayout, settingsLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    titleText.setText(R.string.title_home);
                    // Dit kan waarschijnlijk veel charmanter opgelost worden, maar het werkt.
                    chatLayout.setVisibility(View.INVISIBLE);
                    groceryLayout.setVisibility(View.INVISIBLE);
                    settingsLayout.setVisibility(View.INVISIBLE);
                    homeListView.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_chat:
                    titleText.setText(R.string.title_chat);
                    groceryLayout.setVisibility(View.INVISIBLE);
                    homeListView.setVisibility(View.INVISIBLE);
                    settingsLayout.setVisibility(View.INVISIBLE);
                    chatLayout.setVisibility(View.VISIBLE);

                    return true;
                case R.id.navigation_settings:
                    titleText.setText(R.string.title_settings);
                    groceryLayout.setVisibility(View.INVISIBLE);
                    homeListView.setVisibility(View.INVISIBLE);
                    chatLayout.setVisibility(View.INVISIBLE);
                    settingsLayout.setVisibility(View.VISIBLE);

                    return true;
                case R.id.navigation_grocery:
                    titleText.setText(R.string.title_groceries);
                    homeListView.setVisibility(View.INVISIBLE);
                    chatLayout.setVisibility(View.INVISIBLE);
                    settingsLayout.setVisibility(View.INVISIBLE);
                    groceryLayout.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get parameters from previous activity and based on given email request user information.
        try {
            Bundle b = getIntent().getExtras();
            if (b != null) {

                UserRequest request = new UserRequest();
                request.email = b.getString("email");
                Connection.getInstance().registerPacketCallback(UserRequestResponse.class, handleUserRequestResponse, this);
                new SendPacketTask().execute(request);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        titleText = (TextView) findViewById(R.id.titleText);
        homeListView = (ListView) findViewById(R.id.homeListView);

        HashMap<String, Boolean> presentPeople = new HashMap<String, Boolean>();
        presentPeople.put("Indy", true);
        presentPeople.put("Nils", true);
        presentPeople.put("Michelle", true);
        presentPeople.put("Laura", true);

        ArrayList<String> people = new ArrayList<>();
        for (String s : presentPeople.keySet()) {
            people.add(s);
        }
        ArrayAdapter<String> homeListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, people);
        homeListView.setAdapter(homeListViewAdapter);

        chatLayout = (ConstraintLayout) findViewById(R.id.chatLayout);
        chatListView = (ListView) findViewById(R.id.chatListView);

        ArrayList<String> chatContent = new ArrayList<>();
        chatContent.add("Indy:\tHallo jongens, leuk he een chat jonge jonge jonge :D");
        chatContent.add("Nils:\tJa helemaal top!!! :)");
        chatContent.add("Michelle:\tIn één woord geweldig!");
        chatContent.add("Laura:\tWauw ja hele mooie chat, superhandig!");

        ArrayAdapter<String> chatArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chatContent);
        chatListView.setAdapter(chatArrayAdapter);

        groceryLayout = (ConstraintLayout) findViewById(R.id.groceryLayout);
        groceryListView = (ListView) findViewById(R.id.groceryListView);

        ArrayList<String> groceryContent = new ArrayList<>();
        groceryContent.add("Bananen");
        groceryContent.add("Appels");
        groceryContent.add("Kipfilet");
        groceryContent.add("Hummus");
        groceryContent.add("Wc papier");
        groceryContent.add("Chips");
        groceryContent.add("Cola");
        groceryContent.add("Sponsjes");

        ArrayAdapter<String> groceryListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groceryContent);
        groceryListView.setAdapter(groceryListViewAdapter);

        settingsLayout = (ConstraintLayout) findViewById(R.id.settingsLayout);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    IReceivePacket handleUserRequestResponse = (Client client, Packet packet) -> {
        UserRequestResponse response = (UserRequestResponse) packet;
        setTitle(((UserRequestResponse) packet).street + " " + ((UserRequestResponse) packet).streetNumber);
        titleText.setText("Hallo " + ((UserRequestResponse) packet).firstName + " " + ((UserRequestResponse) packet).lastName);

    };


}
