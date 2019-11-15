package id.student.catbreed.ui.favorites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.student.catbreed.R;
import id.student.catbreed.repository.model.ModelListBreed;
import id.student.catbreed.ui.home.activity.DetailCatActivity;
import id.student.catbreed.ui.home.adapter.CatAdapter;

public class FavoritesFragment extends Fragment implements CatAdapter.CatListener {
    private CatAdapter mCatAdapter;
    private List<ModelListBreed> model = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    View shimmer;
    String dataReciver = "";
    private FovoritesViewModel notificationsViewModel;
    private ModelListBreed mdata;
    String nerima = "";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Gson mdataGson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nerima = getArguments().getString("params");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(FovoritesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        mRecyclerView = root.findViewById(R.id.rvBReed);
//        mdata=new ModelListBreed();
        pref = requireContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        mdataGson = new Gson();
        shimmer = root.findViewById(R.id.shimmerLoad);
        shimmerFrameLayout = root.findViewById(R.id.shimmerLoad).findViewById(R.id.shimmer_view_leave);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = requireContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        Gson mgson = new Gson();
        String temporary = String.valueOf(pref.getString("data_arr", ""));
        if (!temporary.isEmpty()) {
            ModelListBreed[] objectArry = mgson.fromJson(temporary, ModelListBreed[].class);
            List<ModelListBreed> mlist = Arrays.asList(objectArry);
            model = mlist;
        }
        shimmerFrameLayout.setAutoStart(true);
        shimmerFrameLayout.startShimmerAnimation();
        mCatAdapter = new CatAdapter(model, R.layout.item_cat, requireContext(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mCatAdapter);
        shimmer.setVisibility(View.GONE);
    }

    @Override
    public void DetailCat(ModelListBreed modelListBreed) {
        Intent mdetail = new Intent(requireContext(), DetailCatActivity.class);
        mdetail.putExtra("data", mdataGson.toJson(modelListBreed));
        startActivity(mdetail);
    }

    @Override
    public void addFavorites(ModelListBreed modelListBreed) {
    }

    public void recive(String data) {
        Gson mgson = new Gson();
        mdata = mgson.fromJson(data, ModelListBreed.class);
        model.add(mdata);
    }
}