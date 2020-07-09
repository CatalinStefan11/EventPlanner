package ro.ase.eventplanner.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Optional;

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

import static ro.ase.eventplanner.Util.Util.EMPTY_STRING;
import static ro.ase.eventplanner.Util.Util.MESSAGE_HISTORY_ENDPOINT;
import static ro.ase.eventplanner.Util.Util.WEBSOCKET_ENDPOINT;
import static ro.ase.eventplanner.Util.Util.removeEndlines;


public class ChatFragment extends Fragment {

    private View mRoot;

    private WebSocketClient webSocketClient;
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson = builder.create();
    private FirebaseFirestore mFirestore;
    private DocumentReference mServiceRef;
    private FirebaseAuth mFirebaseAuth;
    private String reipientId;
    private ImageButton btn_send;
    private EditText text_send;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mFirebaseAuth = FirebaseAuth.getInstance();
        Optional.ofNullable(bundle.getString("service_id")).ifPresent((string) -> chatFragmentEntryPoint());
        Optional.ofNullable(bundle.getString("user_id")).ifPresent((string) -> listOConversationsMissedEntryPoint());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.chat_fragment, container, false);

        recyclerView = mRoot.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        btn_send = mRoot.findViewById(R.id.btn_send);
        text_send = mRoot.findViewById(R.id.text_send);

        messageAdapter = new MessageAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(messageAdapter);

        btn_send.setOnClickListener(v -> {
            String messagePayload = removeEndlines(text_send.getText().toString());
            if (!messagePayload.equals(EMPTY_STRING)) {
                Message message = new Message(messagePayload, mFirebaseAuth.getUid(), reipientId);
                webSocketClient.send(message.toString());
                messageAdapter.appendMessage(new Message(text_send.getText().toString(), mFirebaseAuth.getUid(), reipientId));
                text_send.setText(EMPTY_STRING);
                Log.i("WEBSOCKET", text_send.getText().toString());
            } else {
                Toast.makeText(getContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
            }
        });
        createWebSocketClient();
        return mRoot;
    }


    public void chatFragmentEntryPoint() {
        Bundle bundle = this.getArguments();

        String collection_path = bundle.getString(Constants.PATH_TAG);
        String service_id = bundle.getString("service_id");


        mFirestore = FirebaseFirestore.getInstance();
        mServiceRef = mFirestore.collection(collection_path).document(service_id);
        mServiceRef.addSnapshotListener((x, y) -> {
                    ServiceProvided service = x.toObject(ServiceProvided.class);
                    retrieveConversationHistory(service);
                }
        );
    }

    public void listOConversationsMissedEntryPoint() {
        Bundle bundle = this.getArguments();
        String user_id = bundle.getString("user_id");
        reipientId = user_id;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.format(MESSAGE_HISTORY_ENDPOINT, user_id, mFirebaseAuth.getUid()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Message>> call = jsonPlaceHolderApi.getMessages();

        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (!response.body().isEmpty()) {
                    messageAdapter = new MessageAdapter(getContext(), response.body());
                    recyclerView.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.i("RETRIEVE HISTORY", "no history to retrieve");
            }
        });
    }

    public void retrieveConversationHistory(ServiceProvided serviceProvided) {
        reipientId = serviceProvided.getCreator();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.format(MESSAGE_HISTORY_ENDPOINT, reipientId, mFirebaseAuth.getUid()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Message>> call = jsonPlaceHolderApi.getMessages();

        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (!response.body().isEmpty()) {
                    messageAdapter = new MessageAdapter(getContext(), response.body());
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.i("RETRIEVE HISTORY", "no history to retrieve");
            }
        });
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            uri = new URI(WEBSOCKET_ENDPOINT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send(new Message("Connected", mFirebaseAuth.getUid(), EMPTY_STRING).toString());
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
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);
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
        webSocketClient.connect();
    }

}

