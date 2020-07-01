package ro.ase.eventplanner.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ro.ase.eventplanner.Adapter.ChatHistoryAdapter;
import ro.ase.eventplanner.Model.ServiceProvided;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.retrofit.JsonPlaceHolderApi;

public class ChatHistoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FirebaseAuth mFirebaseAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_share, container, false);

        mRecyclerView = root.findViewById(R.id.messages_recycler);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        getAllConversationMissed();

        return root;
    }

    public void getAllConversationMissed() {

        String URL = "http://10.0.2.2:8080/%s/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.format(URL, mFirebaseAuth.getUid()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        Call<List<String>> contactedPersons = jsonPlaceHolderApi.getPersonsWhoContactedMe();
        final List<String> contactsId;

        //TODO it should be paginated
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