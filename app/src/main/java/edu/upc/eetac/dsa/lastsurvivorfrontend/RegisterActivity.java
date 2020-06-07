package edu.upc.eetac.dsa.lastsurvivorfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import edu.upc.eetac.dsa.lastsurvivorfrontend.models.Player;
import edu.upc.eetac.dsa.lastsurvivorfrontend.services.PlayerService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private static Retrofit retrofit;
    Player player;private Context mContext;
    private static String retrofitIpAddress;
    private ProgressBar pb_circular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_register);
        pb_circular = findViewById(R.id.progressBar_cyclic);
        Button registerBtn = findViewById(R.id.RegisterBtn);mContext = this.getApplicationContext();
        Button goBack = findViewById(R.id.goBackBtn);
        ResourceFileReader rs =  new ResourceFileReader();
        retrofitIpAddress = ResourceFileReader.ReadResourceFileFromStringNameKey("retrofit.IpAddress",this);
        startRetrofit();
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


   public void onGoBackClicked(View view) {
       exitDialog();
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
    private Bitmap StringToBitmap(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream =  new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,90,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public void onRegisterClick(View view){
        EditText username = findViewById(R.id.input_username2);
        EditText password = findViewById(R.id.input_password2);
        pb_circular.setVisibility(View.VISIBLE);
        if (username.getText().toString().equals(null)||password.getText().toString().equals(null)||password.getText().toString().equals("Password")||username.getText().toString().equals("Username")){
            Toast.makeText(getApplicationContext(), "Error.Campos vacíos.", Toast.LENGTH_LONG).show();
        }

        else{
            PlayerService service = retrofit.create(PlayerService.class);
            player = new Player();
            player.setUsername(username.getText().toString());
            player.setPassword(password.getText().toString());
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.userlogo);
            icon = getResizedBitmap(icon,200,200);
            player.setAvatar(imageToString(icon));
            service.signUp(player).enqueue(new Callback<Player>() {
                @Override

                public void onResponse(Call<Player> call, Response<Player> response) {
                    pb_circular.setVisibility(View.GONE);
                    if (response.code() == 201) {
                        Toast.makeText(getApplicationContext(), "Signed Up successfully", Toast.LENGTH_LONG).show();
                        player = response.body();
                        //Close Register and return result to login which will also close the activity for splash
                        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("Username",player.getUsername());
                        editor.putString("Password",player.getPassword());
                        editor.putString("Id",player.getId());
                        editor.commit();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("Player",player);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                    else if (response.code() == 404){
                        Toast.makeText(getApplicationContext(),"Couldn't Register...",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                        Toast.makeText(getApplicationContext(),"Failed...",Toast.LENGTH_LONG).show();
                }
            });

        }



    }
}
