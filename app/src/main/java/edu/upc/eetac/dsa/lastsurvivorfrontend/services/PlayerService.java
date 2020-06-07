package edu.upc.eetac.dsa.lastsurvivorfrontend.services;

import edu.upc.eetac.dsa.lastsurvivorfrontend.NullOnEmptyConverterFactory;
import edu.upc.eetac.dsa.lastsurvivorfrontend.models.Player;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface PlayerService {

   /*@HTTP(method = "GET", path = "/access/signIn", hasBody = true)
    Call<Player> signIn(@Body Player player);*/
    @POST("access/signIn")
    Call<Player> signIn(@Body Player player);
    //Posts a new Player as String value to server and gets the result if the user was registered
    @POST("access/signUp")
    Call<Player> signUp(@Body Player player);
    //Edits a existing Track to the server
    @PUT("access/updatePlayer")
    Call<Player> updatePlayer(@Body Player player);
    //Delete the selected Track given the Id
    @PUT("access/deletePlayer")
    Call<Player> deletePlayer(@Body Player player);
}
