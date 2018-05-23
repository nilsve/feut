package com.feut.shared.connection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LogHelper implements Runnable {
    private static boolean isLogWriterRunning = false;
    private static boolean isDebugMode = false;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void toggleDebugMode(boolean _isDebugMode) {
        isDebugMode = _isDebugMode;
    }
    public static boolean isDebugMode() {
        return isDebugMode;
    }

    private static List<String> logBuffer = new ArrayList<String>();
    private static Lock logMutex = new ReentrantLock(true);

    public static void startLogWriter() {
        assert(!isLogWriterRunning); // Deze functie mag maar 1x aangeroepen worden
        isLogWriterRunning = true;

        new Thread(new LogHelper()).start();
    }

    public static void Log(Client client, String log) {
        Log("Client " + client.getClientInfo() + ": " + log);
    }

    public static void Log(String log) {
        // Deze functie wordt vanuit meerdere threads aangeroepen, synchronisatie tussen de threads is dus nodig.
        // We gaan niet in deze functie al logs naar de harde schijf schrijven, omdat dit erg slecht zou zijn voor performance.
        // Iedere keer wanneer dan bijvoorbeeld een client een pakket ontvangt moet hij wachten tot de server de log heeft verwerkt. We doen dit op een andere logging thread
        try {
            logMutex.lock();
            logBuffer.add(dateFormat.format(new Date()) + ": " + log);
        } finally {
            logMutex.unlock();
        }
    }

    private static void WriteLogLoop() {
        while(true) {
            try {
                logMutex.lock();

                String fullLog = "";

                for (String log : logBuffer) {
                    fullLog += log + "\n";

                    if (isDebugMode()) {
                        // Bij debug mode direct in de console loggen en niet op de harde schijf
                        System.out.println(log);
                    }
                }

                if (!isDebugMode()) {
                    Files.write(Paths.get("logs.txt"), fullLog.getBytes(), new OpenOption[] {StandardOpenOption.APPEND, StandardOpenOption.CREATE});
                }

                logBuffer.clear();

            } catch (IOException e) {
                System.out.println("");
                e.printStackTrace();
            } finally {
                logMutex.unlock();
            }

            try {

                if (isDebugMode()) {
                    /// Iedere 5 seconden de logs naar de console schrijven
                    Thread.sleep(100);
                } else {
                    /// Iedere 5 seconden de logs naar de harde schijf schrijven
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        WriteLogLoop();
    }
}
