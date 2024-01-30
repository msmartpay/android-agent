package in.msmartpay.agent.orderProducts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.model.order.OrderProduct;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.MyViewHolder> {
    private List<OrderProduct> list;
    private MyListener listener;

    public OrderProductAdapter(List<OrderProduct> list, MyListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dmr_bank_search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(list.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_name = itemView.findViewById(R.id.tv_name);
        }

        public void bind(OrderProduct modal, MyListener listener) {
            if (modal != null) {
                tv_name.setText(modal.getProductName());
                tv_name.setOnClickListener(v -> {
                    listener.onSelectProduct(modal);
                });

            }
        }
    }

    interface MyListener {
        void onSelectProduct(OrderProduct modal);
    }
}
