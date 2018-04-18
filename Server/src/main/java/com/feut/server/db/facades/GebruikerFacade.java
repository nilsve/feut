package com.feut.server.db.facades;

import com.feut.shared.models.Gebruiker;

public class GebruikerFacade extends Facade {
    public static Gebruiker byEmailWachtwoord(String username, String password) {
        return (Gebruiker)Gebruiker.Deserialize(querySingle("SELECT * FROM gebruiker WHERE email = ? AND password = ?", new String[]{username, password}), Gebruiker.class);
    }
}
