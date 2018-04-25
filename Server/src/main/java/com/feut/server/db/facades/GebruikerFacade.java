package com.feut.server.db.facades;

import com.feut.shared.models.Gebruiker;

import java.sql.SQLException;
import java.util.Map;

public class GebruikerFacade extends Facade {
    public static Gebruiker byEmailWachtwoord(String email, String password) throws SQLException {
        return (Gebruiker)Gebruiker.Deserialize(querySingle("SELECT * FROM gebruiker WHERE email = ? AND password = ?", new String[]{email, password}), Gebruiker.class);
    }

    static Gebruiker byEmail(String email) throws SQLException {
        return (Gebruiker)Gebruiker.Deserialize(querySingle("SELECT * FROM gebruiker WHERE email = ?", new String[]{email}), Gebruiker.class);
    }

    public static void registreerGebruiker(Gebruiker gebruiker) throws Exception {
        if (byEmail(gebruiker.email) != null) {
            // Todo: beter error systeem? Nu kan je de app niet in meerdere talen hebben
            throw new Exception("E-mailadres is al geregistreerd!");
        } else {
            Update("INSERT INTO gebruiker SET email = ?, voornaam = ?, achternaam = ?, password = ?", new String[]{gebruiker.email, gebruiker.voornaam, gebruiker.achternaam, gebruiker.password});
        }
    }
}
