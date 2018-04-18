package com.app.feut.feut;

import android.content.Intent;
import android.os.Bundle;
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
    public static boolean loginValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginValid = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = (TextView) findViewById(R.id.usernameText);
        passwordText = (TextView) findViewById(R.id.passwordText);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view) {
                LoginRequest request = new LoginRequest();
                request.username = usernameText.getText().toString();
                request.password = passwordText.getText().toString();

                new SendPacketTask().execute(request);
                try {
                    this.wait(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (loginValid) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }


            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
        }

    public static void setLoginValid() { loginValid = true; }

}
