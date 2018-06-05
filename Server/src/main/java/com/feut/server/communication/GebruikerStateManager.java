package com.feut.server.communication;

import com.feut.server.helpers.Tuple;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.IOnDisconnect;
import com.feut.shared.models.Gebruiker;
import com.feut.shared.models.HuisGebruiker;

import java.util.HashMap;
import java.util.Map;

public class GebruikerStateManager implements IOnDisconnect {

    private GebruikerStateManager() {}
    private static GebruikerStateManager _instance = null;

    private Map<Client, Tuple<Gebruiker, HuisGebruiker>> gebruikerMap = new HashMap<>();

    public static GebruikerStateManager getInstance() {
        if (_instance == null) {
            _instance = new GebruikerStateManager();
        }

        return _instance;
    }

    public Gebruiker getGebruiker(Client client) throws Exception {
        if (!gebruikerMap.containsKey(client)) {
            throw new Exception("User not logged on!");
        }

        return gebruikerMap.get(client).x;
    }

    public HuisGebruiker getHuisGebruiker(Client client) throws Exception {
        getGebruiker(client); // Lelijke check om te kijken of de gebruiker al is ingelogd

        HuisGebruiker huisGebruiker = gebruikerMap.get(client).y;

        if (huisGebruiker == null) {
            throw new Exception("User hasn't selected a house yet!");
        }

        return huisGebruiker;
    }

    // Gooit een exception wanneer de gebruiker al is ingelogd. Zou niet voor moeten komen
    public void handleGebruikerLogin(Client client, Gebruiker gebruiker) throws Exception {
        if (gebruikerMap.containsKey(client)) {
            throw new Exception("This user is already logged on!");
        }

        gebruikerMap.put(client, new Tuple<>(gebruiker, null));
    }

    public void handleGebruikerSelectHuis(Client client, HuisGebruiker huis) throws Exception {
        if (!gebruikerMap.containsKey(client)) {
            throw new Exception("User not logged on!");
        }

        gebruikerMap.get(client).y = huis;
    }

    @Override
    public void onDisconnect(Client client) {
        gebruikerMap.remove(client);
    }
}
