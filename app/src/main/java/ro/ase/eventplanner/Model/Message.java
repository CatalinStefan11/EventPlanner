package ro.ase.eventplanner.Model;

import com.google.gson.annotations.Expose;

public class Message {
    String message;
    String senderID;
    String recipientId;
    @Expose(serialize = false, deserialize = false)
    String uuid;

    public Message(String message, String senderID, String recipientId) {
        this.message = message;
        this.senderID = senderID;
        this.recipientId = recipientId;
        this.uuid = "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "{" +
                "\"message\":" + "\"" + message + "\"" +
                ", " +
                "\"senderId\":" + "\"" + senderID + "\"" +
                ", " +
                "\"recipientId\":" + "\"" + recipientId + "\"" +
                '}';
    }
}
