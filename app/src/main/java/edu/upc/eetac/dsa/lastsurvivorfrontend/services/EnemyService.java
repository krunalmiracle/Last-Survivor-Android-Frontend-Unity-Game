package edu.upc.eetac.dsa.lastsurvivorfrontend.services;

import java.util.List;

import edu.upc.eetac.dsa.lastsurvivorfrontend.models.Enemy;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EnemyService {
@GET("enemy/getEnemies")
    Call<List<Enemy>>getEnemies();

}
