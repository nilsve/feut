package com.feut.shared.connection;

import com.feut.shared.connection.packets.Packet;

public interface IReceivePacket {
    void onReceivePacket(Client client, Packet packet);
}
