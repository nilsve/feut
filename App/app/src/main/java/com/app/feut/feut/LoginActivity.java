package com.app.feut.feut;

import android.app.ActivityManager;
import android.content.ComponentName;
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

import java.util.List;

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
        passwordText = (TextView) findViewById(R.id.passwordTitleText);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(handleLoginClick);

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(handleRegisterClick);

        // Register packet callback
        Connection.getInstance().registerPacketCallback(LoginResponse.class, handleLoginResponse, this);
    }

    IReceivePacket handleLoginResponse = (Client client, Packet packet) -> {

        LoginResponse response = (LoginResponse)packet;
        String tempEmail = emailText.getText().toString();


        if (response.success) {
            if (!isForeground("MainActivity")) {
                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                startActivity(mainActivityIntent);
            }
        } else {
            Toast.makeText(this, "Combinatie van gebruikersnaam en wachtwoord onjuist.", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener handleRegisterClick = (View view) -> {
        String tempEmail = emailText.getText().toString();
        String tempPassword = passwordText.getText().toString();

        if (!isForeground("RegisterActivity")) {
            Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(registerIntent);
        }
    };


    View.OnClickListener handleLoginClick = (View view) -> {
        // Check for email regex
        if (emailText.getText().toString().matches(".+@.+\\..+")) {
                LoginRequest request = new LoginRequest();

                request.email = emailText.getText().toString();
                request.password = passwordText.getText().toString();

                new SendPacketTask().execute(request);
        } else {
            Toast.makeText(this, "Dat is geen geldig emailadres", Toast.LENGTH_SHORT).show();
        }
    };

    public boolean isForeground(String PackageName){
        // Check of het scherm wat je wilt starten niet al draait.
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > task = manager.getRunningTasks(1);
        ComponentName componentInfo = task.get(0).topActivity;
        if(componentInfo.getPackageName().equals(PackageName)) return true;
        return false;
    }
}
