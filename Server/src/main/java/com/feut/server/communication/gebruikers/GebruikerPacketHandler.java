package com.feut.server.communication.gebruikers;

import com.feut.server.db.facades.GebruikerFacade;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.*;
import com.feut.shared.models.Gebruiker;
import com.feut.shared.models.HuisGebruiker;

import java.sql.SQLException;

public class GebruikerPacketHandler implements IReceivePacket {
    @Override
    public void onReceivePacket(Client client, Packet packet) throws SQLException {
        switch (packet.getClass().getSimpleName()) {
            case "LoginRequest": {
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
            }
            case "RegisterRequest": {
                // Pakket ontvangen en casten naar RegisterRequest
                RegisterRequest registerRequest = (RegisterRequest) packet;
                // Gebruiker aanmaken, dit object gebruikt de db om queries te doen
                Gebruiker gebruiker = new Gebruiker();
                gebruiker.voornaam = registerRequest.firstName;
                gebruiker.achternaam = registerRequest.lastName;
                gebruiker.email = registerRequest.email;
                gebruiker.password = registerRequest.password;

                // Pakket voor response klaarmaken, en afhankelijk van exception in query waarde geven
                RegisterResponse registerResponse = new RegisterResponse();

                try {
                    GebruikerFacade.registreerGebruiker(gebruiker);
                    registerResponse.success = true;
                } catch (Exception e) {
                    registerResponse.success = false;
                }

                client.sendPacket(registerResponse);
                break;
            }
            case "CheckinPacket": {
                CheckinPacket checkinPacket = (CheckinPacket)packet;
                System.out.println("Checkin ontvangen: " + checkinPacket.chipId);
                break;
            }
            case "PresentRequest": {
                PresentRequest presentRequest = (PresentRequest) packet;
                PresentResponse presentResponse = new PresentResponse();

                try {
                    GebruikerFacade.toggleAanwezigheid(presentRequest.gebruikerId);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }


                try {
                    HuisGebruiker huisGebruiker = GebruikerFacade.getHuisGebruiker(presentRequest.gebruikerId);
                    if (huisGebruiker.aanwezig == 1) presentResponse.aanwezig = true;
                    else presentResponse.aanwezig = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                client.sendPacket(presentResponse);
                break;
            }
        }
    }
}