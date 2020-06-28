package ro.ase.eventplanner.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ro.ase.eventplanner.Adapter.MessageAdapter;
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
    private FirebaseAuth mFirebaseAuth;
    private String senderId;
    private CircleImageView profile_image;
    private ImageButton btn_send;
    private EditText text_send;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurentClientId();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.chat_fragment, container, false);

//        send = mRoot.findViewById(R.id.start);
//        chatHistory = mRoot.findViewById(R.id.chatHistory);
//        inputText = mRoot.findViewById(R.id.inputText);


        mFirebaseAuth = FirebaseAuth.getInstance();
        senderId = mFirebaseAuth.getUid();


        recyclerView = mRoot.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        profile_image = mRoot.findViewById(R.id.profile_image);
        btn_send = mRoot.findViewById(R.id.btn_send);
        text_send = mRoot.findViewById(R.id.text_send);


        messageAdapter = new MessageAdapter(getContext(), new ArrayList<Message>());
        recyclerView.setAdapter(messageAdapter);


        btn_send.setOnClickListener(v -> {
            String msg = text_send.getText().toString();
            if (!msg.equals("")) {


                Message message = new Message(text_send.getText().toString(), senderId, mFirebaseAuth.getUid());

                webSocketClient.send(message.toString());
                text_send.setText("");
                messageAdapter.appendMessage(message);
                Log.i("WEBSOCKET", text_send.getText().toString());


            } else {
                Toast.makeText(getContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
            }
        });
        createWebSocketClient();
        return mRoot;
    }


    public void getCurentClientId() {
        Bundle bundle = this.getArguments();

        String collection_path = bundle.getString(Constants.PATH_TAG);
        String service_id = bundle.getString("service_id");


        mFirestore = FirebaseFirestore.getInstance();
        mServiceRef = mFirestore.collection(collection_path).document(service_id);
        mServiceRef.addSnapshotListener((x, y) -> {
                    ServiceProvided service = x.toObject(ServiceProvided.class);
                    retrieveCustomerCode(service);
                }
        );
    }


    // to retrieve historuy of conversaations
    public void retrieveCustomerCode(ServiceProvided serviceProvided) {
        senderId = serviceProvided.getCreator();

        String URL = "http://10.0.2.2:8080/%s/%s/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.format(URL, senderId, mFirebaseAuth.getUid()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        Call<List<Message>> call = jsonPlaceHolderApi.getMessages();

        //TODO it should be paginated
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (!response.body().isEmpty()) {

                    messageAdapter = new MessageAdapter(getContext(), response.body());
                    recyclerView.setAdapter(messageAdapter);
//                    response.body().forEach(el -> {
//
//
//                        chatHistory.append(el.getMessage());
//                        chatHistory.append("\n");
//                    });
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.i("RETRIEVE HISTORY", "no history to retrieve");
            }
        });
    }

    public void getAllConversationMissed(ServiceProvided serviceProvided) {

        String URL = "http://10.0.2.2:8080/%s/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.format(URL, mFirebaseAuth.getUid()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        Call<List<String>> contactedPersons = jsonPlaceHolderApi.getPersonsWhoContactedMe();

        //TODO it should be paginated
        contactedPersons.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.body().isEmpty()) {
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
                            messageAdapter.appendMessage(msg);
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

