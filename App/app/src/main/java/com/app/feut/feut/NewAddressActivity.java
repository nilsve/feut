package com.app.feut.feut;

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
import com.feut.shared.connection.packets.RegisterResponse;

public class NewAddressActivity extends AppCompatActivity {
    private Context context = this;

    TextView streetText;
    TextView streetNumberText;
    TextView additionText;
    TextView zipCodeText;
    TextView cityText;

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

    IReceivePacket handleRegisterResponse = new IReceivePacket() {
        @Override
        public void onReceivePacket(Client client, Packet packet) {
            RegisterAddressResponse response = (RegisterAddressResponse)packet;

            if (response.success) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Je bent nu de beheerder van dit adres. Wil je direct iemand uitnodigen?")
                        .setTitle("Adres aangemaakt")
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Intent InviteActivity = new Intent(getApplicationContext(), InviteActivity.class);
                                // startActivity(InviteActivity);

                            }
                        })
                        .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(mainActivityIntent);
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                Toast.makeText(context, "Dit adres kan niet worden aangemaakt", Toast.LENGTH_SHORT);
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
}
