package in.msmartpayagent.aeps;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.msmartpayagent.R;
import in.msmartpayagent.network.model.aeps.FundSettlementBank;

public class SettlementBankAdapter extends RecyclerView.Adapter<SettlementBankAdapter.MyViewHolder> {

    private List<FundSettlementBank> list;
    private SettlementBankListener listener;

    public SettlementBankAdapter(List<FundSettlementBank> list, SettlementBankListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.aeps_settlement_bank_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_1, tv_2,tv_3,tv_4,tv_5;
        LinearLayout ll_select_bank,ll_cashout_bank;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_1 = itemView.findViewById(R.id.tv_1);
            tv_2 = itemView.findViewById(R.id.tv_2);
            tv_3 = itemView.findViewById(R.id.tv_3);
            tv_4 = itemView.findViewById(R.id.tv_4);
            tv_5 = itemView.findViewById(R.id.tv_5);
            ll_select_bank = itemView.findViewById(R.id.ll_select_bank);
            ll_cashout_bank = itemView.findViewById(R.id.ll_cashout_bank);
        }

        public void bind(FundSettlementBank bank, SettlementBankListener listener) {
            tv_1.setText(bank.getBank_name());
            tv_2.setText("Name : "+bank.getName());
            tv_3.setText("A/C : "+bank.getAccount());
            tv_4.setText("IFSC : "+bank.getIfsc());

            if("Pending".equalsIgnoreCase(bank.getStatus())){
                tv_5.setText("Status: Waiting for approval");
                ll_select_bank.setVisibility(View.GONE);
                tv_5.setTextColor(Color.GREEN);

            }else if("Rejected".equalsIgnoreCase(bank.getStatus())){
                tv_5.setText("Status : "+bank.getStatus()+"\n"+bank.getRemark());
                tv_5.setTextColor(Color.RED);
                ll_select_bank.setVisibility(View.GONE);

            }else{
                tv_5.setText("Status : "+bank.getStatus());
                ll_select_bank.setVisibility(View.VISIBLE);
                ll_cashout_bank.setBackgroundColor(Color.WHITE);
            }

            if("Active".equalsIgnoreCase(bank.getStatus())) {
                ll_select_bank.setOnClickListener(v -> {
                    listener.onSettlementBankRequest(bank);
                });
            }
        }
    }

    public interface SettlementBankListener {
        void onSettlementBankRequest(FundSettlementBank bank);
    }
}
