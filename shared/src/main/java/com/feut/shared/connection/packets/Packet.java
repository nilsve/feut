package com.feut.shared.connection.packets;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;

public class Packet {

    static JSONParser parser = new JSONParser();
    static Gson gson = new Gson();
    static String json;
    String packetType;

    public static Packet readPacket(BufferedReader reader) throws IOException, ParseException, ClassNotFoundException {
        StringBuilder builder = new StringBuilder(128); // TODO: Standaard pakket grootte berekenen uit gemiddelde van vorige ofzo?

        // Lezen tot null character
        int r;
        while ((r = reader.read()) != -1) {
            char chr = (char)r;
            if (chr != '\0') {
                builder.append(chr);
            } else {
                break;
            }
        }

        json = builder.toString();

        JSONObject obj = (JSONObject)parser.parse(json);
        String packetType = (String)obj.get("packetType");
        Class c = Class.forName("com.feut.shared.connection.packets." + packetType);

        return (Packet)gson.fromJson(json, c);
    }

    public String Serialize() {
        packetType = this.getClass().getSimpleName();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "Packet{" +
                "json='" + json + '\'' +
                ", packetType='" + packetType + '\'' +
                '}';
    }
}
