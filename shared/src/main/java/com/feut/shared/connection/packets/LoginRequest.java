package com.feut.shared.connection.packets;

import com.feut.shared.models.Adres;

import java.util.ArrayList;
import java.util.List;

public class LoginRequest extends Packet {
    public String username;
    public String password;

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", packetType='" + packetType + '\'' +
                '}';
    }
}
