package com.feut.server.db.facades;

import com.feut.shared.models.Gebruiker;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class GebruikerFacadeTest {

    @Test
    void registreerGebruiker() throws Exception {
        Gebruiker gebruiker = new Gebruiker();
        gebruiker.email = "hoi@hoi.nl";
        GebruikerFacade.registreerGebruiker(gebruiker);
    }
}