package com.feut.server;

import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.Server;
import com.feut.shared.connection.packets.*;
import com.feut.shared.connection.packets.Packet;

public class PacketHandler implements IReceivePacket {

    public Server server;

    @Override
    public void onReceivePacket(Client client, Packet packet) {
        switch (packet.getClass().getSimpleName()) {
            case "LoginRequest":
                LoginResponse response = new LoginResponse();
                response.success = true;
                client.sendPacket(response);
                break;
        }
    }
}
