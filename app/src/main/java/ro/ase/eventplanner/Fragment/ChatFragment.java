package ro.ase.eventplanner.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import nbouma.com.wstompclient.implementation.StompClient;
import nbouma.com.wstompclient.model.Frame;
import okhttp3.OkHttpClient;
import ro.ase.eventplanner.R;


public class ChatFragment extends Fragment {

    private View mRoot;
    private Button start;
    private TextView output;
    private StompClient stompClient;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.chat_fragment, container, false);

        start =  mRoot.findViewById(R.id.start);
        output = mRoot.findViewById(R.id.output);



        stompClient = new StompClient("ws://10.0.2.2:8080/websocket/websocket") { //example "ws://localhost:8080/message-server"
            @Override
            protected void onStompError(String errorMessage) {
                Log.i("WEBSOCKET", "error : " + errorMessage);
            }

            @Override
            protected void onConnection(boolean connected) {
                Log.i("WEBSOCKET", "connected : ----------" + String.valueOf(connected));
            }

            @Override
            protected void onDisconnection(String reason) {
                Log.i("WEBSOCKET", "disconnected : " + reason);
            }

            @Override
            protected void onStompMessage(Frame frame) {
                Log.i("WEBSOCKET","prrimit msg");
                output.setText(frame.getBody());
            }

        };

//        stompClient.subscribe("/topic/send");
        stompClient.subscribe("/topic/send","");
//        stompClient.sendMessage("/topic/send","sd");




        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("WEBSOCKET", new Message("aa","22").toString());
                stompClient.sendMessage("/app/send", new Message("aa","22").toString());
            }
        });

//
//        stompClient.sendMessage("/topic/send", "MESSI");


        return mRoot;
    }




public class Message{
        String message;
        String sender;

    public Message(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "{" +
                "\"message\":" + "\""  + message +"\"" +
                ", " +
                "\"sender\":" + "\"" +sender  + "\"" +
                '}';
    }
}



}
