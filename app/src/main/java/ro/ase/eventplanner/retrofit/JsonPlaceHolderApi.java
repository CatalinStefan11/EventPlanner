package ro.ase.eventplanner.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ro.ase.eventplanner.Fragment.ChatFragment;

public interface JsonPlaceHolderApi {

    @GET("messages")
    Call<List<ChatFragment.Message>> getMessages();
}
