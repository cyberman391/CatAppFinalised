package id.student.catbreed;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.student.catbreed.repository.model.ModelListBreed;
import id.student.catbreed.ui.favorites.FavoritesFragment;
import id.student.catbreed.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnHeadlineSelectedListener {
    BottomNavigationView navView;
    FragmentManager fragmentManager;
    public final String TAG = MainActivity.this.getClass().getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(bottomNavigationView);
        LoadHomeFragment();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
    }

    private void LoadHomeFragment() {
        // update the main content by replacing fragments
        Fragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment, "home_tag");
        fragmentTransaction.commitAllowingStateLoss();
    }

    BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationView = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    HomeFragment f_home = new HomeFragment();
                    onchangeSegmentFragment(f_home, "home_tag");
                    return true;
                case R.id.navigation_notifications:
                    FavoritesFragment f_fav = new FavoritesFragment();
                    onchangeSegmentFragment(f_fav, "fav_tag");
                    return true;
            }
            return false;
        }
    };

    private void onchangeSegmentFragment(Fragment fragment, String Tag) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().
                disallowAddToBackStack().
                replace(R.id.nav_host_fragment, fragment, Tag).
                setCustomAnimations(R.anim.design_bottom_sheet_slide_in,
                        R.anim.design_bottom_sheet_slide_out).
                commit();
    }


    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            HomeFragment headlinesFragment = (HomeFragment) fragment;
            headlinesFragment.setOnHeadlineSelectedListener(this);
        }
    }

    @Override
    public void onReceiver(String modelListBreeds) {
        Gson mgson = new Gson();
        Bundle bundle = new Bundle();
        bundle.putString("data", modelListBreeds);
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        favoritesFragment.recive(modelListBreeds);
        //check
        boolean iSequal = false;
        String dataArray = pref.getString("data_arr", "");
        ModelListBreed mdata = mgson.fromJson(modelListBreeds, ModelListBreed.class);
        if (!dataArray.isEmpty()) {
            ModelListBreed[] objectArry = mgson.fromJson(dataArray, ModelListBreed[].class);
            List<ModelListBreed> mlist = Arrays.asList(objectArry);
            ArrayList<ModelListBreed> mlistArr = new ArrayList<>();
            mlistArr.addAll(mlist);
            for (int i = 0; i < mlist.size(); i++) {
                if (mlist.get(i).getId().equals(mdata.getId())) {
                    iSequal=true;
                }
            }
            if (iSequal) {
                Toast.makeText(this, "Your Cat is Exist !!", Toast.LENGTH_SHORT).show();
            } else {
                mlistArr.add(mdata);
                mlistArr.size();
                editor.putString("data_arr", mgson.toJson(mlistArr));
                editor.commit();
                Toast.makeText(this, "Success add to favorites..!!", Toast.LENGTH_SHORT).show();
            }
        } else {//new array
            List<ModelListBreed> mlist = new ArrayList<>();
            mlist.add(mdata);
            editor.putString("data_arr", mgson.toJson(mlist));
            editor.commit();
            Toast.makeText(this, "Success add to favorites..!!", Toast.LENGTH_SHORT).show();
        }
    }
}
