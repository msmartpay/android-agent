package in.msmartpayagent.dmr.dashboard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import in.msmartpayagent.R;
import in.msmartpayagent.dmr.AddBeneficiaryActivity;
import in.msmartpayagent.dmr.ImpsNeftActivity;
import in.msmartpayagent.dmr.adapter.BeneListAdapter;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.MainResponse2;
import in.msmartpayagent.network.model.dmr.DeleteBeneRequest;
import in.msmartpayagent.network.model.dmr.SenderBeneList;
import in.msmartpayagent.network.model.dmr.SenderDetailsResponse;
import in.msmartpayagent.network.model.dmr.SenderFindRequest;
import in.msmartpayagent.utility.BaseFragment;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Smartkinda on 7/8/2017.
 */

public class BeneficiaryListFragment extends BaseFragment implements BeneListAdapter.BeneListListener {

    private RecyclerView listView;
    private SwipeRefreshLayout swipe_refresh;
    private Dialog d;
    private FloatingActionButton floatingButton;
    private ArrayList<SenderBeneList> customerDetails;
    private TextView tvEmptyList;
    private ProgressDialogFragment pd;
    private String agentID, txnKey, mobileNumber;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dmr_money_trans_listview_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = getActivity();
        listView = view.findViewById(R.id.listview);
        tvEmptyList = (TextView) view.findViewById(R.id.tv_empty_list);
        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        agentID = Util.LoadPrefData(getActivity(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getActivity(), Keys.TXN_KEY);
        mobileNumber = Util.LoadPrefData(getActivity(), Keys.SENDER_MOBILE);

        floatingButton = (FloatingActionButton) view.findViewById(R.id.fab);

        floatingButton.setOnClickListener(v -> {
            Intent floatIntent = new Intent(getActivity(), AddBeneficiaryActivity.class);
            startActivity(floatIntent);
        });

        swipe_refresh.setOnRefreshListener(this::findSenderRequest);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getListBene();
    }

    @Override
    public void onPayNow(SenderBeneList model) {
        Intent intent = new Intent(getActivity(), ImpsNeftActivity.class);
        intent.putExtra("BeneName", model.getBeneName());
        intent.putExtra("BeneAccountNumber", model.getAccount());
        intent.putExtra("BeneBankName", model.getBankName());
        intent.putExtra("IFSCcode", model.getIfsc());
        intent.putExtra("RecipientId", model.getRecipientId());
        intent.putExtra("IMPS", model.getIMPS());
        intent.putExtra("NEFT", model.getNEFT());
        intent.putExtra("Channel", model.getChannel());
        intent.putExtra("RecipientIdType", model.getRecipientIdType());
        intent.putExtra("BeneMobile", model.getBeneMobile());
        startActivity(intent);
    }

    @Override
    public void onBeneDelete(SenderBeneList model) {
        deleteBeneDialog(model.getBeneName(), model.getRecipientId(), 1);
    }
    //Full Data
    private void getListBene() {
        SenderDetailsResponse details = Util.getGson().fromJson(Util.LoadPrefData(getActivity(), Keys.SENDER), SenderDetailsResponse.class);
        if (details.getBeneList() != null && details.getBeneList().size() > 0) {
            tvEmptyList.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            customerDetails = new ArrayList<>();
            customerDetails.clear();
            customerDetails = (ArrayList<SenderBeneList>) details.getBeneList();
            listView.setAdapter(new BeneListAdapter(getActivity(), customerDetails, this));
        } else {
            tvEmptyList.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    //================For Delete Bene===============
    @SuppressLint("SetTextI18n")
    public void deleteBeneDialog(String beneName, final String recipientId, final int i) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dmr_sender_resister_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnOK = (Button) d.findViewById(R.id.btn_resister_ok);
        final Button btnNO = (Button) d.findViewById(R.id.btn_resister_no);
        final TextView tvMessage = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        final TextView title = (TextView) d.findViewById(R.id.title);
        final Button btnClosed = (Button) d.findViewById(R.id.close_push_button);

        if (i == 1) {
            title.setText("For Deletion");
            tvMessage.setText("Are you sure, you want to delete bene " + beneName + " (" + recipientId + ")");
        }
        if (i == 2) {
            title.setText("Deletion Info");
            tvMessage.setText("Bene " + beneName);
            btnOK.setText("Done");
            btnNO.setVisibility(View.GONE);
        }

        btnNO.setOnClickListener(v -> d.dismiss());

        btnOK.setOnClickListener(v -> {
            if (i == 1) {
                DeleteBeneRequest(recipientId);
                d.dismiss();
            }
            if (i == 2) {
                findSenderRequest();
                d.dismiss();
            }
        });

        btnClosed.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            d.cancel();
        });
        d.show();
    }

    //Json request for Delete Bene
    private void DeleteBeneRequest(String recipientId) {
        if (NetworkConnection.isConnectionAvailable(context)) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Getting Latest Details of Sender...");
            ProgressDialogFragment.showDialog(pd, getChildFragmentManager());
            DeleteBeneRequest request = new DeleteBeneRequest();
            request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
            request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
            request.setSenderId(mobileNumber);
            request.setBeneficiaryId(recipientId);
            RetrofitClient.getClient(context).deleteBeneficiary(request).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(Call<MainResponse2> call, retrofit2.Response<MainResponse2> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse2 res = response.body();
                        if (res.getStatus().equals("0")) {
                            Util.SavePrefData(context, Keys.SENDER_MOBILE, mobileNumber);
                            Util.SavePrefData(context, Keys.SENDER, Util.getGson().toJson(res));
                            findSenderRequest();
                        } else {
                            L.toastS(context, res.getMessage());
                        }
                    } else {
                        L.toastS(context, "No Response");
                    }
                }

                @Override
                public void onFailure(Call<MainResponse2> call, Throwable t) {
                    pd.dismiss();
                    L.toastS(context, "Error : " + t.getMessage());
                }
            });
        }
    }

    private void findSenderRequest() {
        if (NetworkConnection.isConnectionAvailable(context)) {
            swipe_refresh.setRefreshing(true);
            SenderFindRequest request = new SenderFindRequest();
            request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
            request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
            request.setSenderId(mobileNumber);
            RetrofitClient.getClient(context).findSenderDetails(request).enqueue(new Callback<SenderDetailsResponse>() {
                @Override
                public void onResponse(Call<SenderDetailsResponse> call, retrofit2.Response<SenderDetailsResponse> response) {
                    swipe_refresh.setRefreshing(false);
                    if (response.isSuccessful() && response.body() != null) {
                        SenderDetailsResponse res = response.body();
                        if (res.getStatus().equals("0")) {
                            Util.SavePrefData(context, Keys.SENDER_MOBILE, mobileNumber);
                            Util.SavePrefData(context, Keys.SENDER, Util.getGson().toJson(res));
                            getListBene();

                        }
                    } else {
                        L.toastS(context, "No Response");
                    }
                }

                @Override
                public void onFailure(Call<SenderDetailsResponse> call, Throwable t) {
                    swipe_refresh.setRefreshing(false);
                    L.toastS(context, "Error : " + t.getMessage());
                }
            });
        } else {
            swipe_refresh.setRefreshing(false);
        }
    }
}
