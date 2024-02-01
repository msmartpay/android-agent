package in.msmartpay.agent.orderProducts;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.order.OrderProduct;
import in.msmartpay.agent.network.model.order.OrderProductsRequest;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;


public class OrderProductsCartActivity extends BaseActivity {
    private RecyclerView rv_list;
    private TextView tv_total,tv_gst,tv_gross_total;
    private LinearLayout ll_check_out,ll_confirm_order,ll_address;
    private NestedScrollView nsv_scroll;
    private CheckBox cb_gst;
    private List<OrderProduct> list = new ArrayList<>();
    private OrderProductsCartAdapter adapter;
    private double total = 0,gst= 0,gross= 0;
    private String name,mobile,address,zip,gstNo,gstCom,tpin;
    private TextInputLayout tid_name,tid_mob,tid_address,tid_zip,tid_gst_number,tid_gst_com;
    private ProgressDialogFragment pd;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_product_cart_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Cart Details");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        context = OrderProductsCartActivity.this;
        rv_list = findViewById(R.id.rv_list);
        tv_total = findViewById(R.id.tv_total);
        tv_gst = findViewById(R.id.tv_gst);
        tv_gross_total = findViewById(R.id.tv_gross_total);
        ll_check_out  = findViewById(R.id.ll_check_out);
        ll_confirm_order  = findViewById(R.id.ll_confirm_order);
        ll_address  = findViewById(R.id.ll_address);
        nsv_scroll  = findViewById(R.id.nsv_scroll);
        tid_name  = findViewById(R.id.tid_name);
        tid_mob  = findViewById(R.id.tid_mob);
        tid_address  = findViewById(R.id.tid_address);
        tid_zip  = findViewById(R.id.tid_zip);
        cb_gst  = findViewById(R.id.cb_gst);
        tid_gst_number  = findViewById(R.id.tid_gst_number);
        tid_gst_com  = findViewById(R.id.tid_gst_com);
        Util.hideView(ll_confirm_order);
        Util.hideView(ll_address);
        Util.hideView(tid_gst_number);
        Util.hideView(tid_gst_com);
        Objects.requireNonNull(tid_name.getEditText()).setText(Util.LoadPrefData(context,Keys.AGENT_NAME));
        Objects.requireNonNull(tid_mob.getEditText()).setText(Util.LoadPrefData(context,Keys.AGENT_MOB));
        Objects.requireNonNull(tid_address.getEditText()).setText(Util.LoadPrefData(context,Keys.AGENT_ADDRESS));
        Objects.requireNonNull(tid_zip.getEditText()).setText(Util.LoadPrefData(context,Keys.PINCODE));
        if (getIntent() != null) {
            String strJson = getIntent().getStringExtra("List");
            if (strJson != null) {
                list = Util.getListFromJson(strJson, OrderProduct.class);
                if (list != null && list.size() > 0) {
                    L.m2("List Size", "" + list.size());
                    adapter = new OrderProductsCartAdapter(list);
                    rv_list.setAdapter(adapter);
                    for (int i = 0;i<list.size();i++){
                        total = total + list.get(i).getPrice();
                    }
                    gst = (total * 18)/100;
                    gross = total + gst;
                }
                tv_gst.setText(""+gst);
                tv_gross_total.setText(""+gross);
                tv_total.setText(""+total);
            }
        }
        cb_gst.setOnClickListener(v -> {
            // Check the state of the checkbox
            boolean isChecked = ((CheckBox)v).isChecked();
            // Perform actions based on the checkbox state
            if (isChecked) {
                // Checkbox is checked
                Util.showView(tid_gst_com);
                Util.showView(tid_gst_number);
            } else {
                // Checkbox is unchecked
                Util.hideView(tid_gst_com);
                Util.hideView(tid_gst_number);
            }
        });
        ll_check_out.setOnClickListener(v -> {
            Util.hideView(ll_check_out);
            Util.showView(ll_confirm_order);
            Util.showView(ll_address);
        });

