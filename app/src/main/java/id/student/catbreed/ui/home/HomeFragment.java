package id.student.catbreed.ui.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import id.student.catbreed.R;
import id.student.catbreed.repository.model.ModelListBreed;
import id.student.catbreed.ui.home.activity.DetailCatActivity;
import id.student.catbreed.ui.home.adapter.CatAdapter;

public class HomeFragment extends Fragment implements CatAdapter.CatListener {
    private HomeViewModel homeViewModel;
    private CatAdapter mCatAdapter;
    private List<ModelListBreed> model = new ArrayList<>();
    private ArrayList<ModelListBreed> modelFavorites = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    View shimmer, no_page;
    OnHeadlineSelectedListener callback;
    Gson mdata;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = root.findViewById(R.id.rvBReed);
        mdata = new Gson();
        modelFavorites = new ArrayList<>();
        shimmer = root.findViewById(R.id.shimmerLoad);
        shimmerFrameLayout = root.findViewById(R.id.shimmerLoad).findViewById(R.id.shimmer_view_leave);
        no_page = root.findViewById(R.id.noData);
        shimmerFrameLayout.setAutoStart(true);
        shimmerFrameLayout.startShimmerAnimation();

        mCatAdapter = new CatAdapter(model, R.layout.item_cat, requireContext(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mCatAdapter);
        homeViewModel.getList().observe(this, new Observer<List<ModelListBreed>>() {
            @Override
            public void onChanged(List<ModelListBreed> modelListBreeds) {
                shimmer.setVisibility(View.GONE);
                mCatAdapter.addItems(modelListBreeds);
                if (mRecyclerView.getVisibility() == View.GONE) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                no_page.setVisibility(View.GONE);
            }
        });
        return root;
    }

    private void SearchData(String key) {
        no_page.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        shimmer.setVisibility(View.VISIBLE);
        homeViewModel.getDataBreeds(key);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_toolbar, menu);
        MenuItem searchItem = menu.findItem(R.id.appSearchBar);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!newText.isEmpty()) {
                        SearchData(newText);
                    }else{
                        mCatAdapter.clearItems();

                        mRecyclerView.setVisibility(View.GONE);
                        no_page.setVisibility(View.VISIBLE);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String newText) {
                    if (!newText.isEmpty()) {
                        SearchData(newText);
                    }else{
                      mCatAdapter.clearItems();

                        mRecyclerView.setVisibility(View.GONE);
                      no_page.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appSearchBar:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void DetailCat(ModelListBreed modelListBreed) {
        Intent mdetail = new Intent(requireContext(), DetailCatActivity.class);
        mdetail.putExtra("data", mdata.toJson(modelListBreed));
        startActivity(mdetail);
    }

    @Override
    public void addFavorites(ModelListBreed modelListBreed) {
        Gson a = new Gson();
        String sendit = a.toJson(modelListBreed);
        callback.onReceiver(sendit);
    }

    public void setOnHeadlineSelectedListener(OnHeadlineSelectedListener callback) {
        this.callback = callback;
    }

    public interface OnHeadlineSelectedListener {
        public void onReceiver(String modelListBreeds);
    }
}