package com.feut.server;

import com.feut.shared.connection.*;
import com.feut.shared.connection.Server;

import java.io.IOException;

public class Main {

    static int PORT = 12345;

    public static void main(String[] args) {
        try {
            Helper.toggleDebugMode(true);
            System.out.println("Starting server on port " + PORT);
            Server server = new Server(PORT);

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
