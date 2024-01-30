package in.msmartpay.agent.orderProducts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainRequest2;
import in.msmartpay.agent.network.model.order.OrderProduct;
import in.msmartpay.agent.network.model.order.OrderProductListResponse;
import in.msmartpay.agent.utility.BaseFragment;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderProductsAddFragment extends BaseFragment {
    private String agentID, txn_key = "";
    private ProgressDialogFragment pd;
    private RecyclerView rv_list;
    private TextView tv_add_product, tv_view_cart;
    private LinearLayout ll_view_cart,ll_add_product;
    ArrayList<OrderProduct> list = new ArrayList<>();
    ArrayList<OrderProduct> listRequest = new ArrayList<>();
    private OrderProductsListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_product_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        agentID = Util.LoadPrefData(getActivity(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getActivity(), Keys.TXN_KEY);
        rv_list = view.findViewById(R.id.rv_list);
        tv_add_product = view.findViewById(R.id.tv_add_product);
        tv_view_cart = view.findViewById(R.id.tv_view_cart);
        ll_add_product = view.findViewById(R.id.ll_add_product);
        ll_view_cart = view.findViewById(R.id.ll_view_cart);
        setUpAdapter();
        getProductList();

        ll_add_product.setOnClickListener(v -> {
            listRequest.add(new OrderProduct());
            adapter.notifyDataSetChanged();
        });
        ll_view_cart.setOnClickListener(v -> {
            boolean flag = true;
            if (listRequest.size() > 0) {
                for (int i = 0; i < listRequest.size(); i++) {
                    if (listRequest.get(i).getPrice() == 0) {
                        flag = false;
                    }
                }
            }
            if (flag) {
                Intent intent = new Intent(requireActivity(), OrderProductsCartActivity.class);
                intent.putExtra("List", Util.getJsonFromModel(listRequest));
                startActivity(intent);
            } else {
                L.toastL(requireActivity(), "Please enter the product quantity");
            }
        });
    }

    private void setUpAdapter() {
        listRequest.add(new OrderProduct());
        adapter = new OrderProductsListAdapter(listRequest, new OrderProductsListAdapter.MyListener() {
            @Override
            public void onDelete(OrderProduct modal, int position) {
                if (listRequest.size() > 0) {
                    listRequest.remove(position);
                    adapter.notifyDataSetChanged();
                } else {
                    L.toastS(requireActivity(), "");
                }
            }

            @Override
            public void onUpdate(OrderProduct modal, int position) {
                listRequest.remove(position);
                listRequest.add(position, modal);
            }

            @Override
            public void onSelectProduct(OrderProduct m, int position) {
                OrderProductDialogFrag dialogFrag = OrderProductDialogFrag.newInstance(list, model -> {
                    model.setQty(m.getQty());
                    model.setPrice(m.getPrice());
                    listRequest.remove(position);
                    listRequest.add(position, model);
                    adapter.notifyDataSetChanged();
                });
                dialogFrag.show(getChildFragmentManager(), "Show");
            }
        });
        rv_list.setAdapter(adapter);
    }

    private void getProductList() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching History...");
        ProgressDialogFragment.showDialog(pd, getChildFragmentManager());
        MainRequest2 request = new MainRequest2();
        request.setAgentID(agentID);
        request.setKey(txn_key);
        RetrofitClient.getClient(getActivity())
                .fetchProductTypeList(request).enqueue(new Callback<OrderProductListResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<OrderProductListResponse> call, @NotNull retrofit2.Response<OrderProductListResponse> response) {
                        pd.dismiss();
                        list.clear();
                        if (response.isSuccessful() && response.body() != null) {
                            OrderProductListResponse res = response.body();
                            if ("0".equalsIgnoreCase(res.getStatus())) {
                                if (res.getData() != null && res.getData().size() > 0) {
                                    list.addAll((ArrayList<OrderProduct>) res.getData());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<OrderProductListResponse> call, @NotNull Throwable t) {
                        pd.dismiss();
                    }
                });
    }

}
