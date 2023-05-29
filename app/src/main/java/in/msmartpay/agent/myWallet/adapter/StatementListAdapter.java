package in.msmartpay.agent.myWallet.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.myWallet.TransactionHistoryReceipt;
import in.msmartpay.agent.network.model.wallet.TransactionItems;
import in.msmartpay.agent.utility.Util;

import java.util.ArrayList;
import java.util.List;

public class StatementListAdapter extends RecyclerView.Adapter<in.msmartpay.agent.myWallet.adapter.StatementListAdapter.MyViewHolder> {
        private ArrayList<TransactionItems> tranListAdd;
        private ArrayList<TransactionItems> tranListAdd2;
        private in.msmartpay.agent.myWallet.adapter.StatementListAdapter.ItemFilter mFilter=new in.msmartpay.agent.myWallet.adapter.StatementListAdapter.ItemFilter();
        private in.msmartpay.agent.myWallet.adapter.StatementListAdapter.ItemFilter2 mFilter2 = new in.msmartpay.agent.myWallet.adapter.StatementListAdapter.ItemFilter2();

        public StatementListAdapter(ArrayList<TransactionItems> list) {
            tranListAdd =  list;
            tranListAdd2 = list;
        }

        @NonNull
        @Override
        public in.msmartpay.agent.myWallet.adapter.StatementListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new in.msmartpay.agent.myWallet.adapter.StatementListAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_history_items, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull in.msmartpay.agent.myWallet.adapter.StatementListAdapter.MyViewHolder holder, int position) {
            holder.bind(tranListAdd.get(position),position);
        }

        @Override
        public int getItemCount() {
            return tranListAdd.size();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            TextView ac,acc_op, amt, status, txnId, date,tv_current_amt,tv_txn_commission,tv_txn_remark;

            public MyViewHolder(@NonNull View view) {
                super(view);

                tv_txn_commission  =view.findViewById(R.id.tv_txn_commission);
                tv_txn_remark  =view.findViewById(R.id.tv_txn_remark);
                ac  =view.findViewById(R.id.ac);
                acc_op =view.findViewById(R.id.acc_op);
                amt = view.findViewById(R.id.amt);
                status = view.findViewById(R.id.status);
                txnId =  view.findViewById(R.id.txn_id);
                date =  view.findViewById(R.id.date);
                //cd =  view.findViewById(R.id.cd);
                tv_current_amt = view.findViewById(R.id.tv_current_amt);
            }

            public void bind(TransactionItems historyModel,int poss) {

                if (historyModel != null) {

                    tv_txn_commission.setText("\u20B9"+" "+historyModel.getCommission());
                    tv_txn_remark.setText(historyModel.getRemark()+"");
                    tv_current_amt.setText("\u20B9"+" "+historyModel.getAgent_F_balAmt());
                    ac.setText( historyModel.getService());
                    acc_op.setText("Mob: "+historyModel.getMobile_number() +"\n"+historyModel.getMobile_operator());
                    status.setText(historyModel.getTran_status());

                    txnId.setText(historyModel.getTran_id());
                    date.setText(historyModel.getDot()+"\n"+historyModel.getTot());
                    String st=historyModel.getTran_status();
                    String aType=historyModel.getAction_on_bal_amt();

                    String amount=historyModel.getNet_amout();
                    float fAmount=Float.parseFloat(amount);

                    if(aType.equalsIgnoreCase("credit"))
                        amt.setText("+"+"\u20B9"+" "+fAmount+"");
                    if(aType.equalsIgnoreCase("debit"))
                        amt.setText("-"+"\u20B9"+" "+fAmount+"");

                    itemView.setOnClickListener(view -> {
                        Intent intent = new Intent( itemView.getContext(), TransactionHistoryReceipt.class);
                        intent.putExtra("position", poss);
                        intent.putExtra("historyModel", Util.getGson().toJson(historyModel));
                        itemView.getContext().startActivity(intent);
                    });

                    if("Success".equalsIgnoreCase(historyModel.getTran_status())) {
                        status.setBackgroundResource(R.color.green);
                    }else if("Failure".equalsIgnoreCase(historyModel.getTran_status())
                            || "Refunded".equalsIgnoreCase(historyModel.getTran_status())) {
                        status.setBackgroundResource(R.color.red);
                        status.setTextColor(Color.WHITE);
                    }else if("Initiated".equalsIgnoreCase(historyModel.getTran_status()) ) {
                        status.setBackgroundResource(R.color.yellow);
                        status.setTextColor(Color.BLACK);
                    }else {
                        status.setBackgroundResource(R.color.spinner_hint_text_color);
                        status.setTextColor(Color.BLACK);
                    }
                }
            }
        }

        public Filter getFilter() {
            return mFilter;
        }
        public Filter getFilter2() {
            return mFilter2;
        }

        private class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<TransactionItems> list = tranListAdd2;

                int count = list.size();
                final ArrayList<TransactionItems> nlist = new ArrayList<>(count);

                for (TransactionItems model:list) {
                    if(filterString.equalsIgnoreCase("in")){
                        if(model.getAction_on_bal_amt().equalsIgnoreCase("Credit"))
                            nlist.add(model);
                    }else if(filterString.equalsIgnoreCase("out")){
                        if(model.getAction_on_bal_amt().equalsIgnoreCase("Debit"))
                            nlist.add(model);
                    }else {
                        nlist.add(model);
                    }
                }

                results.values = nlist;
                results.count = nlist.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                tranListAdd = (ArrayList<TransactionItems>) results.values;
                notifyDataSetChanged();
            }

        }

        private class ItemFilter2 extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                List<TransactionItems> list = tranListAdd2;

                int count = list.size();
                ArrayList<TransactionItems> nlist = new ArrayList<>(count);

                for (TransactionItems model:list) {
                    if(model.getService().toLowerCase().contains(filterString) ||model.getTran_status().toLowerCase().contains(filterString)  ) {
                        nlist.add(model);
                    }
                }
                if(nlist.size()==0)
                    nlist=tranListAdd2;

                results.values = nlist;
                results.count = nlist.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                tranListAdd = (ArrayList<TransactionItems>) results.values;
                notifyDataSetChanged();
            }

        }
}
