package com.app.feut.feut.connection;

import com.feut.shared.connection.Client;
import com.feut.shared.connection.IOnDisconnect;
import com.feut.shared.connection.packets.Packet;

import java.net.Socket;

/**
 * Created by nils.van.eijk on 16-03-18.
 */

public class Connection {

    public static Client client;
    static PacketHandler packetHandler;

    public static void Initialize() {
        packetHandler = new PacketHandler();

        Connect();
    }

    static void Connect() {
        while (true) {
            try {
                Socket socket = new Socket("172.16.10.21", 12345);

                client = new Client(socket, packetHandler, (Client _client) -> handleDisconnect());
                break;
            } catch (Exception err) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    static void handleDisconnect() {

    }

}
