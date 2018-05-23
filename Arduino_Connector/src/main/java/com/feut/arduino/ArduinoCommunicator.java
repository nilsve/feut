package com.feut.arduino;

import com.fazecast.jSerialComm.SerialPort;

import java.util.Scanner;

public class ArduinoCommunicator {
    SerialPort serialPort = null;

    ArduinoCommunicator(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public void Listen() {
        serialPort.setBaudRate(115200);
        if (serialPort.openPort()) {
            System.out.println("Port geopend!");
        } else {
            System.out.println("Kon poort niet openen!");
        }

        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        Scanner in = new Scanner(serialPort.getInputStream());

        while (true) {
            try
            {
                if (in.hasNext()) {
                    System.out.print(in.next());
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
