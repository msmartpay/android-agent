package com.aepssdkssz.paysprint.dialog;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.aepssdkssz.R;
import com.aepssdkssz.adapter.MiniStatementAdapter;
import com.aepssdkssz.network.model.MiniStatementModel;
import com.aepssdkssz.paysprint.model.PaysprintAepsData;
import com.aepssdkssz.paysprint.model.PaysprintAepsRequestData;
import com.aepssdkssz.paysprint.model.PaysprintAepsResponse;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.Utility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;


public class PSAePSPrinterDialogFrag extends DialogFragment {

    private Context context;
    private PaysprintAepsResponse data;
    private ProgressDialog pd;
    private PrinterListener listener;
    private TextView tv_txn_type, tv_txn_status;
    private TextView tv_comment, tv_amount, tv_balance, tv_tid, tv_rrn, tv_terminal_id, tv_merchant_id, tv_merchant_name, tv_merchant_location, tv_cust_mob, tv_cust_uid_no, tv_date_time;
    private ImageView iv_status;
    private LinearLayoutCompat ll_mini_statement,rl_print_data;
    private Button btn_close, btn_print;
    private String receiptId="";
    private MiniStatementAdapter adapter;
    private RecyclerView aeps_mini_statement;
    private void setListener(PrinterListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ssz_aeps_printer_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVies(view);

        context = requireActivity();

        Bundle bundle = getArguments();
        if (bundle != null) {

            String res = bundle.getString("response");
            assert res != null;
            if (!res.isEmpty()) {
                data = Utility.getGson().fromJson(res, PaysprintAepsResponse.class);
                if (data.getPayload() != null) {


                    PaysprintAepsData respData=data.getPayload();
                    PaysprintAepsRequestData reqData=respData.getReqData();

                    receiptId="Rec_"+respData.getTid();

                    tv_txn_status.setText("" + respData.getTxn_status());
                    String status = respData.getTxn_status();
                    if ("Success".equalsIgnoreCase(status)) {//Success
                        iv_status.setImageResource(R.drawable.ssz_aeps_ic_success);
                        iv_status.setColorFilter(ContextCompat.getColor(iv_status.getContext(), R.color.sszAePS_green), android.graphics.PorterDuff.Mode.SRC_IN);
                    } else if ("Failed".equalsIgnoreCase(status)) {//Failed
                        iv_status.setImageResource(R.drawable.ssz_aeps_ic_failed);
                        iv_status.setColorFilter(ContextCompat.getColor(iv_status.getContext(), R.color.sszAePS_red), android.graphics.PorterDuff.Mode.SRC_IN);
                    } else if ("Initiated".equalsIgnoreCase(status)) {//Initiated
                        iv_status.setImageResource(R.drawable.ssz_aeps_ic_initiated);
                        iv_status.setColorFilter(ContextCompat.getColor(iv_status.getContext(), R.color.sszAePSColorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                    }else{
                        iv_status.setImageResource(R.drawable.ssz_aeps_ic_initiated);
                        iv_status.setColorFilter(ContextCompat.getColor(iv_status.getContext(), R.color.sszAePSColorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                    }
                    tv_comment.setText("" + data.getMessage());
                    tv_txn_type.setText("" + respData.getTransaction_type());
                    tv_amount.setText("" + reqData.getAmount());
                    tv_balance.setText("" + respData.getBalance());
                    tv_tid.setText("" + respData.getTid());
                    tv_rrn.setText("" + respData.getRrn());
                    tv_terminal_id.setText("" + respData.getTerminal_id());
                    tv_merchant_id.setText("" + Utility.getData(requireActivity(),Constants.MERCHANT_ID));
                    tv_merchant_name.setText("" + Utility.getData(requireActivity(),Constants.MERCHANT_NAME));
                    tv_merchant_location.setText("" + Utility.getData(requireActivity(),Constants.MERCHANT_LOCATION));
                    tv_cust_mob.setText("" + reqData.getCustomer_id());
                    tv_cust_uid_no.setText("" + reqData.getBc_aadhaar());
                    tv_date_time.setText("" + respData.getDate_time());

                    if("MINISTATEMENT".equalsIgnoreCase(respData.getTransaction_type())){
                        ArrayList<MiniStatementModel> miniStatementList=respData.getMiniStatementlist();
                        if(miniStatementList!=null) {
                            adapter = new MiniStatementAdapter(requireActivity(), miniStatementList);
                            aeps_mini_statement.setAdapter(adapter);

                            Utility.showView(ll_mini_statement);

                        }
                    }else{
                        Utility.hideView(ll_mini_statement);
                    }
                }
            }
        }

        btn_print.setOnClickListener(view1 -> {
            myPermissions();
            //  listener.onClose();
        });
        btn_close.setOnClickListener(view1 -> {
            if(listener!=null) {
                listener.onClose();
            }
            dismiss();
        });
    }

    private void initVies(View view) {
        aeps_mini_statement = view.findViewById(R.id.aeps_mini_statement);
        ll_mini_statement = view.findViewById(R.id.ll_mini_statement);
        rl_print_data = view.findViewById(R.id.rl_print_data);
        iv_status = view.findViewById(R.id.iv_status);
        btn_close = view.findViewById(R.id.btn_close);
        btn_print = view.findViewById(R.id.btn_print);
        tv_txn_status = view.findViewById(R.id.tv_txn_status);
        tv_txn_type = view.findViewById(R.id.tv_txn_type);
        tv_comment = view.findViewById(R.id.tv_comment);
        tv_amount = view.findViewById(R.id.tv_amount);
        tv_balance = view.findViewById(R.id.tv_balance);
        tv_tid = view.findViewById(R.id.tv_tid);
        tv_rrn = view.findViewById(R.id.tv_rrn);
        tv_terminal_id = view.findViewById(R.id.tv_terminal_id);
        tv_merchant_id = view.findViewById(R.id.tv_merchant_id);
        tv_merchant_name = view.findViewById(R.id.tv_merchant_name);
        tv_merchant_location = view.findViewById(R.id.tv_merchant_location);
        tv_cust_mob = view.findViewById(R.id.tv_cust_mob);
        tv_cust_uid_no = view.findViewById(R.id.tv_cust_uid_no);
        tv_date_time = view.findViewById(R.id.tv_date_time);
    }

    private void myPermissions() {
        Dexter.withContext(requireActivity())
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            Utility.loge("myPermissions", "areAllPermissionsGranted");
                            printData();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Utility.loge("myPermissions", "isAnyPermissionPermanentlyDenied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError dexterError) {
                        Utility.loge("myPermissions", "dexterError : " + dexterError.name());
                    }
                })
                .onSameThread()
                .check();
    }

    private void printData() {
        requireActivity().runOnUiThread(() -> {
            Utility.screenshot(rl_print_data,receiptId + "_" + System.currentTimeMillis());
        });
    }

    public static PSAePSPrinterDialogFrag newInstance(String response, PrinterListener listener) {
        Bundle args = new Bundle();
        args.putString("response", response);
        PSAePSPrinterDialogFrag fragment = new PSAePSPrinterDialogFrag();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    public interface PrinterListener {
        void onClose();
    }


}
