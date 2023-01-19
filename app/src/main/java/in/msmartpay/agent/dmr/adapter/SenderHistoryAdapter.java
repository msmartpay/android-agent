package in.msmartpay.agent.dmr.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.dmr.RefundLiveStatusActivity;
import in.msmartpay.agent.dmr.SenderFullHistoryActivity;
import in.msmartpay.agent.network.model.dmr.Statement;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SenderHistoryAdapter extends RecyclerView.Adapter<SenderHistoryAdapter.MyViewHolder> {
    private Context context;

    private ArrayList<Statement> bankList;
    private View itemView;


    public SenderHistoryAdapter(Context context, ArrayList<Statement> bankList) {
        this.context = context;
        this.bankList = bankList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tview_ser_deliv, tview_account_no, tview_date_time, tview_status, tview_request_amt;
        Button btn_live_status;


        MyViewHolder(View itemView) {
            super(itemView);
            tview_ser_deliv = itemView.findViewById(R.id.tview_ser_deliv);
            tview_account_no = itemView.findViewById(R.id.tview_account_no);
            tview_date_time = itemView.findViewById(R.id.tview_date_time);
            tview_status = itemView.findViewById(R.id.tview_status);

            tview_request_amt = itemView.findViewById(R.id.tview_request_amt);
            btn_live_status = itemView.findViewById(R.id.btn_live_status);

        }

        public void bind(Statement model) {

            tview_ser_deliv.setText("Txn Id : " + model.getTranNo());
            tview_account_no.setText(model.getBeneAccount());
            tview_date_time.setText(model.getDot() + " " + model.getTot());
            tview_status.setText(model.getStatus());
            tview_request_amt.setText("\u20B9 " + model.getAmount());

            if(!"Refunded".equalsIgnoreCase(model.getStatus())) {
                btn_live_status.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), RefundLiveStatusActivity.class);
                    intent.putExtra("TranNo", model.getTranNo());
                    intent.putExtra("fromIntent", "fromSenderHistoryFragment");
                    v.getContext().startActivity(intent);
                });
            }else{
                btn_live_status.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), SenderFullHistoryActivity.class);
                intent.putExtra("TranNo", model.getTranNo());
                intent.putExtra("Status", model.getStatus());
                intent.putExtra("DateTime", model.getDot() + " " + model.getTot());
                intent.putExtra("BeneAccount", model.getBeneAccount());
                intent.putExtra("Amount", model.getAmount());
                intent.putExtra("BeneName", model.getBeneName());
                intent.putExtra("BeneBankName", model.getBeneBankName());
                intent.putExtra("BeneBankIfsc", model.getBeneBankIfsc());
                intent.putExtra("TransactionType", model.getTransactionType());
                v.getContext().startActivity(intent);
            });

        }
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dmr_sender_history_textview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(bankList.get(position));
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

}