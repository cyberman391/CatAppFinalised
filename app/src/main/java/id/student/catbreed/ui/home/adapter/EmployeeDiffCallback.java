package id.student.catbreed.ui.home.adapter;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import id.student.catbreed.repository.model.ModelListBreed;

public class EmployeeDiffCallback extends DiffUtil.Callback {

    private final List<ModelListBreed> mOldEmployeeList;
    private final List<ModelListBreed> mNewEmployeeList;

    public EmployeeDiffCallback(List<ModelListBreed> oldEmployeeList, List<ModelListBreed> newEmployeeList) {
        this.mOldEmployeeList = oldEmployeeList;
        this.mNewEmployeeList = newEmployeeList;
    }

    @Override
    public int getOldListSize() {
        return mOldEmployeeList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewEmployeeList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldEmployeeList.get(oldItemPosition).getId() == mNewEmployeeList.get(
                newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final ModelListBreed oldEmployee = mOldEmployeeList.get(oldItemPosition);
        final ModelListBreed newEmployee = mNewEmployeeList.get(newItemPosition);

        return oldEmployee.getName().equals(newEmployee.getName());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
