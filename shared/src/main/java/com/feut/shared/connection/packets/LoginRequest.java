package com.feut.shared.connection.packets;

import com.feut.shared.models.Adres;

import java.util.ArrayList;
import java.util.List;

public class LoginRequest extends Packet {
    public String email;
    public String password;
}
