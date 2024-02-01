package in.msmartpay.agent.orderProducts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import in.msmartpay.agent.R;
import in.msmartpay.agent.dialogs.StringAdapter;
import in.msmartpay.agent.network.model.order.OrderProduct;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Util;


public class OrderProductDialogFrag extends BottomSheetDialogFragment {
    private ImageView iv_back;
    private RecyclerView rv_list;
    private TextView tv_title;
    private List<OrderProduct> list = new ArrayList<>();
    private OrderProductAdapter adapter;
    private SelectedListener listener;

    private void setListener(SelectedListener listener) {
        this.listener = listener;
    }

//    @Override
//    public int getTheme() {
//        return R.style.DialogTheme;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_product_select_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_back = view.findViewById(R.id.iv_back);
        tv_title = view.findViewById(R.id.tv_title);
        rv_list = view.findViewById(R.id.rv_list);
        if (getArguments() != null) {
            String strJson = getArguments().getString("List");
            if (strJson != null) {
                list = Util.getListFromJson(strJson, OrderProduct.class);
                if (list != null && list.size() > 0) {
                    L.m2("List Size", "" + list.size());
                    adapter = new OrderProductAdapter(list, modal -> {
                        listener.onSelected(modal);
                        dismiss();
                    });
                    rv_list.setAdapter(adapter);
                } else {
                    dismiss();
                }
            }
        }
        iv_back.setOnClickListener(v -> {
            dismiss();
        });
    }

    public static OrderProductDialogFrag newInstance(List<OrderProduct> operatorList, SelectedListener listener) {
        Bundle args = new Bundle();
        args.putString("List", Util.getStringFromModel(operatorList));
        OrderProductDialogFrag fragment = new OrderProductDialogFrag();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    public interface SelectedListener {
        void onSelected(OrderProduct model);
    }
}
