package com.app.feut.feut;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.feut.feut.connection.SendPacketTask;
import com.feut.shared.connection.packets.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView emailText = (TextView) findViewById(R.id.emailText);
        TextView usernameText = (TextView) findViewById(R.id.usernameText);
        TextView passwordText = (TextView) findViewById(R.id.passwordText);

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterRequest request = new RegisterRequest();
                request.email = emailText.getText().toString();
                request.username = usernameText.getText().toString();
                request.password = passwordText.getText().toString();

                new SendPacketTask().execute(request);
                try {
                    this.wait(500);
                    // bevestiging of afwijzing registratie
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
