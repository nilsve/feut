package com.feut.server.db.facades;

import com.feut.shared.models.Adres;
import com.feut.shared.models.Gebruiker;
import com.feut.shared.models.Huis;

import java.sql.SQLException;
import java.util.Map;

public class AdresFacade extends Facade {
    public static Adres byAddress(String zip, String number, String addition) throws SQLException {
        if (addition.equals("")) {
            return (Adres)Adres.Deserialize(querySingle("SELECT * FROM adres WHERE huisnummer = ? and toevoeging = ? and postcode = ?", new String[]{number, addition, zip}), Adres.class);
        } else {
            return (Adres)Adres.Deserialize(querySingle("SELECT * FROM adres WHERE huisnummer = ? and postcode = ?", new String[]{number, zip}), Adres.class);
        }
    }

    public static void registreerAdres(Adres adres) throws Exception {
        Adres a = byAddress(adres.postcode, adres.huisnummer, adres.toevoeging);

        if (a != null) {
            // Todo: beter error systeem? Nu kan je de app niet in meerdere talen hebben
            throw new Exception("Adres is al geregistreerd!");
        } else {
            Update("INSERT INTO adres SET woonplaats = ?, straat = ?, huisnummer = ?, toevoeging = ?, postcode = ?",
                    new String[]{adres.woonplaats, adres.straat, adres.huisnummer, adres.toevoeging, adres.postcode});

        }
    }
}