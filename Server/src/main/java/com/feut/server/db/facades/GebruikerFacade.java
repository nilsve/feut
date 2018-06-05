package com.feut.server.db.facades;

import com.feut.shared.models.Adres;
import com.feut.shared.models.Gebruiker;
import com.feut.shared.models.Huis;
import com.feut.shared.models.HuisGebruiker;

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

    public static void toggleAanwezigheid(int gebruikerId, int huisId, boolean aanwezig) throws Exception {
        Update("UPDATE huis_gebruiker SET aanwezig = ? WHERE gebruiker_id = ? AND huis_id = ?", new String[] {aanwezig ? "1" : "0", Integer.toString(gebruikerId), Integer.toString(huisId)});
    }

    public static HuisGebruiker getHuisGebruiker(int gebruikerId) throws Exception {
        return (HuisGebruiker)HuisGebruiker.Deserialize(querySingle("SELECT * FROM huis_gebruiker WHERE gebruiker_id = ? LIMIT 1", new String[]{Integer.toString(gebruikerId)}), HuisGebruiker.class);
    }

}
