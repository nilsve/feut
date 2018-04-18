package com.feut.shared.models;

import java.util.Date;

public class ChatMessage extends Model {
    int chatMessageId;
    int huisId;
    int gebruikerId;
    String bericht;
    Date datum;
}
