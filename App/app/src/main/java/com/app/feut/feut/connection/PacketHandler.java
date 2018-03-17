package com.app.feut.feut.connection;

import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.Packet;

/**
 * Created by nils.van.eijk on 16-03-18.
 */

public class PacketHandler implements IReceivePacket {
    @Override
    public void onReceivePacket(Client client, Packet packet) {
        System.out.println("Pakketje! :D " + packet.Serialize());
    }
}
