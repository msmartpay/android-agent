package com.aepssdkssz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aepssdkssz.R;
import com.aepssdkssz.network.model.MiniStatementModel;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;


public class MiniStatementAdapter extends RecyclerView.Adapter<MiniStatementAdapter.MyViewHolder> {
    private Context context;

    private ArrayList<MiniStatementModel> statements;
    private View itemView;


    public MiniStatementAdapter(Context context, ArrayList<MiniStatementModel> statements) {
        this.context = context;
        this.statements = statements;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ms_date, tv_ms_txntype, tv_ms_narration, tv_ms_amount;
        Button btn_live_status;


        MyViewHolder(View itemView) {
            super(itemView);
            tv_ms_date = itemView.findViewById(R.id.tv_ms_date);
            tv_ms_txntype = itemView.findViewById(R.id.tv_ms_txnType);
            tv_ms_narration = itemView.findViewById(R.id.tv_ms_narration);
            tv_ms_amount = itemView.findViewById(R.id.tv_ms_amount);
        }

        public void bind(MiniStatementModel model) {

            tv_ms_date.setText(model.getDate());
            tv_ms_txntype.setText(model.getTxnType());
            tv_ms_narration.setText(model.getNarration());
            tv_ms_amount.setText(model.getAmount());

        }
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ministatement_textview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(statements.get(position));
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return statements.size();
    }

}