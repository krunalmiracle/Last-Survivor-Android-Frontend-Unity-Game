package edu.upc.eetac.dsa.lastsurvivorfrontend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.unity3d.player.UnityPlayerActivity;
public class gameActivity extends AppCompatActivity{
    boolean isLaunchingFirstTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        launchingFirstTime();

    }
    private void launchingFirstTime(){
        if(isLaunchingFirstTime){
            String mapData = "EEEE;" +
                    "EFFE;" +
                    "EFFE;" +
                    "EEEE";
            //Unity with MapData, playerData,ObjectData
            Intent intent = new Intent(this, UnityPlayerActivity.class);
            //PlayerStats String: P,Level,Exp,Kills,Coins;Sword;axe;katana;baton;hammer where weapons has Damage,Hitrange,attackCooldown
            intent.putExtra("playerData", "P,3,50,50,50;S,4,1.5,1.5");
            intent.putExtra("objectData", "P,1,1;C,3,3,1,1,2");
            intent.putExtra("mapData", mapData);
            startActivityForResult(intent,0);
            isLaunchingFirstTime =false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If login activity closed means the user has logged in, and the data is stored in the database
        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK){
                //Get the Unity Activity Data!
                String playerStats = data.getStringExtra("playerStats");
                Log.w("Close Game","Recieved Stats: "+playerStats);
                //Close the GameActivity
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }

        }
    }
}
