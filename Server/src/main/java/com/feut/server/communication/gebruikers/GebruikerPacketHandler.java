package com.feut.server.communication.gebruikers;

import com.feut.server.db.facades.GebruikerFacade;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.*;
import com.feut.shared.models.Gebruiker;

import java.sql.SQLException;

public class GebruikerPacketHandler implements IReceivePacket {
    @Override
    public void onReceivePacket(Client client, Packet packet) throws SQLException {
        switch (packet.getClass().getSimpleName()) {
            case "LoginRequest":
                LoginRequest request = (LoginRequest) packet;

                Gebruiker gebruiker = GebruikerFacade.byEmailWachtwoord(request.email, request.password);
                LoginResponse loginResponse = new LoginResponse();

                if (gebruiker != null) {
                    loginResponse.success = true;
                    gebruiker.password = ""; // Het wachtwoord lijkt me geen goed idee om mee te sturen..
                    loginResponse.gebruiker = gebruiker;
                } else {
                    loginResponse.success = false;
                }

                client.sendPacket(loginResponse);
                break;
            case "RegisterRequest":
                // Kijken of account al bestaat in database, anders aanmaken.

                RegisterResponse registerResponse = new RegisterResponse();
                registerResponse.success = true;
                client.sendPacket(registerResponse);
                break;
        }
    }
}