package com.aepssdkssz.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aepssdkssz.R;
import com.aepssdkssz.network.model.BankModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class SSZAePSBankPreferredAdapter extends RecyclerView.Adapter<SSZAePSBankPreferredAdapter.MyViewHolder> implements Filterable {

    private List<BankModel> list;
    private List<BankModel> listFiltered;
    private BankListener listener;

    public SSZAePSBankPreferredAdapter(List<BankModel> list, BankListener listener) {
        this.listener = listener;
        this.list = list;
        listFiltered = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ssz_aeps_search_item_img2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(listFiltered.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView iv_img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
            tv_name = itemView.findViewById(R.id.tv_name);
        }

        public void bind(final BankModel bankModel, final BankListener listener) {
            tv_name.setText(bankModel.getBank());
            Picasso.get().load(bankModel.getImage()).into(iv_img);
            itemView.setOnClickListener(view -> listener.onBankSelected(bankModel));
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                List<BankModel> filteredList = new ArrayList<>();
                if (charString.isEmpty()) {
                    filteredList = list;
                } else {

                    for (BankModel row : list) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBank().trim().toLowerCase().contains(charString.trim().toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (List<BankModel>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public interface BankListener {
        void onBankSelected(BankModel bankModel);
    }
}
