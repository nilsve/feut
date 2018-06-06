package com.feut.arduino;

import com.fazecast.jSerialComm.SerialPort;
import com.feut.shared.connection.Client;
import com.feut.shared.connection.packets.CheckinPacket;

import java.util.Scanner;

public class ArduinoCommunicator {
    SerialPort serialPort = null;
    Client client = null;
    String lastScannedId = "";
    long lastScannedTime = 0;

    ArduinoCommunicator(Client client, SerialPort serialPort) {
        this.client = client;
        this.serialPort = serialPort;
    }

    public void Listen() {
        serialPort.setBaudRate(115200);
        if (serialPort.openPort()) {
            System.out.println("Port geopend!");
        } else {
            System.out.println("Kon poort niet openen!");
            return;
        }

        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        Scanner in = new Scanner(serialPort.getInputStream());

        while (true) {
            try
            {

                if (in.hasNext()) {
                    String id = in.next();

                    if (!lastScannedId.equals(id) || lastScannedTime < System.currentTimeMillis() - 5000) {
                        System.out.println(id);

                        CheckinPacket packet = new CheckinPacket();
                        packet.chipId = id;

                        client.sendPacket(packet);
                    }

                    lastScannedTime = System.currentTimeMillis();
                    lastScannedId = id;
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
