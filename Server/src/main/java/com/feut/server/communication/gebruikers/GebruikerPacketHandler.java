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
                // Pakket welke ontvangen wordt casten, en de nieuwe direct klaarzetten voor gebruik.
                PresentRequest presentRequest = (PresentRequest) packet;
                PresentResponse presentResponse = new PresentResponse();

                // We spreken de gebruikersfacade aan om vervolgens de functie toggleAanwezigheid te gebruiken. 
                // Deze functie heeft het userid nodig van de gebruiker, welke wordt vergaard tijdens inloggen. 
                try {
                    GebruikerFacade.toggleAanwezigheid(Integer.parseInt(presentRequest.gebruiker_id));
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

                // De gebruikersfacade heeft een functie welke een volledige Huisgebruiker teruggeeft
                // TODO: Wellicht kijken of deze functie alleen de aanwezigheid teruggeeft, 
                // Echter misschien is het handig om dat hele huisGebuiker object later in het project nog te gebruiken.
                try {
                    HuisGebruiker huisGebruiker = GebruikerFacade.getHuisGebruiker(Integer.parseInt(presentRequest.gebruiker_id));
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
