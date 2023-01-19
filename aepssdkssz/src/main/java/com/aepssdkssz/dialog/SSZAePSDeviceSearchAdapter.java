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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class SSZAePSDeviceSearchAdapter extends RecyclerView.Adapter<SSZAePSDeviceSearchAdapter.MyViewHolder> implements Filterable {

    private List<BiometricDevice> list;
    private List<BiometricDevice> listFiltered;
    private DeviceListener listener;

    public SSZAePSDeviceSearchAdapter(List<BiometricDevice> list, DeviceListener listener){
        this.listener = listener;
        this.list = list;
        listFiltered = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ssz_aeps_search_item_img, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(list.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        ImageView iv_img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            iv_img=itemView.findViewById(R.id.iv_img);
        }

        public void bind(final BiometricDevice deviceModel, final DeviceListener listener) {
            tv_name.setText(deviceModel.getDevice_name());
            Picasso.get().load(deviceModel.getImage()).into(iv_img);
            itemView.setOnClickListener(view -> listener.onDeviceSelected(deviceModel));
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = list;
                } else {
                    List<BiometricDevice> filteredList = new ArrayList<>();
                    for (BiometricDevice row : list) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getDevice_name().trim().toLowerCase().contains(charString.trim().toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (List<BiometricDevice>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
   public interface DeviceListener{
        void onDeviceSelected(BiometricDevice deviceModel);
    }
}
