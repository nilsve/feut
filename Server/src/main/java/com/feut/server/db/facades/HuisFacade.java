package com.feut.server.db.facades;

import com.feut.shared.models.Adres;
import com.feut.shared.models.Gebruiker;
import com.feut.shared.models.Huis;

import java.sql.SQLException;
import java.util.Map;

public class HuisFacade extends Facade {
    static Huis byHouseID(int huisId) throws SQLException {
        return (Huis)Huis.Deserialize(querySingle("SELECT * FROM huis WHERE huis_id = ?", new String[]{Integer.toString(huisId)}), Huis.class);
    }

    public static Huis byHouseName(String name) throws SQLException {
        return (Huis)Huis.Deserialize(querySingle("SELECT * FROM huis WHERE naam = ?", new String[]{name}), Huis.class);
    }

    public static void registreerHuis(int adres_id, String email, String naam) throws Exception {
        // Op basis van de gegeven naam proberen we een huis te zoeken. Als dit lukt is hij niet null en gooien we een Exception.
        // De gebruiker hebben we nodig voor het gebruiker_id en zoeken we op een soortgelijke manier, maar dan via de Gebruikersfacade.
        Huis h = byHouseName(naam);
        Gebruiker g = GebruikerFacade.byEmail(email);

        if (h != null) {
            // Todo: beter error systeem? Nu kan je de app niet in meerdere talen hebben
            throw new Exception("Naam voor huis bestaat al!");
        } else {
            // Update huis op basis van het adres_id en de naam, het huis_id genereert de database zelf.
            Update("INSERT INTO huis SET adres_id = ?, naam = ?", new String[]{Integer.toString(adres_id), naam});
            // Update de huis_gebruiker tabel met gegevens van het huis en gegevens van de gebruiker.
            // beheerder = 1 en dus waar
            // aanwezig = 0 en dus niet waar
            // online = 1 en dus waar
            h = byHouseName(naam);
            Update("INSERT INTO huis_gebruiker SET huis_id = ?, gebruiker_id = ?, beheerder = ?, aanwezig = ?, online = ?",
                    new String[] {Integer.toString(h.huisId), Integer.toString(g.gebruikerId), "1", "0", "1"});
        }
    }
}