package in.msmartpay.agent.ekobbps;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.model.ekobbps.OperatorCategory;

import java.util.ArrayList;
import java.util.List;


public class BillPayCategoryAdapter extends RecyclerView.Adapter<BillPayCategoryAdapter.MyViewHolder> implements Filterable {

    private List<OperatorCategory> list;
    private List<OperatorCategory> listFiltered;
    private MyListener listener;

    public BillPayCategoryAdapter(List<OperatorCategory> list, MyListener listener) {
        this.listener = listener;
        this.list = list;
        listFiltered = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.eko_bbps_payment_bank_search_item, parent, false));
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

        public void bind(OperatorCategory modal, MyListener listener) {
            tv_name.setText(modal.getOperatorCategoryName());
            itemView.setOnClickListener(v -> {
                listener.onSelected(modal);
            });
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
                    List<OperatorCategory> filteredList = new ArrayList<>();
                    for (OperatorCategory row : list) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getOperatorCategoryName().trim().toLowerCase().contains(charString.trim().toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (List<OperatorCategory>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public interface MyListener {
        void onSelected(OperatorCategory model);
    }
}
