package com.app.feut.feut;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.feut.feut.connection.Connection;
import com.app.feut.feut.connection.SendPacketTask;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.LoginResponse;
import com.feut.shared.connection.packets.Packet;
import com.feut.shared.connection.packets.RegisterRequest;
import com.feut.shared.connection.packets.RegisterResponse;

public class RegisterActivity extends AppCompatActivity {

    TextView emailText;
    TextView usernameText;
    TextView passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = (TextView) findViewById(R.id.emailText);
        usernameText = (TextView) findViewById(R.id.usernameText);
        passwordText = (TextView) findViewById(R.id.passwordText);

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(handleRegisterClick);

        Connection.getInstance().registerPacketCallback(RegisterResponse.class, handleRegisterResponse);
    }

    IReceivePacket handleRegisterResponse = new IReceivePacket() {
        @Override
        public void onReceivePacket(Client client, Packet packet) {
            LoginResponse response = (LoginResponse)packet;

            if (response.success) {
                // Naar volgende activitiy ofzo..?
            } else {
                // Error?
            }
        }
    };

    View.OnClickListener handleRegisterClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            RegisterRequest request = new RegisterRequest();
            request.email = emailText.getText().toString();
            request.username = usernameText.getText().toString();
            request.password = passwordText.getText().toString();

            new SendPacketTask().execute(request);
        }
    };
}
