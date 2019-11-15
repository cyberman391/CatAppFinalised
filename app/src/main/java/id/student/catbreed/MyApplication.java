package id.student.catbreed;

import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    private Gson gson;
    @Override
    public void onCreate() {
        super.onCreate();
        //okhttpset
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(AppConstants.TIME_OUT_CONNECTION, TimeUnit.SECONDS)
                .readTimeout(AppConstants.TIME_OUT_CONNECTION, TimeUnit.SECONDS)
                .writeTimeout(AppConstants.TIME_OUT_CONNECTION, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.setLenient();
        gsonBuilder.serializeNulls();
        gson = gsonBuilder.create();
        AndroidNetworking.setParserFactory(new GsonParserFactory(gson));
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
