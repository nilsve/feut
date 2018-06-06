package com.feut.server.communication.handlers;

import com.feut.server.communication.GebruikerStateManager;
import com.feut.server.db.facades.GebruikerFacade;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.*;
import com.feut.shared.models.Gebruiker;
import com.feut.shared.models.HuisGebruiker;

public class GebruikerPacketHandler implements IReceivePacket {

    GebruikerStateManager gebruikerStateManager = GebruikerStateManager.getInstance();

    @Override
    public void onReceivePacket(Client client, Packet packet) throws Exception {
        switch (packet.getClass().getSimpleName()) {
            case "LoginRequest": {
                LoginRequest request = (LoginRequest) packet;

                Gebruiker gebruiker = GebruikerFacade.byEmailWachtwoord(request.email, request.password);
                LoginResponse loginResponse = new LoginResponse();

                if (gebruiker != null) {
                    loginResponse.success = true;
                    gebruiker.password = ""; // Het wachtwoord lijkt me geen goed idee om mee te sturen..
                    loginResponse.gebruiker = gebruiker;

                    // In de ingelogde gebruiker lijst zetten
                    gebruikerStateManager.handleGebruikerLogin(client, gebruiker);

                    // Direct een huis selecteren. Dit kan evt ooit nog via een scherm op de app gaan
                    HuisGebruiker huisGebruiker = GebruikerFacade.getHuisGebruiker(gebruiker.gebruikerId);
                    gebruikerStateManager.handleGebruikerSelectHuis(client, huisGebruiker);

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

                // We spreken de gebruikersfacade aan om vervolgens de functie toggleAanwezigheid te gebruiken.
                // Deze functie heeft het userid nodig van de gebruiker, welke wordt vergaard tijdens inloggen.

                Gebruiker gebruiker = gebruikerStateManager.getGebruiker(client);
                HuisGebruiker huisGebruiker = gebruikerStateManager.getHuisGebruiker(client);

                try {
                    GebruikerFacade.toggleAanwezigheid(gebruiker.gebruikerId, huisGebruiker.huisId, presentRequest.aanwezig);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

                // TODO: Broadcast naar iedereen?
                PresentResponse presentResponse = new PresentResponse();
                presentResponse.aanwezig = presentRequest.aanwezig;
                presentResponse.gebruikerId = gebruiker.gebruikerId;
                client.sendPacket(presentResponse);
                break;
            }
        }
    }
}
