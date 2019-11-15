package id.student.catbreed.ui.home.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import id.student.catbreed.AppConstants;

public class DetailActivityViewModel extends ViewModel {
    private MutableLiveData<String> UrlImage;

    public DetailActivityViewModel() {
        UrlImage = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return UrlImage;
    }

    public void getDataImage(String key) {
        final Gson gson = new Gson();
        AndroidNetworking.get(AppConstants.APP_END_POINT_IMAGE)
                .addQueryParameter("breed_id", key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String urls = response.getJSONObject(0).getString("url");
                            UrlImage.setValue(urls.isEmpty() ? "" : urls);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
//                        Log.e();
                    }
                });
    }
}