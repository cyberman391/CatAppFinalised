package id.student.catbreed.ui.favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FovoritesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FovoritesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is favorites fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}