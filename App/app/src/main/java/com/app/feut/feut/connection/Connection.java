package com.app.feut.feut.connection;

import com.feut.shared.connection.Client;
import com.feut.shared.connection.IReceivePacket;
import com.feut.shared.connection.packets.Packet;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nils.van.eijk on 16-03-18.
 */

public class Connection implements Runnable {

    static Connection instance;

    public Client client;

    Map<String, List<IReceivePacket>> packetHandlers = new HashMap<>();

    IReceivePacket packetHandler = new IReceivePacket() {
        @Override
        public void onReceivePacket(Client client, Packet packet) {
            System.out.println(packet.Serialize());

            String packetName = packet.getClass().getSimpleName();
            if (packetHandlers.containsKey(packetName)) {
                for (IReceivePacket packetHandler : packetHandlers.get(packetName)) {
                    packetHandler.onReceivePacket(client, packet);
                }
            } else {
                System.out.println("Packet dropped: " + packetName + "!");
            }
        }
    };

    public <T extends Packet> void registerPacketCallback(Class<T> packetType, IReceivePacket onReceivePacket) {
        String packetName = packetType.getSimpleName();
        if (!packetHandlers.containsKey(packetName)) {
            packetHandlers.put(packetName, new ArrayList<>());
        }

        packetHandlers.get(packetName).add(onReceivePacket);
    }

    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }

        return instance;
    }

    private void Initialize() {
        Connect();
    }

    private void Connect() {
        while (true) {
            try {
                Socket socket = new Socket("172.16.10.18", 12345);

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
