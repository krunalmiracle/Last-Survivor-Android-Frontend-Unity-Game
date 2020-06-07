package edu.upc.eetac.dsa.lastsurvivorfrontend;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceFileReader {

    public ResourceFileReader(){

    }
    public static String ReadResourceFileFromStringNameKey(String key, Context context){
        try {
            Properties properties = new Properties();
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("retrofit.properties");
            properties.load(inputStream);
            return properties.getProperty(key);
        }catch (IOException e){
            e.fillInStackTrace();
            return null;
        }
    }

}