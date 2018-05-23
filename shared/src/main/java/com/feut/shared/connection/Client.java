package com.feut.shared.connection;

import com.feut.shared.connection.packets.ErrorPacket;
import com.feut.shared.connection.packets.Packet;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable {
    boolean shouldClose = false;
    Socket socket;
    List<Packet> packetHistory = new ArrayList<>();

    List<IReceivePacket> packetHandlers = new ArrayList<>();
    IOnDisconnect onDisconnect;

    public Client(Socket socket, IReceivePacket onReceivePacket, IOnDisconnect onDisconnect) {
        this.socket = socket;
        this.packetHandlers.add(onReceivePacket);
        this.onDisconnect = onDisconnect;
    }

    public String getClientInfo() {
        return socket.getInetAddress().toString();
    }

    public void regiserPacketHandler(IReceivePacket onReceivePacket) {
        packetHandlers.add(onReceivePacket);
    }

    public void unregisterPacketHandler(IReceivePacket onReceivePacket) {
        packetHandlers.remove(onReceivePacket);
    }

    private void Listen() throws IOException, InterruptedException {
        LogHelper.Log(this, "Started listening");
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while(!socket.isClosed() && !shouldClose) {
            try {
                Packet packet = Packet.readPacket(reader);
                if (LogHelper.isDebugMode()) {
                    LogHelper.Log(this, "New packet received");
                    packetHistory.add(packet);
                }

                for (IReceivePacket packetHandler : packetHandlers) {
                    try {
                        packetHandler.onReceivePacket(this, packet);
                    } catch (Exception err) {
                        LogHelper.Log(this, "Unhandled exception: " + err.getMessage());

                        if (LogHelper.isDebugMode()) {
                            ErrorPacket p = new ErrorPacket();
                            p.message = err.getMessage();
                            sendPacket(p);
                        }
                    }
                }
            } catch(ParseException ex) {
                LogHelper.Log(this, "Corrupted packet received!");
                Disconnect();
            } catch (ClassNotFoundException ex) {
                LogHelper.Log(this, "Unknown packet received!");
                Disconnect(); // Misschien niet helemaal nodig, maar het zou niet voor moeten komen.
            }
        }
    }

    public void sendPacket(Packet packet) {
        String json = packet.Serialize();
        byte[] buffer = json.getBytes();
        try {
            OutputStream stream = socket.getOutputStream();
            stream.write(buffer);
            stream.write(0);
        } catch (Exception err) {
            LogHelper.Log(this, "Could not send packet");
            Disconnect();
        }
    }

    void Disconnect() {
        shouldClose = true;

        try {
            socket.close();
        } catch (IOException er) {}
        finally {
            onDisconnect.onDisconnect(this);
        }
    }

    @Override
    public void run() {
        try {
            Listen();
        } catch (Exception err) {
            LogHelper.Log(this, "Client connection closed");
            LogHelper.Log(this, err.getMessage());
        }

    }
}
