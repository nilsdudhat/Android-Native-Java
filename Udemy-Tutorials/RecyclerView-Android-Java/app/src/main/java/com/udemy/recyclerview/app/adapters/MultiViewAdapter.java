package com.udemy.recyclerview.app.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.recyclerview.app.databinding.ItemCallBinding;
import com.udemy.recyclerview.app.databinding.ItemEmailBinding;
import com.udemy.recyclerview.app.models.MultiEmployeeModel;

import java.util.ArrayList;

public class MultiViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static int VIEW_TYPE_EMAIL = 0;
    static int VIEW_TYPE_CALL = 1;

    ArrayList<MultiEmployeeModel> multiEmployeeModelArrayList = new ArrayList<>();

    public MultiViewAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CALL) {
            ItemCallBinding binding = ItemCallBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new CallViewHolder(binding);
        }
        ItemEmailBinding binding = ItemEmailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EmailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MultiEmployeeModel model = multiEmployeeModelArrayList.get(position);

        if (getItemViewType(position) == VIEW_TYPE_CALL) {
            CallViewHolder viewHolder = (CallViewHolder) holder;
            viewHolder.binding.txtMobileNumber.setText(model.getMobileNumber());
        }

        if (getItemViewType(position) == VIEW_TYPE_EMAIL) {
            EmailViewHolder viewHolder = (EmailViewHolder) holder;
            viewHolder.binding.txtEmail.setText(model.getEmail());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.isEmpty(multiEmployeeModelArrayList.get(position).getEmail())) {
            return VIEW_TYPE_CALL;
        } else {
            return VIEW_TYPE_EMAIL;
        }
    }

    @Override
    public int getItemCount() {
        return multiEmployeeModelArrayList.size();
    }

    public void setMultiEmployeeModelArrayList(ArrayList<MultiEmployeeModel> multiEmployeeModelArrayList) {
        this.multiEmployeeModelArrayList = multiEmployeeModelArrayList;
        notifyDataSetChanged();
    }

    private static class EmailViewHolder extends RecyclerView.ViewHolder {

        ItemEmailBinding binding;

        public EmailViewHolder(@NonNull ItemEmailBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    private static class CallViewHolder extends RecyclerView.ViewHolder {

        ItemCallBinding binding;

        public CallViewHolder(@NonNull ItemCallBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
