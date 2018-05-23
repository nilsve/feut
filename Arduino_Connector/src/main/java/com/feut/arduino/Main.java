package com.feut.arduino;

import com.fazecast.jSerialComm.SerialPort;
import com.feut.shared.connection.Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    static PacketHandler packetHandler = new PacketHandler();
    static Client client = null;
    static ArduinoCommunicator arduinoCommunicator = null;

    public static void main(String[] args) {
        SerialPort ports[] = SerialPort.getCommPorts();

        System.out.println("Kies het poort nummer:");

        for (int i = 0; i < ports.length; i++) {
            System.out.println("\t" + i + ": " + ports[i].getDescriptivePortName());
        }

        Scanner scanner = new Scanner(System.in);
        int portNumber = scanner.nextInt();

        if (portNumber < 0 || portNumber > ports.length) {
            System.out.println("Invalid port number!");
        }

        arduinoCommunicator = new ArduinoCommunicator(ports[portNumber]);
        arduinoCommunicator.Listen();
    }

    static void Connect() {
        try {
            Socket socket = new Socket("localhost", 12345);
            client = new Client(socket, packetHandler, (Client _client) -> handleDisconnect());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static void handleDisconnect() {


    }
}
