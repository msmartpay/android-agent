package in.msmartpay.agent.commission.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.model.commission.DmrModel;

import java.util.ArrayList;


public class DmrAdapter extends RecyclerView.Adapter<DmrAdapter.MyHolder> {
    private ArrayList<DmrModel> list;
    private Context context;
    public DmrAdapter(Context context, ArrayList<DmrModel> list){
     this.context=context;
     this.list=list;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.commision_item, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int i) {
    final DmrModel model = list.get(i);
    holder.tv_1.setText(model.getService());
    holder.tv_2.setText(model.getOperstor());
    holder.tv_3.setText(model.getRange());
    holder.tv_4.setText(model.getCharge());
        if(i%2==0){
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.hintcolor));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_1,tv_4,tv_2,tv_3;

        public MyHolder(View itemView) {
            super(itemView);
            tv_1 = itemView.findViewById(R.id.tv_1);
            tv_2 = itemView.findViewById(R.id.tv_2);
            tv_3 = itemView.findViewById(R.id.tv_3);
            tv_4 = itemView.findViewById(R.id.tv_4);
        }
    }

}
