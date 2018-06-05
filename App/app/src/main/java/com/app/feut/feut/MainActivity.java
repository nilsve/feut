package com.app.feut.feut;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.app.feut.feut.connection.Connection;
import com.app.feut.feut.connection.SendPacketTask;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.Packet;
import com.feut.shared.connection.packets.PresentRequest;
import com.feut.shared.connection.packets.PresentResponse;
import com.feut.shared.models.Gebruiker;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Switch aSwitch;
    private Gebruiker gebruiker = null;
    private boolean present, updated = false;

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Registreer een packetcallback zodat we iets kunnen doen zodra we antwoord krijgen op het pakket wat we hierna gaan sturen
        Connection.getInstance().registerPacketCallback(PresentResponse.class, handlePresentResponse, this);

        // Verkrijg het gebruikers id om een verzoek tot aanwezigheid te doen.
        try {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                gebruiker = (Gebruiker)Gebruiker.deserializeJson(b.getString("gebruiker"), Gebruiker.class);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        aSwitch = (Switch) findViewById(R.id.presentSwitch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean aanwezig) {
                // Elke keer als de waarde van de switch veranderd stuurt hij opnieuw een pakket met het gebruikers id.
                PresentRequest presentRequest = new PresentRequest();
                presentRequest.aanwezig = aanwezig;
                new SendPacketTask().execute(presentRequest);
            }
        });
    }

    IReceivePacket handlePresentResponse = (Client client, Packet packet) -> {
        // Zodra we een pakket krijgen casten we hem zodat we variabelen kunnen gebruiken.
        PresentResponse presentResponse = (PresentResponse) packet;
        present = presentResponse.aanwezig;

        // Dit zorgt ervoor dat de switch in de app wordt geupdate naar de huidige staat op de server
        if (!updated) {
            aSwitch.setChecked(present);
            updated = true;
        }
    };

    protected void onResume() {
        super.onResume();
        FeutApplication.setCurrentActivity(this);
    }
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    private void clearReferences(){
        Activity currActivity = FeutApplication.getCurrentActivity();
        if (this.equals(currActivity))
            FeutApplication.setCurrentActivity(null);
    }

}