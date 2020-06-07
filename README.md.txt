
Things to Do inside unity to intrage with Android
In Build Settings
    #Publish Settings inside Player
        Build --> Custom Android Manifest(Modified to Exclude the Intent) & Custom Gradle Template
        OtherSettings --> Minimum API Level to same as Android application & Configuration Mono .Net 2.0 and armv7
    #Build Settings --> Export Tick Yes!
    Export like this--> inside where the Android_application_is_located/unityExport/ (create the folder if necessary!)
    //If necessary and need to change the directory also change below in the projectDir the location of the Unity Export Folder
Importat Things to do in Android to integrate Unity with Android
#Append in String.xml inside the values folder of the application 
<string name="game_view_content_description"></string>

#No need to change anything in Andorid Manifest aldready taken care of from unity manifest

 UnityPlayerActivity extends Activity //Remmember to change it to like this! and inside the method recover
public void receivePlayerStats(String str)
    {//Recieved Str
        Log.e("Unity Player Activity:","Recievied String: "+str);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("playerStats",str);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
    Build Gradle Both App and UnityLibrary
    android {
    ...
    defaultConfig {
        ndk {
            abiFilters 'armeabi-v7a'
        }
    }
    //Build Gradle App
    implementation project(':unityLibrary')
    //Also copy the Android settings of UnityLibrary Android to Build Gradle LastSurvivorApp Most of the Things such as 
    lintOptions,aaptOptions,packagingOptions,defaultConfig compleSDK and build Tools Version

    //Settings gradle App
    include ':unityLibrary'
    project(':unityLibrary').projectDir=new File('unityExport\\unityLibrary')
    //Build Gradle project
    allprojects {
        repositories {
            google()
            jcenter()
            flatDir {
                dirs "${project(':unityLibrary').projectDir}/libs"
            }
        }
    }
