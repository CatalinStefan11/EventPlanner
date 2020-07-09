package ro.ase.eventplanner.Util;

public class Util {

    public final static String EMPTY_STRING = "";
    public final static String MESSAGE_HISTORY_ENDPOINT = "http://10.0.2.2:8080/%s/%s/";
    public final static String MISSED_CONVERSATIONS_ENDPOINT = "http://10.0.2.2:8080/%s/";
    public final static String WEBSOCKET_ENDPOINT = "ws://10.0.2.2:8080/websocket";

    public static String removeEndlines(String message) {
        return message.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n");
    }
}
