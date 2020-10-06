package msmartpay.in.dmr2Moneytrasfer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import msmartpay.in.MainActivity;
import msmartpay.in.R;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Smartkinda on 7/8/2017.
 */

public class BeneficiaryListFragment extends Fragment {

    private ListView listView;
    private Dialog d;
    private FloatingActionButton floatingButton;
    private ArrayList<BankDetailsModel> customerDetails;
    private BankDetailsModel customerData;
    private TextView tvEmptyList;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private String url_delete_bene = HttpURL.DELETE_BENE_URL_Dmr2;
    private String agentID, txnKey, SessionID, mobileNumber, BeneCodeForOTP;
    private SharedPreferences sharedPreferences;
    private String url_find_sender = HttpURL.FIND_SENDER_Dmr2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dmr2_money_trans_listview_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        listView = (ListView) view.findViewById(R.id.listview);
        tvEmptyList = (TextView) view.findViewById(R.id.tv_empty_list);
        L.m2("SenderLimit_Detail-->", MainActivity.jsonObjectStatic.toString());

        sharedPreferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);

        //listdata
        getListBene();

        floatingButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent floatIntent = new Intent(getActivity(), AddBeneficiaryActivity.class);
                startActivity(floatIntent);
            }
        });

        return view;
    }

    //Adapter Class
    public class bankDetailAdaptorClass extends BaseAdapter {

        private Context contextData;
        private ArrayList<BankDetailsModel> BeneListData;
        private TextView BeneName, BeneAccountNumber, BeneBankName, btn_recipient_id;
        private Button btnPayOrActive, btn_delete;

        bankDetailAdaptorClass(Context context, ArrayList<BankDetailsModel> arrayList) {
            contextData = context;
            BeneListData = arrayList;
        }

        @Override
        public int getCount() {
            return BeneListData.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) contextData.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.new_dmr_listview_text, parent, false);

            BeneName = (TextView) view.findViewById(R.id.tv_bene_name);
            BeneAccountNumber = (TextView) view.findViewById(R.id.tv_account_no);
            BeneBankName = (TextView) view.findViewById(R.id.tv_bank_name);
            btn_recipient_id = (TextView) view.findViewById(R.id.btn_recipient_id);
            btn_delete =  view.findViewById(R.id.btn_delete);
            btnPayOrActive =  view.findViewById(R.id.btn_pay_active_now);

            BeneName.setText(BeneListData.get(position).getBeneName());
            BeneAccountNumber.setText(BeneListData.get(position).getAccount());
            BeneBankName.setText(BeneListData.get(position).getBankName());
            btn_recipient_id.setText(BeneListData.get(position).getRecipientId());

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteBeneDialog(BeneListData.get(position).getBeneName(), BeneListData.get(position).getRecipientId(), 1);
                }
            });

            btnPayOrActive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contextData, ImpsNeftActivity.class);
                    intent.putExtra("BeneName", BeneListData.get(position).getBeneName());
                    intent.putExtra("BeneAccountNumber", BeneListData.get(position).getAccount());
                    intent.putExtra("BeneBankName", BeneListData.get(position).getBankName());
                    intent.putExtra("IFSCcode", BeneListData.get(position).getIfsc());
                    intent.putExtra("RecipientId", BeneListData.get(position).getRecipientId());
                    intent.putExtra("IMPS", BeneListData.get(position).getIMPS());
                    intent.putExtra("NEFT", BeneListData.get(position).getNEFT());
                    intent.putExtra("Channel", BeneListData.get(position).getChannel());
                    intent.putExtra("RecipientIdType", BeneListData.get(position).getRecipientIdType());
                    intent.putExtra("BeneMobile", BeneListData.get(position).getBeneMobile());
                    contextData.startActivity(intent);
                }
            });

            return view;
        }
    }

    //Full Data
    private void getListBene() {
        try {
            JSONArray jsonArray = MainActivity.jsonObjectStatic.optJSONArray("BeneList");

            if (jsonArray!=null && jsonArray.length() != 0) {
                L.m2("check_beneList--", jsonArray.toString());
                tvEmptyList.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                customerDetails = new ArrayList<>();
                customerDetails.clear();
                L.m2("Array lenght ", jsonArray.length() + "");

                JSONObject jsonObject1;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject1 = jsonArray.getJSONObject(i);
                    customerData = new BankDetailsModel();
                    customerData.setRecipientId(jsonObject1.getString("RecipientId"));
                    customerData.setRecipientIdType(jsonObject1.getString("RecipientIdType"));
                    customerData.setBeneName(jsonObject1.getString("BeneName"));
                    customerData.setAccount(jsonObject1.getString("Account"));
                    customerData.setIfsc(jsonObject1.getString("Ifsc"));
                    customerData.setBankName(jsonObject1.getString("BankName"));
                    customerData.setBeneMobile(jsonObject1.getString("BeneMobile"));
                    customerData.setIMPS(jsonObject1.getString("IMPS"));
                    customerData.setNEFT(jsonObject1.getString("NEFT"));
                    customerData.setChannel(jsonObject1.getString("Channel"));
                    customerDetails.add(customerData);
                }
                listView.setAdapter(new bankDetailAdaptorClass(getActivity(), customerDetails));
            } else {
                tvEmptyList.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //================For Delete Bene===============
    public void deleteBeneDialog(String beneName, final String recipientId, final int i) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(getActivity(), R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.sender_resister_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnOK =  d.findViewById(R.id.btn_resister_ok);
        final Button btnNO =  d.findViewById(R.id.btn_resister_no);
        final TextView tvMessage = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        final TextView title = (TextView) d.findViewById(R.id.title);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);

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

        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 1) {
                    DeleteBeneRequest(recipientId);
                    d.dismiss();
                }
                if (i == 2) {
                    findSenderRequest();
                    d.dismiss();
                }
            }
        });

        btnClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                d.cancel();
            }
        });
        d.show();
    }

    //Json request for Delete Bene
    private void DeleteBeneRequest(String recipientId) {
        pd = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {

            JSONObject jsonObjectReq = new JSONObject()
                    .put("Key", txnKey)
                    .put("AgentID", agentID)
                    .put("BeneficiaryId", recipientId)
                    .put("SenderId", mobileNumber);

            L.m2("url--deleteBene", url_delete_bene);
            Log.e("Req--deleteBene", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_delete_bene, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            L.m2("resp--deleteBene", object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    deleteBeneDialog(object.getString("message"), "", 2);
                                } else {
                                    Toast.makeText(getActivity(), object.getString("message").toString(), Toast.LENGTH_SHORT).show();
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

    private void findSenderRequest() {
        pd = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("Key", txnKey)
                    .put("AgentID", agentID)
                    .put("SenderId", mobileNumber);

            L.m2("url-called", url_find_sender);
            Log.e("Request", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_find_sender, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();

                            L.m2("url data", object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    MainActivity.jsonObjectStatic = object;
                                    getListBene();
                                } else {
                                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
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
