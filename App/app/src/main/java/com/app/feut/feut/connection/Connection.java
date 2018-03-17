package com.app.feut.feut.connection;

import com.feut.shared.connection.Client;
import com.feut.shared.connection.IOnDisconnect;
import com.feut.shared.connection.packets.Packet;

import java.net.Socket;

/**
 * Created by nils.van.eijk on 16-03-18.
 */

public class Connection implements Runnable {

    static Connection instance;

    public Client client;
    PacketHandler packetHandler;

    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }

        return instance;
    }

    private void Initialize() {
        packetHandler = new PacketHandler();

        Connect();
    }

    private void Connect() {
        while (true) {
            try {
                Socket socket = new Socket("192.168.0.108", 12345);

                client = new Client(socket, packetHandler, (Client _client) -> handleDisconnect());
                new Thread(client).start();
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
        getInstance().Connect();
    }

    @Override
    public void run() {
        Initialize();
    }
}
