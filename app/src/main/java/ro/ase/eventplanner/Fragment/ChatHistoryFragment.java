package ro.ase.eventplanner.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ro.ase.eventplanner.Adapter.ChatHistoryAdapter;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.retrofit.JsonPlaceHolderApi;

import static ro.ase.eventplanner.Util.Util.MISSED_CONVERSATIONS_ENDPOINT;

public class ChatHistoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FirebaseAuth mFirebaseAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat_history, container, false);

        mRecyclerView = root.findViewById(R.id.messages_recycler);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        retrieveAllMissedConversationsByUser();

        return root;
    }

    public void retrieveAllMissedConversationsByUser() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.format(MISSED_CONVERSATIONS_ENDPOINT, mFirebaseAuth.getUid()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<String>> contactedPersons = jsonPlaceHolderApi.getPersonsWhoContactedMe();

        contactedPersons.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.body().isEmpty()) {
                    ChatHistoryAdapter adapter = new ChatHistoryAdapter(getContext(), response.body());
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("RETRIEVE HISTORY", "no history to retrieve");
            }
        });
    }
}