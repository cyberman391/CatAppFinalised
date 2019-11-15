package id.student.catbreed.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.student.catbreed.R;
import id.student.catbreed.repository.model.ModelListBreed;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> {

    public List<ModelListBreed> models;
    private int rowLayout;
    private Context context;
    private CatListener mListener;

    public CatAdapter(List<ModelListBreed> model, int rowLayout, Context context, CatListener mlist) {
        this.mListener = mlist;
        this.models = model;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public void updateEmployeeListItems(List<ModelListBreed> employees) {
        final EmployeeDiffCallback diffCallback = new EmployeeDiffCallback(this.models, employees);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.models.clear();
        this.models.addAll(employees);
        diffResult.dispatchUpdatesTo(this);
    }

    public List<ModelListBreed> getList() {
        return models;
    }

    public void addItems(List<ModelListBreed> cartList) {
        models.clear();
        models.addAll(cartList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        models.clear();
        notifyDataSetChanged();
    }
    //A view holder inner class where we get reference to the views in the layout using their ID

    public static class CatViewHolder extends RecyclerView.ViewHolder {
        CardView CvLayout;
        TextView catTitle;
        TextView catTipe;

        public CatViewHolder(View v) {
            super(v);
            CvLayout = v.findViewById(R.id.cardviewList);
            catTitle = v.findViewById(R.id.TvTitleCat);
            catTipe = v.findViewById(R.id.TvTipe);
        }
    }


    @Override
    public CatAdapter.CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new CatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, final int position) {
        final ModelListBreed model = models.get(position);
        holder.catTitle.setText(model.getName());
        holder.catTipe.setText(model.getTemperament());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.DetailCat(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public interface CatListener {
        void DetailCat(ModelListBreed modelListBreed);

        void addFavorites(ModelListBreed modelListBreed);
    }
}
