package in.msmartpay.agent.dmr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.model.dmr.SenderBeneList;

import java.util.ArrayList;

public class BeneListAdapter extends RecyclerView.Adapter<BeneListAdapter.MyViewHolder> {
        private Context context;

       private ArrayList<SenderBeneList> bankList;
       private View itemView;
       private BeneListListener listener;


        public BeneListAdapter(Context context, ArrayList<SenderBeneList> bankList, BeneListListener listener) {
            this.context = context;
            this.bankList = bankList;
            this.listener = listener;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_bene_name, tv_account_no, tv_bank_name, tv_dmr_recipient_id,tv_bank_ifsc,tv_delete_dmr_beneficiary;
            ImageView iv_dmr_bene;
            LinearLayout ll_select_beneficiary;


            MyViewHolder(View itemView) {
                super(itemView);
                iv_dmr_bene = itemView.findViewById(R.id.iv_dmr_bene);
                tv_bene_name = itemView.findViewById(R.id.tv_bene_name);
                tv_account_no = itemView.findViewById(R.id.tv_account_no);
                tv_bank_name = itemView.findViewById(R.id.tv_bank_name);
                tv_dmr_recipient_id = itemView.findViewById(R.id.tv_dmr_recipient_id);
                tv_bank_ifsc = itemView.findViewById(R.id.tv_bank_ffsc);
                iv_dmr_bene = itemView.findViewById(R.id.iv_dmr_bene);

                ll_select_beneficiary = itemView.findViewById(R.id.ll_select_beneficiary);
                tv_delete_dmr_beneficiary = itemView.findViewById(R.id.tv_delete_dmr_beneficiary);

            }

            public void bind(SenderBeneList model) {

                tv_bene_name.setText(model.getBeneName());
                tv_account_no.setText("A/C: "+model.getAccount());
                tv_bank_name.setText(model.getBankName());
                tv_dmr_recipient_id.setText(model.getRecipientId());
                tv_bank_ifsc.setText(model.getIfsc());

                if("1".equalsIgnoreCase(model.getIsVerified()))
                    iv_dmr_bene.setImageResource(R.drawable.tick_ok);
                else
                    iv_dmr_bene.setImageResource(R.drawable.pending);

                ll_select_beneficiary.setOnClickListener(v -> {
                    listener.onPayNow(model);
                });
                tv_delete_dmr_beneficiary.setOnClickListener(v -> {
                    listener.onBeneDelete(model);
                });

            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dmr_bene_listview_text, parent, false);

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

       public interface BeneListListener{
            void onPayNow(SenderBeneList model);
            void onBeneDelete(SenderBeneList model);
        }
    }