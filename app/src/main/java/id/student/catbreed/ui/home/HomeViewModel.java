package id.student.catbreed.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import id.student.catbreed.AppConstants;
import id.student.catbreed.repository.model.ModelListBreed;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private MutableLiveData<List<ModelListBreed>> modelListBreeds = new MutableLiveData<>();

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<ModelListBreed>> getList() {
        return modelListBreeds;
    }

    public void getDataBreeds(String key) {
        final Gson gson = new Gson();
        AndroidNetworking.get(AppConstants.APP_END_POINT_BREED)
                .addQueryParameter("q", key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ModelListBreed[] objectArry = gson.fromJson(String.valueOf(response), ModelListBreed[].class);
                        List<ModelListBreed> mlist = Arrays.asList(objectArry);
                        modelListBreeds.setValue(mlist);
                    }

                    @Override
                    public void onError(ANError error) {

                    }
                });
    }
}