package in.msmartpayagent.myWallet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.msmartpayagent.R;
import in.msmartpayagent.network.model.wallet.BalHistoryModel;

import java.util.List;

public class BalanceListAdapter extends RecyclerView.Adapter<BalanceListAdapter.MyViewHolder> {
    private List<BalHistoryModel> list;

    public BalanceListAdapter(List<BalHistoryModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_history_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, status, mode, refid, amount;

        public MyViewHolder(@NonNull View v) {
            super(v);
            date =  v.findViewById(R.id.cstmdate);
            time =  v.findViewById(R.id.cstmtime);
            mode =  v.findViewById(R.id.cstmmode);
            refid =  v.findViewById(R.id.cstmrefid);
            amount =  v.findViewById(R.id.cstmamount);
            status =  v.findViewById(R.id.cstmstatus);
        }

        public void bind(BalHistoryModel historyModel) {

            if (historyModel != null) {
                status.setText(historyModel.getStatus());
                mode.setText("Remark : " + historyModel.getMode());
                time.setText(historyModel.getTime());
                date.setText(historyModel.getDate());
                refid.setText(historyModel.getRefId());
                amount.setText(historyModel.getAmount());
            }
        }
    }
}
