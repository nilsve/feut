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
                // Uitsluiten of wachtwoord klopt en hoort bij bestaande gebruikersnaam.

                LoginResponse loginResponse = new LoginResponse();
                loginResponse.success = true;
                client.sendPacket(loginResponse);
                break;
            case "RegisterRequest":
                // Kijken of account al bestaat in database, anders aanmaken.

                RegisterResponse registerResponse = new RegisterResponse();
                registerResponse.success = true;
                client.sendPacket(registerResponse);
                break;
            case "RegisterAddressRequest":
                // Kijken of adres al bestaat in database, anders aanmaken.

                RegisterAddressResponse registerAddressResponse = new RegisterAddressResponse();
                registerAddressResponse.success = true;
                client.sendPacket(registerAddressResponse);
                break;
            case "UserRequest":
                // Gebruiker opzoeken en data over gebruiker terugsturen
                // Als gebruiker een adres heeft, adres misschien ook meesturen?

                UserRequestResponse userRequestResponse = new UserRequestResponse();
                userRequestResponse.firstName = "Indy";
                userRequestResponse.lastName = "Maat";
                userRequestResponse.street = "Adriaan van Bergenstraat";
                userRequestResponse.streetNumber = "129";
                client.sendPacket(userRequestResponse);
                break;
        }
    }
}
