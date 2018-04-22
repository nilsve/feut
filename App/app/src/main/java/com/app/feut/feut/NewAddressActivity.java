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

import com.app.feut.feut.connection.SendPacketTask;
import com.feut.shared.connection.packets.RegisterAddressRequest;

public class NewAddressActivity extends AppCompatActivity {
    private static boolean addressRegisterValid;
    private Context context = this;

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        addressRegisterValid = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        TextView streetText = (TextView) findViewById(R.id.streetText);
        TextView streetNumberText = (TextView) findViewById(R.id.streetNumberText);
        TextView additionText = (TextView) findViewById(R.id.additionText);
        TextView zipCodeText = (TextView) findViewById(R.id.zipCodeText);
        TextView cityText = (TextView) findViewById(R.id.cityText);

        Button registerAddressButton = (Button) findViewById(R.id.registerAddressButton);
        registerAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view) {
                RegisterAddressRequest request = new RegisterAddressRequest();
                request.street = streetText.getText().toString();
                request.streetNumber = streetNumberText.getText().toString();
                request.addition = additionText.getText().toString();
                request.zipCode = zipCodeText.getText().toString();
                request.city = cityText.getText().toString();

                new SendPacketTask().execute(request);
                try {
                    this.wait(500);
                    // doe iets
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("In OnClick: " + addressRegisterValid);

                    if (addressRegisterValid) {
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

            }
        });

    }

    public static void setAddressRegisterValid() { addressRegisterValid = true; }
}
