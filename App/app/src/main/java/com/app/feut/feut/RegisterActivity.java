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
import com.feut.shared.connection.packets.LoginResponse;
import com.feut.shared.connection.packets.Packet;
import com.feut.shared.connection.packets.RegisterRequest;
import com.feut.shared.connection.packets.RegisterResponse;

public class RegisterActivity extends AppCompatActivity {

    private TextView emailText;
    private TextView usernameText;
    private TextView passwordText;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = (TextView) findViewById(R.id.emailText);
        usernameText = (TextView) findViewById(R.id.usernameText);
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
    }

    IReceivePacket handleRegisterResponse = new IReceivePacket() {
        @Override
        public void onReceivePacket(Client client, Packet packet) {
            RegisterResponse response = (RegisterResponse)packet;
            if (response.success) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Account is aangemaakt, wil je nu een adres aanmaken?")
                        .setTitle("Account aangemaakt")
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent newAddresIntent = new Intent(context, NewAddressActivity.class);
                                startActivity(newAddresIntent);

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
                Toast.makeText(context, "Dit account kan niet worden aangemaakt", Toast.LENGTH_SHORT);
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
