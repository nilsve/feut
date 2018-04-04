package com.app.feut.feut.connection;

import android.os.AsyncTask;

import com.feut.shared.connection.packets.Packet;

/**
 * Created by nilsvaneijk on 17-03-18.
 */

public class SendPacketTask extends AsyncTask<Packet, Void, Void> {
    @Override
    protected Void doInBackground(Packet... packets) {
        Connection connection = Connection.getInstance();

        for (Packet packet : packets) {
            System.out.println(packet.toString());
            connection.client.sendPacket(packet);
        }

        return null;
    }
}
