package msmartpay.in.collectBanks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class CollectBankActivity extends BaseActivity {
    private Toolbar toolbar;
    private RecyclerView rv_list_bank;
    private String txnKey, agent_id;
    private Context context;
    private SharedPreferences myPrefs;
    private ProgressDialog pd;
   private ArrayList<CollectBankModel> bankList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_collect_activity);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        rv_list_bank = findViewById(R.id.rv_list_bank);

        getSupportActionBar().setTitle("Bank Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = CollectBankActivity.this;
        myPrefs = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        txnKey = myPrefs.getString("txn-key", "");
        agent_id = myPrefs.getString("agentonlyid", "");

        getBankDetails();
    }

    private void getBankDetails()  {

        pd = ProgressDialog.show(CollectBankActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("txn_key", txnKey)
                    .put("agent_id", agent_id);

            L.m2("summary_Request--1>", jsonObjectReq.toString());

            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, HttpURL.CollectBankDetails, jsonObjectReq,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            try {
                                if (object.get("response-code") != null && object.get("response-code").equals("0")) {
                                    final JSONArray parentArray = (JSONArray) object.get("Bank_List");
                                    if (bankList == null)
                                        bankList = new ArrayList<>();
                                    else
                                        bankList.clear();

                                    if (parentArray.length() > 0) {
                                        for (int i = 0; i < parentArray.length(); i++) {
                                            JSONObject obj = (JSONObject) parentArray.get(i);

                                            CollectBankModel bankDetailsItem = new CollectBankModel();


                                            bankDetailsItem.setBank_name(obj.get("bank_name") + "");
                                            bankDetailsItem.setBank_account(obj.get("bank_account") + "");
                                            bankDetailsItem.setBank_account_name(obj.get("bank_account_name") + "");
                                            bankDetailsItem.setBnk_ifsc(obj.get("bnk_ifsc") + "");

                                            bankList.add(bankDetailsItem);

                                        }
                                        CollectBankAdapter adapterCurrent = new CollectBankAdapter(getApplicationContext(), bankList);
                                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1);
                                        rv_list_bank.setLayoutManager(layoutManager);
                                        rv_list_bank.setItemAnimator(new DefaultItemAnimator());
                                        rv_list_bank.setAdapter(adapterCurrent);


                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(CollectBankActivity.this, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);

        } catch (Exception exp) {
            pd.dismiss();
            exp.printStackTrace();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {

        return super.onSupportNavigateUp();
    }
}
