package ro.ase.eventplanner.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ro.ase.eventplanner.Model.Message;

public interface JsonPlaceHolderApi {

    @GET("messages")
    Call<List<Message>> getMessages();

    @GET("contacts")
    Call<List<String>> getPersonsWhoContactedMe();
}
