package com.feut.shared.connection;

import com.feut.shared.connection.packets.Packet;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    ServerSocket socket;
    List<Client> clientList = new ArrayList<Client>();

    IReceivePacket onReceivePacket;
    IOnDisconnect onDisconnect;

    public Server(int port, IReceivePacket onReceivePacket, @Nullable IOnDisconnect onDisconnect) throws IOException {
        this.socket = new ServerSocket(port);
        this.onReceivePacket = onReceivePacket;
        this.onDisconnect = onDisconnect;
    }

    public void Listen() throws IOException {
        while(true) {
            Client client = new Client(socket.accept(), onReceivePacket, (Client _client) -> handleDisconnect(_client));
            new Thread(client).start();
            LogHelper.Log(client, "Connected");
            clientList.add(client);
        }
    }

    void handleDisconnect(Client client) {
        clientList.remove(client);
        if (this.onDisconnect != null) {
            this.onDisconnect.onDisconnect(client);
        }
    }

    public void Broadcast(Packet packet) {
        for (Client client : clientList) {
            client.sendPacket(packet);
        }
    }

}
