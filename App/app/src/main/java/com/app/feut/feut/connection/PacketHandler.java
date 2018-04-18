package com.app.feut.feut.connection;

import com.app.feut.feut.LoginActivity;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.LoginResponse;
import com.feut.shared.connection.packets.Packet;

/**
 * Created by nils.van.eijk on 16-03-18.
 */

public class PacketHandler implements IReceivePacket {
    @Override
    public void onReceivePacket(Client client, Packet packet) {
        System.out.println(packet.Serialize());

        switch (packet.getClass().getSimpleName()) {
            case "LoginResponse":

                LoginResponse loginResponse = (LoginResponse) packet;
                System.out.println(loginResponse.success);
                if (loginResponse.success = true) { LoginActivity.setLoginValid(); }

                break;
                default:
                    System.out.println("Ongeldig pakket ontvangen!");
        }
    }
}
