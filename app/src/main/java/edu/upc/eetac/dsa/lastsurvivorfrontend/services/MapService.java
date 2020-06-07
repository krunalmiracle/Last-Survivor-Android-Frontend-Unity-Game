package edu.upc.eetac.dsa.lastsurvivorfrontend.services;

import java.util.List;

import edu.upc.eetac.dsa.lastsurvivorfrontend.models.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface MapService {
    //Posts a new map
    @POST("map/addMap")
    Call<Map> addMap(@Body Map map);
    //Gets the list of maps from database
    @GET("map/getMaps")
    Call<List<Map>> getMaps();
    //Updates a map
    @PUT("map/updateMap")
    Call<Map> updateMap(@Body Map map);
    //Deletes a map
    @PUT("map/deleteMap")
    Call<Map> deleteMap(@Body Map map);

}
