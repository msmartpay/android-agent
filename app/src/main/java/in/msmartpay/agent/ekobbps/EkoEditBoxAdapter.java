package com.scinfotech.ekobbps;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.scinfotech.R;
import com.scinfotech.network.model.ekobbps.OperatorParameter;
import com.scinfotech.utility.Util;

import java.util.List;

public class EkoEditBoxAdapter extends RecyclerView.Adapter<EkoEditBoxAdapter.MyViewHolder> {

    private List<OperatorParameter> list;
    private MyListener listener;

    public EkoEditBoxAdapter(List<OperatorParameter> list, MyListener listener) {
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_payment_eko_edit_box_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.bind(list.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText et_data;
        TextInputLayout til_bb;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            et_data = itemView.findViewById(R.id.et_data);
            til_bb = itemView.findViewById(R.id.til_bb);
        }

        public void bind(OperatorParameter modal, int poss, MyListener listener) {
            til_bb.setHint(modal.getParamLabel());
            et_data.setInputType(Util.getInputType(modal.getParamType()));
            et_data.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    listener.onTextChange(poss, s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    public interface MyListener {
        void onTextChange(int poss, String text);
    }
}
