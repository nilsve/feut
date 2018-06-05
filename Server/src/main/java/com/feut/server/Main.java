package com.feut.server;

import com.feut.server.communication.MainPacketHandler;
import com.feut.server.communication.GebruikerStateManager;
import com.feut.server.db.DBConnection;
import com.feut.shared.connection.*;
import com.feut.shared.connection.Server;

import java.io.IOException;

public class Main {

    static int PORT = 12345;


    public static void main(String[] args) {

        LogHelper.toggleDebugMode(true);
        LogHelper.startLogWriter();

        // Verbinding opzetten
        DBConnection.getInstance();

        try {

            MainPacketHandler packetHandler = new MainPacketHandler();

            LogHelper.Log("Starting server on port " + PORT);
            Server server = new Server(PORT, packetHandler, GebruikerStateManager.getInstance());

            packetHandler.server = server;

            try {
                server.Listen();
            } catch (IOException err) {
                LogHelper.Log("Error while trying to wait for sockets!");
                LogHelper.Log(err.toString());
            }
        } catch (IOException err) {
            LogHelper.Log("Could not start the server!");
            LogHelper.Log(err.toString());
        }
    }
}
