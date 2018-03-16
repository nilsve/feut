package com.feut.shared.connection;

import com.feut.shared.connection.packets.Packet;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable {
    Socket socket;
    List<Packet> packetHistory = new ArrayList<>();

    public Client(Socket socket) {
        this.socket = socket;
    }

    public String getClientInfo() {
        return socket.getInetAddress().toString();
    }

    private void Listen() throws IOException, InterruptedException {
        Helper.Log("Client " + getClientInfo() + " started listening");
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while(true) {
            try {
                Packet packet = Packet.readPacket(reader);
                if (Helper.isDebugMode()) {
                    System.out.println("New packet received from " + getClientInfo());
                    System.out.println("Serialized: " + packet.Serialize());
                    packetHistory.add(packet);
                }
            } catch(ParseException ex) {
                System.out.println("Corrupted packet received!");
            } catch (ClassNotFoundException ex) {
                System.out.println("Unknown packet received!");
            }
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Listening");
            Listen();
        } catch (Exception err) {
            Helper.Log("Client connection closed for " + getClientInfo());
            Helper.Log(err.getMessage());
        }

    }
}
