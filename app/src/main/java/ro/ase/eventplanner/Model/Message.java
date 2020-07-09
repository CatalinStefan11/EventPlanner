package ro.ase.eventplanner.Model;

import com.google.gson.annotations.Expose;

import static ro.ase.eventplanner.Util.Util.EMPTY_STRING;

public class Message {
    String message;
    String senderId;
    String recipientId;
    @Expose(serialize = false, deserialize = false)
    String uuid;

    public Message(String message, String senderId, String recipientId) {
        this.message = message;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.uuid = EMPTY_STRING;
    }

    public String getMessage() {
        return message;
    }

    public String getRecipientId() {
        return recipientId;
    }

    @Override
    public String toString() {
        return "{" +
                "\"message\":" + "\"" + message + "\"" +
                ", " +
                "\"senderId\":" + "\"" + senderId + "\"" +
                ", " +
                "\"recipientId\":" + "\"" + recipientId + "\"" +
                '}';
    }
}
