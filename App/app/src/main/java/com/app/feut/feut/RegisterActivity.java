package com.app.feut.feut;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feut.feut.connection.Connection;
import com.app.feut.feut.connection.SendPacketTask;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.Packet;
import com.feut.shared.connection.packets.RegisterRequest;
import com.feut.shared.connection.packets.RegisterResponse;

public class RegisterActivity extends AppCompatActivity {

    private TextView firstNameText, lastNameText, emailText, passwordText;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameText = (TextView) findViewById(R.id.firstNameText);
        lastNameText = (TextView) findViewById(R.id.lastNameText);
        emailText = (TextView) findViewById(R.id.emailText);
        passwordText = (TextView) findViewById(R.id.passwordText);


        // Get parameters from previous activity
        try {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                emailText.setText(b.getString("email"));
                passwordText.setText(b.getString("password"));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(handleRegisterClick);
        Connection.getInstance().registerPacketCallback(RegisterResponse.class, handleRegisterResponse, this);

        firstNameText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(firstNameText, InputMethodManager.SHOW_IMPLICIT);
    }

    IReceivePacket handleRegisterResponse = (Client client, Packet packet) -> {
        RegisterResponse response = (RegisterResponse)packet;
        if (response.success) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Je nieuwe account is geregistreerd, wil je nu een adres registreren?")
                    .setTitle("Account geregistreerd")
                    .setCancelable(false)
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!FeutApplication.getCurrentActivity().equals(NewAddressActivity.class)) {
                                Intent newAddresIntent = new Intent(context, NewAddressActivity.class);
                                newAddresIntent.putExtra("email", emailText.getText().toString()); // Misschien moet de RegisterRequest global en private worden?
                                startActivity(newAddresIntent);
                            }

                        }
                    })
                    .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!FeutApplication.getCurrentActivity().equals(MainActivity.class)) {
                                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(mainActivityIntent);
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else {
            Toast.makeText(context, "Dit account kan niet worden aangemaakt", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener handleRegisterClick = (View view) -> {
        // Kijk of emailadressen kloppen en/of wachtwoorden/voornaam en achternaam ingevuld zijn voordat we een verzoek sturen.

        String vnaam = firstNameText.getText().toString();
        String anaam = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String pass = passwordText.getText().toString();
        String message = "";

        if (vnaam.equals("")) {
            message = "Er is geen voornaam ingevuld";
        } else if (anaam.equals("")) {
            message = "Er is geen achternaam ingevuld";
        } else if (email.equals("")) {
            message = "Er is geen emailadres ingevuld";
        } else if (!Helper.isValidEmail(email)) {
            message = "Er is geen geldig emailadres ingevuld";
        } else if (pass.equals("")) {
            message = "Er is geen wachtwoord ingevuld";
        } else {
            RegisterRequest request = new RegisterRequest();

            request.firstName = vnaam;
            request.lastName = anaam;
            request.email = email;
            request.password = pass;

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
