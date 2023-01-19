package in.msmartpay.agent.myWallet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.model.wallet.complaints.ComplaintHistoryModel;

public class ComplaintListAdapter extends RecyclerView.Adapter<ComplaintListAdapter.MyViewHolder> {
    private List<ComplaintHistoryModel> list;

    public ComplaintListAdapter(List<ComplaintHistoryModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_history_items, parent, false));
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
        TextView tv_complaint_id, tx_c_status, tv_c_service, tv_c_amt, tx_c_openDate, tv_c_txnDate,tv_c_query,
                tv_c_solution,tv_txn_id;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_complaint_id =  v.findViewById(R.id.tv_complaint_id);
            tx_c_status =  v.findViewById(R.id.tx_c_status);
            tv_c_service =  v.findViewById(R.id.tv_c_service);
            tv_c_amt =  v.findViewById(R.id.tv_c_amt);
            tx_c_openDate =  v.findViewById(R.id.tx_c_openDate);
            tv_c_txnDate =  v.findViewById(R.id.tv_c_txnDate);

            tv_c_query =  v.findViewById(R.id.tv_c_query);
            tv_c_solution =  v.findViewById(R.id.tv_c_solution);
            tv_txn_id =  v.findViewById(R.id.tv_txn_id);
        }

        public void bind(ComplaintHistoryModel historyModel) {

            if (historyModel != null) {
                tx_c_status.setText(historyModel.getStatus());
                tv_complaint_id.setText(historyModel.getTicketId());
                tv_c_service.setText(historyModel.getService());
                tv_c_amt.setText(historyModel.getTxnAmount());
                tx_c_openDate.setText("Open Date: "+historyModel.getOpenedDate()+"\n"+"Closed Date: "+historyModel.getClosedDate());
                tv_c_txnDate.setText("Txn Date: "+historyModel.getDot()+" "+historyModel.getTot());
                tv_c_query.setText(historyModel.getQueryMessage());
                tv_c_solution.setText(historyModel.getSolutionMessage());
                tv_txn_id.setText(historyModel.getTxnId());
            }
        }
    }
}
