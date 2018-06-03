package com.feut.server.communication;

import com.feut.server.communication.gebruikers.GebruikerPacketHandler;
import com.feut.server.communication.gebruikers.HuisPacketHandler;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.Server;
import com.feut.shared.connection.packets.Packet;

import java.sql.SQLException;

public class MainPacketHandler implements IReceivePacket {

    public Server server;

    GebruikerPacketHandler gebruikerHandler = new GebruikerPacketHandler();
    HuisPacketHandler huisHandler = new HuisPacketHandler();

    @Override
    public void onReceivePacket(Client client, Packet packet) throws SQLException {
        switch (packet.getClass().getSimpleName()) {
            case "PresentRequest":
            case "CheckinPacket":
            case "LoginRequest":
            case "RegisterRequest":
                gebruikerHandler.onReceivePacket(client, packet);
                break;
            case "RegisterAddressRequest":
                huisHandler.onReceivePacket(client, packet);
                break;
        }
    }
}
