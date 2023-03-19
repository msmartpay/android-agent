package in.msmartpayagent.dialogs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.msmartpayagent.R;
import in.msmartpayagent.network.model.fastag.FastagOperator;


public class FastagOperatorSearchAdapter extends RecyclerView.Adapter<FastagOperatorSearchAdapter.MyViewHolder> implements Filterable {

    private List<FastagOperator> list;
    private List<FastagOperator> listFiltered;
    private OperatorListener listener;

    public FastagOperatorSearchAdapter(List<FastagOperator> list, OperatorListener listener) {
        this.listener = listener;
        this.list = list;
        listFiltered = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dmr_bank_search_item, parent, false));
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

        public void bind(FastagOperator fastagOperator, OperatorListener listener) {
            tv_name.setText(fastagOperator.getName());
            itemView.setOnClickListener(v -> {
                listener.onOperatorSelected(fastagOperator);
            });
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                List<FastagOperator> filteredList = new ArrayList<>();
                if (charString.isEmpty()) {
                    filteredList = list;
                } else {

                    for (FastagOperator row : list) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().trim().toLowerCase().contains(charString.trim().toLowerCase())) {
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
                listFiltered = (List<FastagOperator>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public interface OperatorListener {
        void onOperatorSelected(FastagOperator model);
    }
}
