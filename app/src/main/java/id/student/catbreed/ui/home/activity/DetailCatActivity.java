package id.student.catbreed.ui.home.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.student.catbreed.R;
import id.student.catbreed.repository.model.ModelListBreed;

public class DetailCatActivity extends AppCompatActivity {
    /**
     * Nameofcatbreed.
     * o Imageofthecatbreed. o Description
     * o Weight
     * o Temperament
     * o Origin
     * o Lifespan
     * o Wikipedialink
     * o Dogfriendlinesslevel.
     */
    private AppCompatTextView TvTitleCat;
    private ModelListBreed mdata;
    private Gson mgson;
    private AppCompatTextView TvDescription;
    private ImageView imvCover;
    private DetailActivityViewModel mViewModel;
    private AppCompatTextView TvInOrigin;
    private AppCompatTextView TvInWeight;
    private AppCompatTextView TvInTemprament;
    private AppCompatTextView TvInLifespan;
    private AppCompatTextView TvInDogFriends;
    private AppCompatTextView TvInWiki;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        mViewModel = ViewModelProviders.of(this).get(DetailActivityViewModel.class);
        initView();
        mgson = new Gson();
        Bundle mdataIn = getIntent().getExtras();
        if (mdataIn != null) {
            String data = mdataIn.getString("data");
            mdata = mgson.fromJson(data, ModelListBreed.class);
            TvTitleCat.setText(mdata.getName());
            TvDescription.setText(mdata.getDescription());
            TvInOrigin.setText(mdata.getOrigin());
            TvInDogFriends.setText(Integer.toString(mdata.getDog_friendly()));
            TvInLifespan.setText(mdata.getLife_span());
            TvInTemprament.setText(mdata.getTemperament().isEmpty() ? "-" : mdata.getTemperament());
            TvInWiki.setText(mdata.getWikipedia_url().isEmpty() ? "-" : mdata.getWikipedia_url());
            if (mdata.getWikipedia_url() != null) {
                TvInWiki.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OpenLink(mdata.getWikipedia_url());
                    }
                });
            }
            TvInWeight.setText("Imperial :" + mdata.getWeight().getImperial() + "\n Metric :" + mdata.getWeight().getMetric());
            mViewModel.getDataImage(mdata.getId());
        }

        mViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null)
                    Glide.with(DetailCatActivity.this).load(s).placeholder(R.drawable.cat_default).into(imvCover);
            }
        });
    }


    private void initView() {
        TvTitleCat = (AppCompatTextView) findViewById(R.id.TvTitleCat);
        TvDescription = (AppCompatTextView) findViewById(R.id.TvDescription);
        imvCover = (ImageView) findViewById(R.id.imv_cover);
        TvInOrigin = (AppCompatTextView) findViewById(R.id.TvInOrigin);
        TvInWeight = (AppCompatTextView) findViewById(R.id.TvInWeight);
        TvInTemprament = (AppCompatTextView) findViewById(R.id.TvInTemprament);
        TvInLifespan = (AppCompatTextView) findViewById(R.id.TvInLifespan);
        TvInDogFriends = (AppCompatTextView) findViewById(R.id.TvInDogFriends);
        TvInWiki = (AppCompatTextView) findViewById(R.id.TvInWiki);
    }

    void OpenLink(String URL) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(URL));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorit, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }else if (item.getItemId() == R.id.appFavorite){
            Gson mgson = new Gson();
            //check
            boolean iSequal = false;
            String dataArray = pref.getString("data_arr", "");
//            ModelListBreed mdata = mgson.fromJson(modelListBreeds, ModelListBreed.class);
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
        return super.onOptionsItemSelected(item);
    }
}
