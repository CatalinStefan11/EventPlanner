package ro.ase.eventplanner.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.retrofit.JsonPlaceHolderApi;
import tech.gusavila92.websocketclient.WebSocketClient;


public class ChatFragment extends Fragment {

    private View mRoot;
    private Button send;
    private TextView chatHistory;
    private WebSocketClient webSocketClient;
    private EditText inputText;
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson = builder.create();

    private String senderId = String.valueOf(new Random().nextInt(10));

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.chat_fragment, container, false);

        send = mRoot.findViewById(R.id.start);
        chatHistory = mRoot.findViewById(R.id.chatHistory);
        inputText = mRoot.findViewById(R.id.inputText);

        System.out.println("SENDER: " + senderId);

        String URL = "http://10.0.2.2:8080/%s/%s/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.format(URL, "1","2"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        Call<List<Message> > call = jsonPlaceHolderApi.getMessages();

        //TODO it should be paginated
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (!response.body().isEmpty())
                {
                    response.body().forEach(el -> {
                        chatHistory.append(el.message);
                        chatHistory.append("\n");
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.i("RETRIEVE HISTORY", "no history to retrieve");
            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("WEBSOCKET", inputText.getText().toString());
                chatHistory.append(inputText.getText().toString() + "\n");
                String re = Arrays.asList(inputText.getText().toString().split(":")).get(0);
                inputText.setText("");
                webSocketClient.send(new Message(inputText.getText().toString(), senderId, re).toString());
            }
        });


//        requestQueue.start();
//
//        for(Message message: messageList.messages)
//        {
//            chatHistory.append(message.message);
//            chatHistory.append("\n");
//        }

        createWebSocketClient();
        return mRoot;
    }


    public class Message {
        String message;
        String senderID;
        String recipientId;
        String uuid;

        public Message(String message, String senderID, String recipientId) {
            this.message = message;
            this.senderID = senderID;
            this.recipientId = recipientId;
            this.uuid = "";
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

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://10.0.2.2:8080/websocket");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send("Connected");
            }

            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                final String message = s;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

//                            Message msg = gson.fromJson(message, Message.class);
//                            chatHistory.append(msg.toString());
                            chatHistory.append(message + "\n");
                            System.out.println(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }

            @Override
            public void onPingReceived(byte[] data) {
            }

            @Override
            public void onPongReceived(byte[] data) {
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("DisConnected");
            }
        };
        webSocketClient.setConnectTimeout(Integer.MAX_VALUE);
        webSocketClient.setReadTimeout(60000);
        //TODO think about it
//        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public class MessageDTO{
        private List<Message> messages;

        public MessageDTO(List<Message> messages) {
            this.messages = messages;
        }

        public MessageDTO() {
        }

    }

}
