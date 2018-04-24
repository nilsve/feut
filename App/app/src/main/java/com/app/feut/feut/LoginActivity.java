package com.app.feut.feut;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    IReceivePacket handleLoginResponse = new IReceivePacket() {
        @Override
        public void onReceivePacket(Client client, Packet packet) {
            LoginResponse response = (LoginResponse)packet;

            if (response.success) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }
    };

    View.OnClickListener handleRegisterClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tempEmail = "";
            String tempPassword = "";

            tempEmail = emailText.getText().toString();
            tempPassword = passwordText.getText().toString();

            Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            registerIntent.putExtra("email", tempEmail).putExtra("password", tempPassword);
            startActivity(registerIntent);
        }
    };

    View.OnClickListener handleLoginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LoginRequest request = new LoginRequest();
            request.email = emailText.getText().toString();
            request.password = passwordText.getText().toString();

            new SendPacketTask().execute(request);
        }
    };
}
