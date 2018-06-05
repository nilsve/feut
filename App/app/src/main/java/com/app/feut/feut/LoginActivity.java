package com.app.feut.feut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feut.feut.connection.Connection;
import com.app.feut.feut.connection.SendPacketTask;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.LoginRequest;
import com.feut.shared.connection.packets.LoginResponse;
import com.feut.shared.connection.packets.Packet;

/**
 * Created by nils.van.eijk on 16-03-18.
 */

public class LoginActivity extends AppCompatActivity {
    private TextView emailText;
    private TextView passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (TextView) findViewById(R.id.emailText);
        passwordText = (TextView) findViewById(R.id.passwordText);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(handleLoginClick);

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(handleRegisterClick);

        // Register packet callback
        Connection.getInstance().registerPacketCallback(LoginResponse.class, handleLoginResponse, this);
    }

    IReceivePacket handleLoginResponse = (Client client, Packet packet) -> {
        // Cast response zodat we variabelen kunnen gebruiken.
        LoginResponse response = (LoginResponse)packet;

        // Als we een succesvolle combinatie hebben ingevoerd gaan we MainActivity Openen en geven we gegevens mee.
        // We kunnen helaas geen custom Objects meesturen..
        if (response.success) {
            if (!FeutApplication.getCurrentActivity().equals(MainActivity.class)) {
                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                mainActivityIntent.putExtra("gebruiker", response.gebruiker.serializeJson());
                startActivity(mainActivityIntent);
            }
        } else {
            Toast.makeText(this, "Combinatie van gebruikersnaam en wachtwoord onjuist.", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener handleRegisterClick = (View view) -> {
        String tempEmail = emailText.getText().toString();
        String tempPassword = passwordText.getText().toString();

        if (!FeutApplication.getCurrentActivity().equals(RegisterActivity.class)) {
            Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            registerIntent.putExtra("email", tempEmail);
            registerIntent.putExtra("password", tempPassword);
            startActivity(registerIntent);
        }
    };


    View.OnClickListener handleLoginClick = (View view) -> {
        // Kijk of emailadressen kloppen en/of wachtwoorden ingevuld zijn voordat we een verzoek sturen.
        String email = emailText.getText().toString();
        String pass = passwordText.getText().toString();
        String message = "";

        if (email.equals("")){
            message = "Er is geen emailadres ingevuld";
        } else if (!Helper.isValidEmail(email)){
            message = "Er is geen geldig emailadres ingevuld";
        } else if (pass.equals("")){
            message = "Er is geen wachtwoord ingevuld";
        } else {
            LoginRequest request = new LoginRequest();

            request.email = emailText.getText().toString();
            request.password = passwordText.getText().toString();

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
