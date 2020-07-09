package ro.ase.eventplanner.Util;

public class Util {

    public final static String EMPTY_STRING = "";
    public final static String MESSAGE_HISTORY_ENDPOINT = "http://52.15.203.44/%s/%s/";
    public final static String MISSED_CONVERSATIONS_ENDPOINT = "http://52.15.203.44/%s/";
    public final static String WEBSOCKET_ENDPOINT = "ws://52.15.203.44/websocket";

    public static String removeEndlines(String message) {
        return message.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n");
    }
}
