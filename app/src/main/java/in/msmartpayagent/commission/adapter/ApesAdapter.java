package in.msmartpayagent.commission.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.msmartpayagent.R;
import in.msmartpayagent.network.model.commission.ApesModel;

import java.util.ArrayList;


public class ApesAdapter extends RecyclerView.Adapter<ApesAdapter.MyHolder> {
    private ArrayList<ApesModel> list;
    private Context context;
    public ApesAdapter(Context context, ArrayList<ApesModel> list){
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
    final ApesModel model = list.get(i);
    holder.tv_1.setText(model.getService());
    holder.tv_2.setText(model.getOperstor());
    holder.tv_3.setText(model.getRange());
    holder.tv_4.setText(model.getComm());
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
