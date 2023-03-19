package in.msmartpayagent.audit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.msmartpayagent.R;
import in.msmartpayagent.network.model.audit.AuditRequest;
import in.msmartpayagent.network.model.audit.Transaction;

public class AuditTxnAdapter extends RecyclerView.Adapter<AuditTxnAdapter.ViewHolder> {
    private List<Transaction> list;
    private Context context;

    public AuditTxnAdapter(Context context, List<Transaction> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audit_txn_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.bind(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_txn_id, tv_date,tv_service_name,tv_amt,tv_rrn,tv_remark;
        public ViewHolder(View view) {
            super(view);
            tv_txn_id = view.findViewById(R.id.tv_txn_id);
            tv_date = view.findViewById(R.id.tv_date);
            tv_service_name = view.findViewById(R.id.tv_service_name);
            tv_amt = view.findViewById(R.id.tv_amt);
            tv_rrn = view.findViewById(R.id.tv_rrn);
            tv_remark = view.findViewById(R.id.tv_remark);
        }
        public void bind(Transaction model, int position) {
            tv_txn_id.setText("TID: "+model.getTxnId());
            tv_date.setText(model.getTxnDate());
            tv_service_name.setText(model.getServiceName());
            tv_amt.setText("Amt: "+model.getAmount());
            tv_rrn.setText("RRN: "+model.getRrn());
            tv_remark.setText("Remark: "+model.getRemark());
        }
    }

}
