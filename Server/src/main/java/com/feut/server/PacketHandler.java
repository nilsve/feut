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
                registerResponse.succes = true;
                client.sendPacket(registerResponse);
                break;
            case "RegisterAddressRequest":
                // Kijken of adres al bestaat in database, anders aanmaken.

                RegisterAddressResponse registerAddressResponse = new RegisterAddressResponse();
                registerAddressResponse.succes = true;
                client.sendPacket(registerAddressResponse);
                break;
        }
    }
}
