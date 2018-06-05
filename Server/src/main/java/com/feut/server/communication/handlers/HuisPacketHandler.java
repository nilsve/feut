package com.feut.server.communication.handlers;

import com.feut.server.db.facades.AdresFacade;
import com.feut.server.db.facades.GebruikerFacade;
import com.feut.server.db.facades.HuisFacade;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.*;
import com.feut.shared.models.Adres;
import com.feut.shared.models.Gebruiker;
import com.feut.shared.models.Huis;

import java.sql.SQLException;

public class HuisPacketHandler implements IReceivePacket {
    @Override
    public void onReceivePacket(Client client, Packet packet) throws SQLException {
        switch (packet.getClass().getSimpleName()) {
            case "RegisterAddressRequest": {
                // Pakket ontvangen en casten naar RegisterAddressRequest
                RegisterAddressRequest registerAddressRequest = (RegisterAddressRequest) packet;

                // Adres aanmaken, dit object gebruikt de db om een update query te doen.
                Adres adres = new Adres();
                adres.straat = registerAddressRequest.street;
                adres.huisnummer = registerAddressRequest.streetNumber;
                adres.toevoeging = registerAddressRequest.addition;
                adres.postcode = registerAddressRequest.zipCode;
                adres.woonplaats = registerAddressRequest.city;

                // We hebben de gegevens van de gebruiker nodig tijdens registratie. Deze zijn opgegeven in het verzoek wat de Android applicatie verstuurd.
                // Op basis hiervan kunnen we een query doen in de GebruikersFacade
                Gebruiker gebruiker = GebruikerFacade.byEmailWachtwoord(registerAddressRequest.email, registerAddressRequest.password);

                // Pakket alvast voor respons klaarmaken, het is nu nog leeg
                RegisterAddressResponse registerAddressResponse = new RegisterAddressResponse();

                // Adres aanmaken, zodat we het adres_id weten. Dit wordt aangemaakt door de db zelf namelijk.
                // Als het mislukt versturen we direct een negatieve boolean naar de Android applicatie
                try {
                    AdresFacade.registreerAdres(adres);
                } catch (Exception e) {
                    e.printStackTrace();
                    registerAddressResponse.success = false;
                    client.sendPacket(registerAddressResponse);
                    break;
                } finally {
                    // Nu het adres is aangemaakt kunnen we een query doen en het adres_id ophalen.
                    adres = AdresFacade.byAddress(adres.postcode, adres.huisnummer, adres.toevoeging);
                }

                // Huis registreren met behulp van het opgehaalde adres_id en de naam voor het huis die in het RegisterAddresRequest zit vanaf de Android applicatie.
                // Deze Facade vult ook meteen de huis_gebruiker tabel. En ook hier vangen we de eventuele fout af.
                try {
                    HuisFacade.registreerHuis(adres.adresId, registerAddressRequest.email, registerAddressRequest.name);
                } catch (Exception e) {
                    e.printStackTrace();
                    registerAddressResponse.success = false;
                    client.sendPacket(registerAddressResponse);
                    break;
                }

                // Als de code hier uitkomt is de gehele update query goed uitgevoerd en kunnen we een pakket terugzenden met een positieve boolean
                registerAddressResponse.success = true;
                client.sendPacket(registerAddressResponse);
                break; }

        }
    }
}
