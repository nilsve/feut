package com.app.feut.feut;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.feut.feut.connection.Connection;
import com.app.feut.feut.connection.SendPacketTask;
import com.feut.shared.connection.packets.LoginRequest;

/**
 * Created by nils.van.eijk on 16-03-18.
 */

public class LoginActivity extends AppCompatActivity {
    private TextView usernameText;
    private TextView passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        new Thread(Connection.getInstance()).start(); // TODO: Niet de beste plek hiervoor

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = (TextView) findViewById(R.id.usernameText);
        passwordText = (TextView) findViewById(R.id.passwordText);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginRequest request = new LoginRequest();
                request.username = usernameText.getText().toString();
                request.password = passwordText.getText().toString();

                new SendPacketTask().execute(request);
            }
        });
        }
}
