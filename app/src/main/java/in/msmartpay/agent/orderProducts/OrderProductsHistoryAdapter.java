package in.msmartpay.agent.orderProducts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import in.msmartpay.agent.R;
import in.msmartpay.agent.network.model.order.OrderProductHistoryData;

public class OrderProductsHistoryAdapter extends RecyclerView.Adapter<OrderProductsHistoryAdapter.MyViewHolder> {
    private List<OrderProductHistoryData> list;
    public MyListener listener;

    public OrderProductsHistoryAdapter(List<OrderProductHistoryData> list, MyListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_remark, tv_tracking_id, tv_order_details, tv_order_date, tv_order_id;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_remark = v.findViewById(R.id.tv_remark);
            tv_tracking_id = v.findViewById(R.id.tv_tracking_id);
            tv_order_details = v.findViewById(R.id.tv_order_details);
            tv_order_date = v.findViewById(R.id.tv_order_date);
            tv_order_id = v.findViewById(R.id.tv_order_id);
        }

        public void bind(OrderProductHistoryData modal) {
            if (modal != null) {
                tv_order_details.setText(modal.getOrderDetail());
                tv_order_date.setText(modal.getOrderTime());
                tv_order_id.setText(modal.getOrderId());
                tv_tracking_id.setText(modal.getDeliveryTrackingId());
                tv_remark.setText(modal.getRemark());
            }
        }
    }

    interface MyListener {
        void onClick(OrderProductHistoryData modal);
    }
}
