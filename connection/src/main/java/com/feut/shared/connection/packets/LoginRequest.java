package com.feut.shared.connection.packets;

import com.feut.shared.models.Adres;

import java.util.ArrayList;
import java.util.List;

public class LoginRequest extends Packet {
    String username;
    String password;

    List<Adres> adressen = new ArrayList<Adres>();

    public LoginRequest() {
        adressen.add(new Adres("Veghel city"));
    }
}
