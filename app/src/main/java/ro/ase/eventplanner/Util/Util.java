package ro.ase.eventplanner.Util;

public class Util {

    public final static String EMPTY_STRING = "";
    public final static String MESSAGE_HISTORY_ENDPOINT = "http://18.223.14.236/%s/%s/";
    public final static String MISSED_CONVERSATIONS_ENDPOINT = "http://18.223.14.236/%s/";
    public final static String WEBSOCKET_ENDPOINT = "ws://18.223.14.236/websocket";

    public static String removeEndlines(String message) {
        return message.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n");
    }

}
