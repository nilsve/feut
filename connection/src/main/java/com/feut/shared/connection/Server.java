package com.feut.shared.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    ServerSocket socket;
    List<Client> clientList = new ArrayList<Client>();

    public Server(int port) throws IOException {
        socket = new ServerSocket(port);
    }

    public void Listen() throws IOException {
        while(true) {
            Client client = new Client(socket.accept());
            new Thread(client).start();
            System.out.println("New client connected: " + client.getClientInfo());
            clientList.add(client);
        }
    }

}
