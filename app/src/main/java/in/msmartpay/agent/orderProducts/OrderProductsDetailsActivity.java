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
import android.widget.ImageView;
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
import in.msmartpay.agent.network.model.order.OrderProductHistoryData;
import in.msmartpay.agent.network.model.order.OrderProductsRequest;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;


public class OrderProductsDetailsActivity extends BaseActivity {
    private RecyclerView rv_list;
    private TextView tv_total,tv_gst,tv_gross_total;
    private TextView tv_name,tv_mob,tv_address,tv_txn_status,tv_gst_no,tv_tracking_id,tv_delivery_status;
    private ImageView iv_logo;
    private NestedScrollView nsv_scroll;
    private CheckBox cb_gst;
    private OrderProductHistoryData data;
    private OrderProductsCartAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_product_details_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Cart Details");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        context = OrderProductsDetailsActivity.this;
        tv_name = findViewById(R.id.tv_name);
        tv_mob = findViewById(R.id.tv_mob);
        tv_address = findViewById(R.id.tv_address);
        tv_gst_no = findViewById(R.id.tv_gst_no);
        tv_txn_status = findViewById(R.id.tv_txn_status);
        iv_logo = findViewById(R.id.iv_logo);
        rv_list = findViewById(R.id.rv_list);
        tv_total = findViewById(R.id.tv_total);
        tv_gst = findViewById(R.id.tv_gst);
        tv_gross_total = findViewById(R.id.tv_gross_total);
        nsv_scroll  = findViewById(R.id.nsv_scroll);
        tv_tracking_id = findViewById(R.id.tv_tracking_id);
        tv_delivery_status = findViewById(R.id.tv_delivery_status);

         if (getIntent() != null) {
            String strJson = getIntent().getStringExtra(Keys.OBJECT);
            if (strJson != null) {
                data = Util.getGson().fromJson(strJson, OrderProductHistoryData.class);
                if (data!=null){
                    tv_txn_status.setText(data.getTxnStatus());
                    tv_name.setText(data.getOrderName());
                    tv_mob.setText(data.getMobile());
                    tv_address.setText(data.getOrderAddress());
                    tv_gst.setText(data.getGstAmount());
                    tv_gst_no.setText(data.getGstNo());
                    tv_gross_total.setText(data.getNetAmount());
                    tv_total.setText(data.getOrderAmount());
                    if (data.getOrderDescription()!=null) {
                        OrderProductsCartAdapter adapter = new OrderProductsCartAdapter(data.getOrderDescription());
                        rv_list.setAdapter(adapter);
                    }
                    tv_tracking_id.setText(data.getDeliveryTrackingId());
                    tv_delivery_status.setText(data.getDeliveryStatus());
                }
            }
        }
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
