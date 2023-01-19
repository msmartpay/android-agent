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
import com.aepssdkssz.network.model.BiometricDevice;
import com.aepssdkssz.network.model.fingpayonboard.FingpayStateData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FingpayStateSearchAdapter extends RecyclerView.Adapter<FingpayStateSearchAdapter.MyViewHolder> implements Filterable {

    private List<FingpayStateData> list;
    private List<FingpayStateData> listFiltered;
    private FingpayStateListener listener;

    public FingpayStateSearchAdapter(List<FingpayStateData> list, FingpayStateListener listener) {
        this.listener = listener;
        this.list = list;
        listFiltered = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fingpay_state_search_item, parent, false));
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }

        public void bind(FingpayStateData stateModel, FingpayStateListener listener) {
            tv_name.setText(stateModel.getState());
            itemView.setOnClickListener(v -> {
                listener.onStateSelected(stateModel);
            });
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                List<FingpayStateData> filteredList = new ArrayList<>();
                if (charString.isEmpty()) {
                    filteredList = list;
                } else {

                    for (FingpayStateData row : list) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getState().trim().toLowerCase().contains(charString.trim().toLowerCase())) {
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
                listFiltered = (List<FingpayStateData>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public interface FingpayStateListener {
        void onStateSelected(FingpayStateData model);
    }
}
