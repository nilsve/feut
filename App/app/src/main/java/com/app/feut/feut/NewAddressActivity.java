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

        // Get parameters from previous activity
        try {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                email = b.getString("email");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }


        Button registerAddressButton = (Button) findViewById(R.id.registerAddressButton);
        registerAddressButton.setOnClickListener(handleRegisterAddressClick);

        Connection.getInstance().registerPacketCallback(RegisterAddressResponse.class, handleRegisterResponse, this);
    }

    IReceivePacket handleRegisterResponse = new IReceivePacket() {
        @Override
        public void onReceivePacket(Client client, Packet packet) {
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
        }
    };

    View.OnClickListener handleRegisterAddressClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RegisterAddressRequest request = new RegisterAddressRequest();
            request.street = streetText.getText().toString();
            request.streetNumber = streetNumberText.getText().toString();
            request.addition = additionText.getText().toString();
            request.zipCode = zipCodeText.getText().toString();
            request.city = cityText.getText().toString();

            new SendPacketTask().execute(request);
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
