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

public class OrderProductsCartAdapter extends RecyclerView.Adapter<OrderProductsCartAdapter.MyViewHolder> {
    private List<OrderProduct> list;
    private MyListener listener;

    public OrderProductsCartAdapter(List<OrderProduct> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_product, tv_rate, tv_qty, tv_total;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_product = v.findViewById(R.id.tv_product);
            tv_total = v.findViewById(R.id.tv_total);
            tv_qty = v.findViewById(R.id.tv_qty);
            tv_rate = v.findViewById(R.id.tv_rate);
        }

        public void bind(OrderProduct modal, int position) {
            if (modal != null) {
                tv_product.setText(modal.getProductName());
                tv_rate.setText("" + modal.getAgentPrice());
                tv_total.setText("" + modal.getPrice());
                tv_qty.setText("" + modal.getQty());
            }
        }
    }

    interface MyListener {
        void onSelectProduct(OrderProduct modal, int position);
    }
}
