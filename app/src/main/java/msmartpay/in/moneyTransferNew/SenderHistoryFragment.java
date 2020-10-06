package msmartpay.in.moneyTransferNew;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import msmartpay.in.R;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Smartkinda on 7/8/2017.
 */

public class SenderHistoryFragment extends Fragment {
    private ProgressDialog pd;
    private String url_sender_history = HttpURL.SENDER_HISTORY;
    private SharedPreferences sharedPreferences;
    private ListView listViewSender;
    private Context context;
    private ArrayList<SenderHistoryModel> arrayList = new ArrayList<SenderHistoryModel>();
    private String agentID, txnKey, mobileNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_dmr_sender_history_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = getActivity();
        sharedPreferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);
        listViewSender = (ListView) view.findViewById(R.id.list_view);


        try {
            SenderHistoryRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    //===================Adapter Class=============================

    //Adapter Class
    public class SenderAdaptorClass extends BaseAdapter {

        private Context contextData;
        private ArrayList<SenderHistoryModel> arrayListData;
        private TextView DateTime, AccountNumber, Amount, SerDelivTID, Status;
        private Button btn_live_status;

        SenderAdaptorClass(Context context, ArrayList<SenderHistoryModel> arrayList) {
            contextData = context;
            arrayListData = arrayList;
        }


        @Override
        public int getCount() {
            return arrayListData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;

        }


        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) contextData.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.sender_history_textview, parent, false);

            SerDelivTID = (TextView) view.findViewById(R.id.tview_ser_deliv_tid);
            Status = (TextView) view.findViewById(R.id.tview_status);
            DateTime = (TextView) view.findViewById(R.id.tview_date_time);
            AccountNumber = (TextView) view.findViewById(R.id.tview_account_no);
            Amount = (TextView) view.findViewById(R.id.tview_request_amt);
            btn_live_status =  view.findViewById(R.id.btn_live_status);

            SerDelivTID.setText("Txn Id : " + arrayListData.get(position).getTranNo());
            Status.setText(arrayListData.get(position).getStatus());
            DateTime.setText(arrayListData.get(position).getDot()+" "+arrayListData.get(position).getTot());
            AccountNumber.setText(arrayListData.get(position).getBeneAccount());
            Amount.setText("\u20B9 "+arrayListData.get(position).getAmount());

            btn_live_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), RefundLiveStatusActivity.class);
                    intent.putExtra("TranNo", arrayListData.get(position).getTranNo());
                    intent.putExtra("fromIntent", "fromSenderHistoryFragment");
                    startActivity(intent);
                    ((MoneyTransferActivity)context).finish();
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SenderFullHistoryActivity.class);
                    intent.putExtra("TranNo", arrayListData.get(position).getTranNo());
                    intent.putExtra("Status", arrayListData.get(position).getStatus());
                    intent.putExtra("DateTime", arrayListData.get(position).getDot() + " " + arrayListData.get(position).getTot());
                    intent.putExtra("BeneAccount", arrayListData.get(position).getBeneAccount());
                    intent.putExtra("Amount", arrayListData.get(position).getAmount());
                    intent.putExtra("BeneName", arrayListData.get(position).getBeneName());
                    intent.putExtra("BeneBankName", arrayListData.get(position).getBeneBankName());
                    intent.putExtra("BeneBankIfsc", arrayListData.get(position).getBeneBankIfsc());
                    intent.putExtra("TransactionType", arrayListData.get(position).getTransferType());
                    startActivity(intent);
                }
            });

            return view;
        }
    }

//=========================================================================
    //For Json Request
    private void SenderHistoryRequest() {
        pd = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txnKey)
                    .put("SenderId", mobileNumber);

            Log.e("Request--SenderHistory", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_sender_history, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            L.m2("SenderHistory-->", object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    L.m2("url data--SenderHist", object.toString());

                                    JSONArray jsonArray = object.getJSONArray("Statement");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        SenderHistoryModel historyModel = new SenderHistoryModel();
                                        historyModel.setSenderId(jsonObject1.getString("SenderId"));
                                        historyModel.setTranNo(jsonObject1.getString("TranNo"));
                                        historyModel.setDot(jsonObject1.getString("Dot"));
                                        historyModel.setTot(jsonObject1.getString("Tot"));
                                        historyModel.setAmount(jsonObject1.getString("Amount"));
                                        historyModel.setStatus(jsonObject1.getString("Status"));
                                        historyModel.setBankrefId(jsonObject1.getString("BankrefId"));
                                        historyModel.setBeneName(jsonObject1.getString("BeneName"));
                                        historyModel.setBeneBankName(jsonObject1.getString("BeneBankName"));
                                        historyModel.setBeneBankIfsc(jsonObject1.getString("BeneBankIfsc"));
                                        historyModel.setBeneAccount(jsonObject1.getString("BeneAccount"));
                                        historyModel.setTransferType(jsonObject1.getString("TransactionType"));
                                        arrayList.add(historyModel);
                                    }
                                    listViewSender.setAdapter(new SenderAdaptorClass(context, arrayList));
                                } else {
                                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getActivity()).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }

    }
}
