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

                Gebruiker gebruiker1 = GebruikerFacade.byEmailWachtwoord(request.email, request.password);
                LoginResponse loginResponse = new LoginResponse();

                if (gebruiker1 != null) {
                    loginResponse.success = true;
                    gebruiker1.password = ""; // Het wachtwoord lijkt me geen goed idee om mee te sturen..
                    loginResponse.gebruiker = gebruiker1;
                } else {
                    loginResponse.success = false;
                }

                client.sendPacket(loginResponse);
                break;
            case "RegisterRequest":
                // Pakket ontvangen en casten naar RegisterRequest
                RegisterRequest registerRequest = (RegisterRequest) packet;
                // Gebruiker aanmaken, dit object gebruikt de db om queries te doen
                Gebruiker gebruiker2 = new Gebruiker();
                gebruiker2.voornaam = registerRequest.firstName;
                gebruiker2.achternaam = registerRequest.lastName;
                gebruiker2.email = registerRequest.email;
                gebruiker2.password = registerRequest.password;

                // Pakket voor respons klaarmaken, en afhankelijk van exception in query waarde geven
                RegisterResponse registerResponse = new RegisterResponse();

                try {
                    GebruikerFacade.registreerGebruiker(gebruiker2);
                    registerResponse.success = true;
                } catch (Exception e) {
                    registerResponse.success = false;
                }

                client.sendPacket(registerResponse);
                break;
        }
    }
}