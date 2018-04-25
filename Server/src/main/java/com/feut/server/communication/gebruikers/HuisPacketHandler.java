package com.feut.server.communication.gebruikers;

import com.feut.server.db.facades.GebruikerFacade;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.*;
import com.feut.shared.models.Gebruiker;

import java.sql.SQLException;

public class HuisPacketHandler implements IReceivePacket {
    @Override
    public void onReceivePacket(Client client, Packet packet) throws SQLException {
        switch (packet.getClass().getSimpleName()) {
            case "RegisterAddressRequest":
                // Kijken of adres al bestaat in database, anders aanmaken.

                RegisterAddressResponse registerAddressResponse = new RegisterAddressResponse();
                registerAddressResponse.success = true;
                client.sendPacket(registerAddressResponse);
                break;
        }
    }
}
