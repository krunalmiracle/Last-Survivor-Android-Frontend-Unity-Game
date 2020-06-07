package edu.upc.eetac.dsa.lastsurvivorfrontend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import edu.upc.eetac.dsa.lastsurvivorfrontend.models.Player;
import edu.upc.eetac.dsa.lastsurvivorfrontend.services.PlayerService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {
    //TOKEN
    private boolean temp_token = false;
    // RETROFIT OBJECT
    private static Retrofit retrofit;
    private static String retrofitIpAddress;
    //Player Service Object
    PlayerService playerService;
    //Player Objects
    Player player = new Player();
    //TextView of Splash
    private ProgressBar pb_circular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb_circular = findViewById(R.id.progressBar_cyclic);
        //After launch check if a user exists in preferences and also set the player
        if(!ExistPlayerAndSetData()){
            LaunchLoginActivity();
        }else{
            //Connect with retrofit & Login Automatically and Retrieve the Player Data
            try{
                ResourceFileReader rs =  new ResourceFileReader();
                retrofitIpAddress = ResourceFileReader.ReadResourceFileFromStringNameKey("retrofit.IpAddress",this);
                startRetrofit();
                playerService = retrofit.create(PlayerService.class);
                //Login with Player Object player which was written using (ExistPlayerAndSetData)
                LoginUser();
            }catch(Exception e){
                //Not possible to connect to server
                e.printStackTrace();
                NotifyUser("Can't Connect to Server!");
            }
        }
        pb_circular.setVisibility(View.GONE);
    }
    private boolean ExistPlayerAndSetData(){
        //Access the shared preference UserInfo and obtain the parameters, default =string empty
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        player.setUsername(settings.getString("Username", ""));
        player.setPassword(settings.getString("Password", ""));
        player.setId(settings.getString("Id", ""));
        //return !player.getId().equals("");
        return true;
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //If the focus has been changed,means we are on the activity than hide the bar again behind the activity
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /* Shows the system bars by removing all the flags
        except for the ones that make the content appear under the system bars.*/
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void onStartGameClicked(View view){
        pb_circular.setVisibility(View.VISIBLE);
        //Launch Unity Game and after starting the game also get the data back from the unity to update the server
        NotifyUser("Game Started: " + player.getUsername());
        //Start Game Activity!
        Intent intent = new Intent(MainActivity.this , gameActivity.class);
        startActivityForResult(intent,4);
        pb_circular.setVisibility(View.GONE);
    }
    public void onButtonEnemiesClick(View view){
        pb_circular.setVisibility(View.VISIBLE);
        Intent inte = new Intent(MainActivity.this, EnemyActivity.class);
        startActivity(inte);
        pb_circular.setVisibility(View.GONE);
    }
    public void onForumClicked(View view){
        //Here the press of the Button Forum

    }
    public void onInventoryClicked(View view){
        //Inventory Part

    }
    public void onButtonRedirectWebClick(View view){
        pb_circular.setVisibility(View.VISIBLE);
        //Launch Unity Game and after starting the game also get the data back from the unity to update the server
        NotifyUser("Web Redirect: " + player.getUsername());
        String url = "http://"+retrofitIpAddress+":8080/LastSurvivor/Register/main.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        pb_circular.setVisibility(View.GONE);
    }
    public void onButtonModifyProfileClick(View view){

        pb_circular.setVisibility(View.VISIBLE);
        //Launch Unity Game and after starting the game also get the data back from the unity to update the server
        Intent intent = new Intent(MainActivity.this , ProfileActivity.class);
        intent.putExtra("Player",player);
        startActivityForResult(intent,3);
        pb_circular.setVisibility(View.GONE);
    }
    public void onSignOutClicked(View view){
        pb_circular.setVisibility(View.VISIBLE);
        //Launch Unity Game and after starting the game also get the data back from the unity to update the server
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Username","");
        editor.putString("Password","");
        editor.putString("Id","");
        editor.commit();
        //After logout from phone, set the screen to LoginActivity
        LaunchLoginActivity();
        pb_circular.setVisibility(View.GONE);
    }
    public void onCurrentRankingClicked(View view){
        pb_circular.setVisibility(View.VISIBLE);
        Intent intent = new Intent(MainActivity.this ,RankingActivity.class);
        startActivityForResult(intent,2);
        pb_circular.setVisibility(View.GONE);
    }
    private void LaunchLoginActivity() {
        pb_circular.setVisibility(View.VISIBLE);
        Intent intent = new Intent(MainActivity.this ,LoginActivity.class);
        startActivityForResult(intent,1);
        pb_circular.setVisibility(View.GONE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If login activity closed means the user has logged in, and the data is stored in the database
        pb_circular.setVisibility(View.VISIBLE);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                player = data.getParcelableExtra("Player");
                Log.w("s","Test");

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }

        }
        //Profile Modified request code 3
        if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                player = data.getParcelableExtra("Player");
                //Retrieved updated data from Profile Modified
                Log.w("Main Modify",player.getAvatar());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
               //Do nothing as nothing updated
            }
        }
        //Profile Modified request code 3
        if (requestCode == 4) {
            if(resultCode == Activity.RESULT_OK){
                player = data.getParcelableExtra("Player");
                //Retrieved updated data from Profile Modified
                Log.w("Game Activity Result",player.toString());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Do nothing as nothing updated AFTER GAME Play
            }
        }
        pb_circular.setVisibility(View.GONE);
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
    private void LoginUser(){
        String username,password;
        username =  player.getUsername();
        password =  player.getPassword();
        pb_circular.setVisibility(View.VISIBLE);
        if(username == null|| password== null){
            NotifyUser("Please fill the fields!");
        }else if(username.isEmpty()|| password.isEmpty()){
            NotifyUser("Please Type something other than space");
        }else if(username.contains(" ")|| password.contains(" ")){
            NotifyUser("Please Type that doesn't contain spaces");
        }else{
            //Now we can send the data to Server and ask for login
            String TAG = "onSignIn";

            try {

                player = new Player(username,password,0,0,0,0);
                player.setUsername(username);player.setPassword(password);
                Call<Player> playerID = playerService.signIn(player);
                Gson gson = new Gson();
                String jsonInString = gson.toJson(player);
                Log.d(TAG, "PlayerGson: "+jsonInString);
                Log.d(TAG, "Player Logging In from Splash: "+playerID.toString());
                /* Android Doesn't allow synchronous execution of Http Request and so we must put it in queue*/
                playerID.enqueue(new Callback<Player>() {
                    @Override
                    public void onResponse(Call<Player> call, Response<Player> response) {
                        Log.d(TAG, "Player Logging In from splash response Server: "+call.toString());
                        pb_circular.setVisibility(View.GONE);
                        //SingIn Successful
                        if (response.code() == 201) {
                            //Successful we can get the ID, and call again to ask for PLayer
                            if(response.isSuccessful()){
                                player =  response.body();
                                Log.d(TAG,"The Player ID is: "+player.getId());
                            }else{ Log.d(TAG,"Something went horribly wrong!");}
                        } else if(response.code() == 404){ // Not Found User
                            NotifyUser("Unsuccessful!");
                        }else if(response.code() == 401){ //Incorrect Password
                            NotifyUser("Incorrect Password");
                        }else{
                            NotifyUser("Something went horribly wrong!");
                        }
                    }
                    @Override
                    public void onFailure(Call<Player> call, Throwable t) {
                        pb_circular.setVisibility(View.GONE);
                        NotifyUser("Failure to logIn");
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //User Notifier Handler using Toast
    private void NotifyUser(String showMessage){
        Toast toast = Toast.makeText(MainActivity.this,showMessage,Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    public void onBackPressed() {
        // do something on back.
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
