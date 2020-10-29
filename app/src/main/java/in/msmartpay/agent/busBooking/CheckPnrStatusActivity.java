package in.msmartpay.agent.busBooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;

/**
 * Created by Smartkinda on 8/12/2017.
 */

public class CheckPnrStatusActivity extends BaseActivity {

    private EditText editPNR;
    private Button btnPNR;
    private ProgressDialog pd;
    private String url_pnr_status_penality = HttpURL.PNR_STATUS_PENALITY_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_check_pnr_status);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Inquiry");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        editPNR =  findViewById(R.id.edit_pnr_status);
        btnPNR =  findViewById(R.id.btn_pnr_status_get);

        btnPNR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editPNR.getText().toString().trim())){
                    editPNR.requestFocus();
                    Toast.makeText(CheckPnrStatusActivity.this, "Please enter PNR number !!!", Toast.LENGTH_SHORT).show();
                }else {
                    hideKeyBoard(editPNR);
                    try {
                        PNRDetailsRequest();
                    }catch (Exception exp){
                        Toast.makeText(CheckPnrStatusActivity.this, "Something went wrong !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    //===========Get PNR Details==============
    //Json request for Seat Details
    private void PNRDetailsRequest() {
        pd = ProgressDialog.show(CheckPnrStatusActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("TransactionId", editPNR.getText().toString());

            Log.e("PNRDetail--Request", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_pnr_status_penality, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            System.out.println("PNRDetail--Object>" + object.toString());
                            System.out.println("PNRDetail--URL>" + url_pnr_status_penality);
                            try {
                                if (object.getString("status").equalsIgnoreCase("0")) {
                                    L.m2("PNRDetail--url-called", url_pnr_status_penality);
                                    L.m2("PNRDetail--url-data", object.toString());
                                    //Toast.makeText(CheckPnrStatusActivity.this, object.getString("Remarks").toString(), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(CheckPnrStatusActivity.this, PnrDetailsActivity.class);
                                        intent.putExtra("PnrStatusPenality", object.toString());
                                        intent.putExtra("PnrNumber", editPNR.getText().toString().trim());
                                        startActivity(intent);
                                } else {
                                    Toast.makeText(CheckPnrStatusActivity.this, object.getString("Remarks").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(CheckPnrStatusActivity.this).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }

    }
    //=======================================
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }
}
