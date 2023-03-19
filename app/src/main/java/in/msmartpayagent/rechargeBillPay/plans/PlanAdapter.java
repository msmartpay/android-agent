package in.msmartpayagent.rechargeBillPay.plans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.msmartpayagent.R;
import in.msmartpayagent.network.model.wallet.PlanModel;
import in.msmartpayagent.utility.PlanAdListener;

import java.util.ArrayList;


public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyHolder> {
    private ArrayList<PlanModel> planListModels;
    private Context context;
    private PlanAdListener listener;
    public PlanAdapter(Context context, ArrayList<PlanModel> planListModels, PlanAdListener listener){
     this.context=context;
     this.planListModels=planListModels;
        this.listener = listener;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.plan_item, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int i) {
    final PlanModel model = planListModels.get(i);
    holder.tv_price.setText(model.getRs());
    holder.tv_desc.setText(model.getDesc());
    holder.tv_last_date.setText(model.getLast_update());
    holder.view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.onClickPlan(model.getRs());

        }
    });
    }

    @Override
    public int getItemCount() {
        return planListModels.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_price, tv_last_date, tv_desc;
        View view;

        public MyHolder(View itemView) {
            super(itemView);
            view=itemView;
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_last_date = itemView.findViewById(R.id.tv_last_date);
            tv_last_date.setVisibility(View.GONE);
        }
    }

}
