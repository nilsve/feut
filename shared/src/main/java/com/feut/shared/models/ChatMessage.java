package com.feut.shared.models;

import java.util.Date;

public class ChatMessage extends Model {
    public int chatMessageId;
    public int huisId;
    public int gebruikerId;
    public String bericht;
    public Date datum;
}
