package com.app.feut.feut;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.feut.feut.connection.Connection;
import com.app.feut.feut.connection.SendPacketTask;
import com.feut.shared.connection.packets.RegisterAddresRequest;

public class NewAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Thread(Connection.getInstance()).start(); // TODO: Niet de beste plek hiervoor

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        TextView streetText = (TextView) findViewById(R.id.streetText);
        TextView streetNumberText = (TextView) findViewById(R.id.streetNumberText);
        TextView additionText = (TextView) findViewById(R.id.additionText);
        TextView zipCodeText = (TextView) findViewById(R.id.zipCodeText);
        TextView cityText = (TextView) findViewById(R.id.cityText);

        Button registerAddressButton = (Button) findViewById(R.id.registerAddressButton);
        registerAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterAddresRequest request = new RegisterAddresRequest();
                request.street = streetText.getText().toString();
                request.streetNumber = streetNumberText.getText().toString();
                request.addition = additionText.getText().toString();
                request.zipCode = zipCodeText.getText().toString();
                request.city = cityText.getText().toString();

                new SendPacketTask().execute(request);
                try {
                    this.wait(500);
                    // doe iets
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // doe nog iets..
                }

            }
        });





    }
}
