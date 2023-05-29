package in.msmartpay.agent.dmr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.model.dmr.BankListModel;

import java.util.ArrayList;

public class BankSearchAdapter extends RecyclerView.Adapter<BankSearchAdapter.MyViewHolder> implements Filterable {
        private Context context;

       private ArrayList<BankListModel> bankList;
    private ArrayList<BankListModel> bankListFiltered;
       private BankListener listener;
       

        public BankSearchAdapter(Context context, ArrayList<BankListModel> bankList, BankListener listener) {
            this.context = context;
            this.bankList = bankList;
            bankListFiltered = bankList;
            this.listener = listener;
        }



    class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_name;


            MyViewHolder(View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_name);
            }

            public void bind(BankListModel model) {

                tv_name.setText(model.getBankName());
                itemView.setOnClickListener(v -> {
                    listener.onBankSelected(model);
                });

            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

           View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dmr_bank_search_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(bankListFiltered.get(position));
            holder.setIsRecyclable(false);

        }

        @Override
        public int getItemCount() {
            return bankListFiltered.size();
        }
        
       public interface BankListener{
            void onBankSelected(BankListModel model);
        }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    bankListFiltered = bankList;
                } else {
                    ArrayList<BankListModel> filteredList = new ArrayList<>();
                    for (BankListModel row : bankList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBankName().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    bankListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = bankListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                bankListFiltered = (ArrayList<BankListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    }