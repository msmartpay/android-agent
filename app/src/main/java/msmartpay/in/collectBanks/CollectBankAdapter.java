package msmartpay.in.collectBanks;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;

import msmartpay.in.R;

public class CollectBankAdapter extends RecyclerView.Adapter<CollectBankAdapter.MyViewHolder> {
        private Context context;

       private ArrayList<CollectBankModel> bankList;
        private View itemView;

        CollectBankAdapter(Context context, ArrayList<CollectBankModel> bankList) {
            this.context = context;
            this.bankList = bankList;

        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_text_bank, ac_holderName, ac_no, ifsc_code;


            MyViewHolder(View itemView) {
                super(itemView);
                tv_text_bank = itemView.findViewById(R.id.tv_text_bank);
                ac_holderName = itemView.findViewById(R.id.ac_holderName);
                ac_no = itemView.findViewById(R.id.ac_no);
                ifsc_code = itemView.findViewById(R.id.ifsc_code);

            }
        }

        @Override
        public CollectBankAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_collect_item, parent, false);

            return new CollectBankAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CollectBankAdapter.MyViewHolder holder, int position) {

            CollectBankModel ni = bankList.get(position);

            holder.tv_text_bank.setText(ni.getBank_name());
            holder.ac_holderName.setText(ni.getBank_account_name());
            holder.ac_no.setText(ni.getBank_account());
            holder.ifsc_code.setText(ni.getBnk_ifsc());

            holder.setIsRecyclable(false);

        }

        @Override
        public int getItemCount() {
            return bankList.size();
        }
    }