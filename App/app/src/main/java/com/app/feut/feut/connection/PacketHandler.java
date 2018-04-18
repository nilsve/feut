package com.app.feut.feut.connection;

import com.app.feut.feut.LoginActivity;
import com.app.feut.feut.NewAddressActivity;
import com.app.feut.feut.RegisterActivity;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.LoginResponse;
import com.feut.shared.connection.packets.Packet;
import com.feut.shared.connection.packets.RegisterAddressResponse;
import com.feut.shared.connection.packets.RegisterResponse;

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
            case "RegisterResponse":

                RegisterResponse registerResponse = (RegisterResponse) packet;
                System.out.println(registerResponse.succes);
                if (registerResponse.succes = true) {  RegisterActivity.setRegisterValid(); };

                break;
            case "RegisterAddressResponse":

                RegisterAddressResponse registerAddressResponse = (RegisterAddressResponse) packet;
                System.out.println("In PacketHandler: " + registerAddressResponse.succes);
                if (registerAddressResponse.succes = true) {  NewAddressActivity.setAddressRegisterValid(); };

                break;
        }
    }
}
