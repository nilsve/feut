package com.feut.shared.connection;

import com.feut.shared.connection.packets.Packet;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    ServerSocket socket;
    List<Client> clientList = new ArrayList<Client>();

    IReceivePacket onReceivePacket;

    public Server(int port, IReceivePacket onReceivePacket) throws IOException {
        this.socket = new ServerSocket(port);
        this.onReceivePacket = onReceivePacket;
    }

    public void Listen() throws IOException {
        while(true) {
            Client client = new Client(socket.accept(), onReceivePacket, (Client _client) -> onDisconnect(_client));
            new Thread(client).start();
            LogHelper.Log(client, "Connected");
            clientList.add(client);
        }
    }

    void onDisconnect(Client client) {
        clientList.remove(client);
    }

    public void Broadcast(Packet packet) {
        for (Client client : clientList) {
            client.sendPacket(packet);
        }
    }

}
