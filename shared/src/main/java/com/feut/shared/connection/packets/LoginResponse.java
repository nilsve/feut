package com.feut.shared.connection.packets;

import com.feut.shared.models.Gebruiker;

public class LoginResponse extends Packet {
    public boolean success;
    public Gebruiker gebruiker;
}
