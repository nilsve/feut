package com.feut.server;

import com.feut.server.db.DBConnection;
import com.feut.server.db.facades.GebruikerFacade;
import com.feut.shared.connection.*;
import com.feut.shared.connection.Server;
import com.feut.shared.models.Adres;
import com.feut.shared.models.Gebruiker;

import java.io.IOException;

public class Main {

    static int PORT = 12345;

    public static void main(String[] args) {

        // Verbinding opzetten
        DBConnection.getInstance();

        /*
            TODO: Dit weg halen
            Hieronder een voorbeeld over hoe het orm systeem werkt.
        */
        Gebruiker gebruiker = GebruikerFacade.byEmailWachtwoord("nils@blink.nl", "hoi");

        try {
            Helper.toggleDebugMode(true);

            PacketHandler packetHandler = new PacketHandler();

            System.out.println("Starting server on port " + PORT);
            Server server = new Server(PORT, packetHandler);

            packetHandler.server = server;

            try {
                server.Listen();
            } catch (IOException err) {
                System.out.println("Error while trying to wait for sockets!");
                System.out.println(err.toString());
            }
        } catch (IOException err) {
            System.out.println("Could not start the server!");
            System.out.println(err.toString());
        }
    }
}
