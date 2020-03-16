package com.equipu.profeplus;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.util.Log;

import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.lang.reflect.Field;
import java.util.Locale;


public class LearnApp extends Application {

    private Tracker mTracker;


    /*
    Estas constantes son usada por el app en cada actividad
     */
    public static final int BTN_APP = 0Xff69990d;
    public final static int TIMEOUT = 60000;// 60 seconds
    public final static int READTIMEOUT = 10000; /// 10 seconds
    public final static boolean SETUP_NETWORK = true;

    public final static int MSG_BIRTH_DIALOG = 342;
    public final static int MSG_TIME_DIALOG = 311;
    public final static int MSG_CONFIRM_ONE_BUTTON_DIALOG = 222;
    public final static int MSG_CONFIRM_TWO_BUTTON_DIALOG = 367;
    public final static int MSG_ANSWER_DIALOG = 458;
    public final static int MSG_OWNER_LOGIN_DIALOG = 324;
    public final static int MSG_RECONNECT_DIALOG = 629;


    /*
    Estas constantes son usadas para hacer las llamadas al servidor
    Tener cuidado con el protocolo https usado para las peticiones
     */
    //php artisan serve --host 192.168.100.100 --port 8000
    //public static String baseURL = "http://192.168.100.100:8000";
    public static String baseURL = "https://www.profeplus.com";
    public static String traceURL = "https://www.profeplus.com/trace/server.php";
    public static String tokenURL = "?auth_token=";
    public static String mAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0";
    //public static String mAgent = System.getProperty("http.agent");

    /*
    Estas constantes son usadas para transferir datos entre actividades
     */
    public static String PCL_APP_STATE  = "AppStateParcel";
    public static String PCL_SESSION_PARCEL  = "SessionParcel";
    public static String PCL_TASK_PARCEL  = "TaskParcel";
    public static String PCL_USER_PARCEL  = "UserParcel";
    public static String PCL_TUTORIAL  = "Tutorial";
    public static String PCL_TASK_GUEST_PARCEL  = "TaskGuestParcel";
    public static String PCL_USER_GUEST_PARCEL  = "UserGuestParcel";
    public static String PCL_APP_STATE_GUEST  = "AppStateGuestParcel";
    public static String PCL_EVALUATION  = "EvaluationParcel";

    /*
    Estas constantes son usadas para definir las fuentes
    appFont : fuente de línea fina
    appFont1 : fuente negrita
    appFont2 : fuente de línea gruesa en negrita
     */
    public static Typeface appFont;
    public static Typeface appFont2;
    public static Typeface appFont1;

    public static boolean TASK_DONE;

    /*
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
    */



    @Override
    public void onCreate() {
        super.onCreate();

        /*
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        boolean wifiEnabled = wifiManager.isWifiEnabled();
        if (wifiEnabled) {
            WifiManager.WifiLock wifiLock =
                    wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "LockTag");
            wifiLock.acquire();
        }
        */

        TASK_DONE = false;
        String mLanguage = getApplicationContext().getResources().getConfiguration().locale.toString();
        Log.d("profeplus.lan",mLanguage);
        final Field staticField;
        try {
            appFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Medium.ttf");
            appFont2 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Black.ttf");
            appFont1 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Bold.ttf");
            staticField = Typeface.class.getDeclaredField("DEFAULT");
            staticField.setAccessible(true);
            staticField.set(null, appFont);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        /*
        ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        am.killBackgroundProcesses("com.facebook.orca");
        am.killBackgroundProcesses("com.facebook.katana");
        am.killBackgroundProcesses("com.facebook.android");
        am.killBackgroundProcesses("com.whatsapp-1");
        am.killBackgroundProcesses("com.whatsapp");
        */

    }

    public static void setLanguage(Context context, String lan){
        Locale locale = new Locale(lan);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }

    /*
    Este método configura la librería para cargar imágenes eficientemente
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static String getAppVersion(){
        String vers = String.format("Profeplus v%s Build %d",
                BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        return vers;
    }


}
