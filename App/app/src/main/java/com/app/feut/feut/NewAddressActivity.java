package com.app.feut.feut;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feut.feut.connection.Connection;
import com.app.feut.feut.connection.SendPacketTask;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.Packet;
import com.feut.shared.connection.packets.RegisterAddressRequest;
import com.feut.shared.connection.packets.RegisterAddressResponse;

public class NewAddressActivity extends AppCompatActivity {
    private Context context = this;
    private TextView streetText, streetNumberText, additionText, zipCodeText, cityText;
    private String email;

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        streetText = findViewById(R.id.streetText);
        streetNumberText = findViewById(R.id.streetNumberText);
        additionText = findViewById(R.id.additionText);
        zipCodeText = findViewById(R.id.zipCodeText);
        cityText = findViewById(R.id.cityText);

        Button registerAddressButton = (Button) findViewById(R.id.registerAddressButton);
        registerAddressButton.setOnClickListener(handleRegisterAddressClick);

        Connection.getInstance().registerPacketCallback(RegisterAddressResponse.class, handleRegisterResponse, this);
    }

    IReceivePacket handleRegisterResponse = (Client client, Packet packet) -> {
            RegisterAddressResponse response = (RegisterAddressResponse)packet;

            if (response.success) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Je bent nu de beheerder van dit adres. Je kunt in de instellingen mensen uitnodigen.")
                        .setTitle("Adres aangemaakt")
                        .setCancelable(false)
                        .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (!FeutApplication.getCurrentActivity().equals(MainActivity.class)) {
                                    Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    mainActivityIntent.putExtra("email", email);
                                    startActivity(mainActivityIntent);
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                Toast.makeText(context, "Dit adres kan niet worden aangemaakt", Toast.LENGTH_SHORT).show();
            }
    };

    View.OnClickListener handleRegisterAddressClick = (View view) -> {
        String street = streetText.getText().toString();
        String num = streetNumberText.getText().toString();
        String add = additionText.getText().toString();
        String zip = zipCodeText.getText().toString();
        String city = cityText.getText().toString();
        String message = "";

        if (street.equals("")) {
            message = "Er is geen straatnaam ingevoerd";
        } else if (num.equals("")) {
            message = "Er is geen huisnummer ingevoerd";
        } else if (zip.equals("")) {
            message = "Er is geen postcode ingevoerd";
        } else if (!zip.matches("(\\d{4})([a-zA-Z]{2})")) {
            message = "Er is geen geldige postcode ingevoerd";
        } else if (city.equals("")) {
            message = "Er is geen straatnaam ingevoerd";
        } else {
            RegisterAddressRequest request = new RegisterAddressRequest();

            request.street = street;
            request.streetNumber = num;
            request.addition = add;
            request.zipCode = zip;
            request.city = city;

            new SendPacketTask().execute(request);
        }

        if (!message.equals("")) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
