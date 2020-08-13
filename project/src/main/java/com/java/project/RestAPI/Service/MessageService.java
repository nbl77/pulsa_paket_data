package com.java.project.RestAPI.Service;

import java.util.Arrays;

public class MessageService {
    private String type, message;

    public MessageService(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public MessageService() {
    }

    public String getResponse(String[] message) {
        String msg = "{\"status\":" + message[0] + ",\"message\":\"" + message[1] + "\"}";
        if (message.length > 2) {
            msg = "{\"status\":" + message[0] + ",\"message\":\"" + message[1] + "\",\"Info\":\"" + message[2] + "\"}";
        }
        msg = msg.replaceAll("([\"][\\{])","{").replaceAll("([\\}][\"])","}");
        msg = msg.replaceAll("([\"][\\[])","[").replaceAll("([\\]][\"])","]");
        return msg;
    }
}
