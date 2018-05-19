package com.app.feut.feut;

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
        passwordText = (TextView) findViewById(R.id.passwordTitleText);


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
            builder.setMessage("Account is aangemaakt, wil je nu een adres aanmaken?")
                    .setTitle("Account aangemaakt")
                    .setCancelable(false)
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent newAddresIntent = new Intent(context, NewAddressActivity.class);
                            newAddresIntent.putExtra("email", emailText.getText().toString() ); // Misschien moet de RegisterRequest global en private worden?
                            startActivity(newAddresIntent);

                        }
                    })
                    .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                            mainActivityIntent.putExtra("email", emailText.getText().toString() ); // Misschien moet de RegisterRequest global en private worden?
                            startActivity(mainActivityIntent);
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else {
            Toast.makeText(context, "Dit account kan niet worden aangemaakt", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener handleRegisterClick = (View view) -> {
        RegisterRequest request = new RegisterRequest();
        request.firstName = firstNameText.getText().toString();
        request.lastName = lastNameText.getText().toString();
        request.email = emailText.getText().toString();
        request.password = passwordText.getText().toString();

        new SendPacketTask().execute(request);
    };
}
