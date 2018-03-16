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

    String packetType;

    public static Packet readPacket(BufferedReader reader) throws IOException, ParseException, ClassNotFoundException {
        StringBuilder builder = new StringBuilder(128); // TODO: Betere standaard pakket grootte?

        int r;
        while ((r = reader.read()) != -1) {
            char chr = (char)r;
            if (chr != '\0') {
                builder.append(chr);
            } else {
                break;
            }
        }

        String json = builder.toString();

        JSONObject obj = (JSONObject)parser.parse(json);
        String packetType = (String)obj.get("packetType");
        Class c = Class.forName("com.feut.shared.connection.packets." + packetType);

        return (Packet)gson.fromJson(json, c);
    }

    public String Serialize() {
        String[] splitted = this.getClass().getName().split("\\.");
        packetType = splitted[splitted.length - 1];

        return gson.toJson(this);
    }
}
