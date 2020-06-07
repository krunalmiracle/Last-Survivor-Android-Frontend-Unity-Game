package edu.upc.eetac.dsa.lastsurvivorfrontend.services;
import java.util.List;

import edu.upc.eetac.dsa.lastsurvivorfrontend.models.Player;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface RankingService {
    @GET("rank/global")
    Call<List<Player>> rankingList();
}
