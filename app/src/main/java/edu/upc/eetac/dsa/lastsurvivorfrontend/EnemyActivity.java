package edu.upc.eetac.dsa.lastsurvivorfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.upc.eetac.dsa.lastsurvivorfrontend.models.Enemy;
import edu.upc.eetac.dsa.lastsurvivorfrontend.services.EnemyService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnemyActivity extends AppCompatActivity {
    EnemyService service;
    List<Enemy> enemyList;
    private RecyclerView recyclerView;
    private EnemyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar pb_circular;
    private static Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enemy);
        recyclerView = findViewById(R.id.enemy_recyclerView);
        pb_circular = findViewById(R.id.progressBar_cyclic2);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        startRetrofit();
        getEnemies();


    }
    private static void startRetrofit(){
        //HTTP &
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //Attaching Interceptor to a client
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(interceptor).build();

        // Running Retrofit to get result from Local tracks service Interface
        //Remember when using Local host on windows the IP is 10.0.2.2 for Android
        //Also added NullOnEmptyConverterFactory when the response from server is empty
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/Backend/")
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
    private void getEnemies(){
        pb_circular.setVisibility(View.VISIBLE);
        service=retrofit.create(EnemyService.class);
        Call<List<Enemy>> enemies= service.getEnemies();
        enemies.enqueue(new Callback<List<Enemy>>() {
            @Override
            public void onResponse(Call<List<Enemy>> call, Response<List<Enemy>> response) {
                pb_circular.setVisibility(View.GONE);
                if(response.body().isEmpty()){
                    Toast.makeText(EnemyActivity.this,"No enemies available",Toast.LENGTH_LONG);
                }
                else{
                    enemyList=response.body();
                    if(mAdapter == null){
                        mAdapter = new EnemyAdapter(enemyList);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.SetOnItemClickListener(new EnemyAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                enemyStats(position);
                            }
                        });
                    }
                    else{
                        mAdapter = null;
                        mAdapter = new EnemyAdapter(enemyList);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.SetOnItemClickListener(new EnemyAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                enemyStats(position);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Enemy>> call, Throwable t) {
                Toast.makeText(EnemyActivity.this,"Error",Toast.LENGTH_LONG);

            }
        });
    }
    private void enemyStats(int position){
        String stats= "Health: "+ enemyList.get(position).getHealth() +"\n Damage: "+enemyList.get(position).getDamage() +
                "\n Experience: " + enemyList.get(position).getExperience();
        final Dialog dialog=new Dialog(EnemyActivity.this);
        dialog.setContentView(R.layout.dialog_enemystats);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        LinearLayout back =dialog.findViewById(R.id.back);
        TextView text =dialog.findViewById(R.id.statstxt);
        TextView enemytxt=dialog.findViewById(R.id.textView7);
        enemytxt.setText(enemyList.get(position).getName());
        text.setText(stats);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public void onBackPressed() {
        exitDialog();
    }
    private void exitDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        LinearLayout cancel= dialog.findViewById(R.id.cancelbtn);
        LinearLayout exit=dialog.findViewById(R.id.button_back);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });
        dialog.show();

    }
}
