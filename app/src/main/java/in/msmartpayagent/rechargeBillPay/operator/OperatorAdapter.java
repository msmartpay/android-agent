package in.msmartpayagent.rechargeBillPay.operator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import in.msmartpayagent.R;
import in.msmartpayagent.network.model.wallet.OperatorModel;

import java.util.ArrayList;
import java.util.List;


public class OperatorAdapter extends RecyclerView.Adapter<OperatorAdapter.MyViewHolder> implements Filterable {
    private List<OperatorModel> list;
    private List<OperatorModel> listFiltered;
    private OperatorListener listener;


    public OperatorAdapter(List<OperatorModel> list, OperatorListener listener) {
        this.list = list;
        listFiltered = list;
        this.listener = listener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;


        MyViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.custom_spinner);
        }

        public void bind(OperatorModel model) {

            tv_name.setText(model.getOperatorName());
            itemView.setOnClickListener(v -> {
                listener.onOperatorSelected(model);
            });

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_single_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(listFiltered.get(position));
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }

    public interface OperatorListener {
        void onOperatorSelected(OperatorModel model);
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
                    List<OperatorModel> filteredList = new ArrayList<>();
                    for (OperatorModel row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getOperatorName().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (List<OperatorModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}