        ll_confirm_order.setOnClickListener(v -> {
            name = Objects.requireNonNull(tid_name.getEditText()).getText().toString().trim();
            mobile = Objects.requireNonNull(tid_mob.getEditText()).getText().toString().trim();
            address = Objects.requireNonNull(tid_address.getEditText()).getText().toString().trim();
            zip = Objects.requireNonNull(tid_zip.getEditText()).getText().toString().trim();
            boolean check = cb_gst.isChecked();
            gstNo = Objects.requireNonNull(tid_gst_number.getEditText()).getText().toString().trim();
            gstCom = Objects.requireNonNull(tid_gst_com.getEditText()).getText().toString().trim();

            if (name.isEmpty()){
                L.toastL(context,"Please Enter Name");
            }else if (mobile.length()>10){
                L.toastL(context,"Please Enter Correct Mobile");
            }else if (address.isEmpty()){
                L.toastL(context,"Please Enter Delivery Address");
            }else if (zip.isEmpty()){
                L.toastL(context,"Please Enter Zip Code");
            } else if (check && gstNo.isEmpty()){
                L.toastL(context,"Please Enter GST Number");
            } else if (check && gstCom.isEmpty()){
                L.toastL(context,"Please Enter GST Company");
            }else {
                transactionPinDialog();

            }
        });
    }
    private void transactionPinDialog() {
        final Dialog dialog_status = new Dialog(OrderProductsCartActivity.this);
        dialog_status.setCancelable(false);
        dialog_status.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_status.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog_status.setContentView(R.layout.transaction_pin_dialog);
        dialog_status.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputLayout til_enter_tpin =  dialog_status.findViewById(R.id.til_enter_tpin);

        Button btn_confirm_tpin =  dialog_status.findViewById(R.id.btn_confirm_tpin);
        Button close_confirm_tpin =  dialog_status.findViewById(R.id.close_confirm_tpin);

        btn_confirm_tpin.setOnClickListener(view -> {
            if(TextUtils.isEmpty(til_enter_tpin.getEditText().getText().toString().trim())){
                Toast.makeText(context, "Enter valid 4-digit Transaction pin!!", Toast.LENGTH_SHORT).show();
                til_enter_tpin.getEditText().requestFocus();
            }else{
                tpin=til_enter_tpin.getEditText().getText().toString().trim();
                dialog_status.dismiss();
                saveProducts();
            }

        });

        close_confirm_tpin.setOnClickListener(view -> {
            dialog_status.cancel();
            hideKeyBoard(til_enter_tpin.getEditText());
        });

        dialog_status.show();
    }


    private void saveProducts() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching History...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        OrderProductsRequest request = new OrderProductsRequest();
        request.setKey( Util.LoadPrefData(context, Keys.TXN_KEY));
        request.setAgentID( Util.LoadPrefData(context, Keys.AGENT_ID));
        request.setGstCompany(gstCom);
        request.setGstNumber(gstNo);
        request.setOrderName(name);
        request.setMobile(mobile);
        request.setOrderAddress(address);
        request.setPinCode(zip);
        request.setOrderPrice(""+gross);
        request.setOrderDescription(Util.getJsonFromModel(list));
        request.setTransactionPin(tpin);
        RetrofitClient.getClient(context)
                .saveProductOrderDetails(request).enqueue(new Callback<MainResponse2>() {
                    @Override
                    public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                        pd.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            MainResponse2 res = response.body();
                            if ("0".equalsIgnoreCase(res.getmStatus())) {
                                gotoHomeScreen();
                            }else {
                                L.toastL(context,res.getMessage()==null?"Ops!":res.getMessage());
                            }
                        }
                    }
                    @Override
                    public void onFailure(@NotNull Call<MainResponse2> call, @NotNull Throwable t) {
                        L.toastL(context,t.getMessage()==null?"Ops!":t.getMessage());
                        pd.dismiss();
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
