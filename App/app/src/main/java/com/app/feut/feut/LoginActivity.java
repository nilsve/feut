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
    private TextView emailText;
    private TextView passwordText;
    public static boolean loginValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginValid = false;
        new Thread(Connection.getInstance()).start(); // TODO: Niet de beste plek hiervoor

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (TextView) findViewById(R.id.emailText);
        passwordText = (TextView) findViewById(R.id.passwordText);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view) {
                LoginRequest request = new LoginRequest();
                request.username = emailText.getText().toString();
                request.password = passwordText.getText().toString();

                new SendPacketTask().execute(request);
                try {
                    this.wait(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (loginValid) {
                        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivityIntent);
                    }
                }


            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempEmail = "";
                String tempPassword = "";

                if (emailText.getText().toString().length() > 0 || passwordText.getText().toString().length() > 0) {
                    tempEmail = emailText.getText().toString();
                    tempPassword = passwordText.getText().toString();
                }

                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                registerIntent.putExtra("email", tempEmail).putExtra("password", tempPassword);
                startActivity(registerIntent);
            }
        });
        }

    public static void setLoginValid() { loginValid = true; }

}
