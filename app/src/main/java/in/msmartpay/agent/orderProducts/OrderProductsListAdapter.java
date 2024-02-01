package in.msmartpay.agent.orderProducts;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.model.order.OrderProduct;

public class OrderProductsListAdapter extends RecyclerView.Adapter<OrderProductsListAdapter.MyViewHolder> {
    private List<OrderProduct> list;
    private MyListener listener;

    public OrderProductsListAdapter(List<OrderProduct> list, MyListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(list.get(position),position,listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_product,tv_delete;
        EditText et_price,et_qty;
        RelativeLayout rl_select;
        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_product =  v.findViewById(R.id.tv_product);
            et_price =  v.findViewById(R.id.et_price);
            et_qty =  v.findViewById(R.id.et_qty);
            rl_select =  v.findViewById(R.id.rl_select);
            tv_delete=  v.findViewById(R.id.tv_delete);
        }

        public void bind(OrderProduct modal, int position, MyListener listener) {
            if (modal != null) {
                tv_product.setText(modal.getProductName());
                et_price.setText(""+modal.getPrice());
                et_qty.setText(""+modal.getQty());

                et_qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(modal!=null && modal.getAgentPrice()>0) {
                            double price = modal.getAgentPrice();
                            int qty = 0;
                            //if (count>0){
                            String ss = s.toString().trim();
                            if (!ss.isEmpty())
                                qty = Integer.parseInt(ss);
                            //}
                            modal.setPrice(price * qty);
                            modal.setQty(qty);
                            et_price.setText("" + modal.getPrice());
                            listener.onUpdate(modal, position);
                        }else{

                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                rl_select.setOnClickListener(v -> {
                    listener.onSelectProduct(modal,position);

                });
                tv_delete.setOnClickListener(v -> {
                    listener.onDelete(modal,position);
                });
            }
        }
    }

    interface MyListener {
        void onDelete(OrderProduct modal,int position);
        void onUpdate(OrderProduct modal,int position);
        void onSelectProduct(OrderProduct modal,int position);
    }
}
