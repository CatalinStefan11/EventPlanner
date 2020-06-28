package ro.ase.eventplanner.Model;

import com.google.gson.annotations.Expose;

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
        this.uuid = "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
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
                "\"senderId\":" + "\"" + senderId + "\"" +
                ", " +
                "\"recipientId\":" + "\"" + recipientId + "\"" +
                '}';
    }
}
