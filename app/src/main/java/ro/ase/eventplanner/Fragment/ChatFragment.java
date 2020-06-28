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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ro.ase.eventplanner.Model.Message;
import ro.ase.eventplanner.Model.ServiceProvided;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.Constants;
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
    private FirebaseFirestore mFirestore;
    private DocumentReference mServiceRef;
    public FirebaseAuth mFirebaseAuth;

    private String senderId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurentClientId();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.chat_fragment, container, false);

        send = mRoot.findViewById(R.id.start);
        chatHistory = mRoot.findViewById(R.id.chatHistory);
        inputText = mRoot.findViewById(R.id.inputText);

        System.out.println("SENDER: " + senderId);
        mFirebaseAuth = FirebaseAuth.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("WEBSOCKET", inputText.getText().toString());
                chatHistory.append(inputText.getText().toString() + "\n");
                webSocketClient.send(new Message(inputText.getText().toString(), senderId, mFirebaseAuth.getUid()).toString());
                inputText.setText("");
            }
        });

        createWebSocketClient();
        return mRoot;
    }


    public void getCurentClientId(){
        Bundle bundle = this.getArguments();

        String collection_path = bundle.getString(Constants.PATH_TAG);
        String service_id = bundle.getString("service_id");


        mFirestore = FirebaseFirestore.getInstance();
        mServiceRef = mFirestore.collection(collection_path).document(service_id);
        mServiceRef.addSnapshotListener((x,y) -> {
            ServiceProvided service = x.toObject(ServiceProvided.class);
            retrieveCustomerCode(service);
                }
        );
    }

    // to retrieve historuy of conversaations
    public void retrieveCustomerCode(ServiceProvided serviceProvided){
        senderId = serviceProvided.getCreator();

        String URL = "http://10.0.2.2:8080/%s/%s/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.format(URL, senderId, mFirebaseAuth.getUid().toString()))
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
                        chatHistory.append(el.getMessage());
                        chatHistory.append("\n");
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.i("RETRIEVE HISTORY", "no history to retrieve");
            }
        });
    }

    public void getAllConversationMissed(ServiceProvided serviceProvided){

        String URL = "http://10.0.2.2:8080/%s/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.format(URL, mFirebaseAuth.getUid()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        Call<List<String> > contactedPersons = jsonPlaceHolderApi.getPersonsWhoContactedMe();

        //TODO it should be paginated
        contactedPersons.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.body().isEmpty())
                {
                    //iaici e lista de stringuri
                    System.out.println(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("RETRIEVE HISTORY", "no history to retrieve");
            }
        });
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
                webSocketClient.send(new Message("Connected", mFirebaseAuth.getUid(), "").toString());
            }

            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                final String message = s;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Message msg = gson.fromJson(message, Message.class);
                            chatHistory.append(msg.getMessage());
                            chatHistory.append("\n");
//                            System.out.println(msg.toString());
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
        webSocketClient.setReadTimeout(Integer.MAX_VALUE);
        //TODO think about it
//        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

}
