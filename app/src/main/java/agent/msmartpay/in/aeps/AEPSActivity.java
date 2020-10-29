package agent.msmartpay.in.aeps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import in.co.eko.ekopay.EkoPayActivity;
import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;

public class AEPSActivity extends BaseActivity {

    private Button btnAeps;
    private SharedPreferences sharedPreferences;
    private String agentID="",txnKey="";
    private int AEPS_REQUEST_CODE = 10923;
    private String language = "en";
    private String GET_AEPS_ACCESS_KEY = HttpURL.GET_AEPS_ACCESS_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("AEPS");

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);

        btnAeps =  findViewById(R.id.proceed);
        btnAeps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getAccessKey();

            }
        });
    }

    private void getAccessKey() {


        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txnKey);
            L.m2(GET_AEPS_ACCESS_KEY+": Request :  ", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, GET_AEPS_ACCESS_KEY, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {

                            if(object!=null)
                                L.m2("object : ", object.toString());
                            try {

                                if (object.optString("Status").equalsIgnoreCase("0")) {
                                    JSONObject jsondata=object.getJSONObject("data");
                                    Bundle bundle = new Bundle();
                                    bundle.putString("environment", HttpURL.environment);
                                    // Optional
                                    // Default value "production"  If environment not passed then, default environment will be "production"
                                    // Expected values "uat" for beta testing or"production" for production environment
                                    bundle.putString("product","aeps");
                                    // mandatory
                                    bundle.putString("secret_key_timestamp",jsondata.getString("secret_key_timestamp"));
                                    // mandatory
                                    bundle.putString("secret_key",jsondata.getString("secret_key"));
                                    // mandatory
                                    bundle.putString("developer_key", jsondata.getString("developer_key"));
                                    // mandatory
                                    bundle.putString("initiator_id", jsondata.getString("initiator_id"));
                                    // mandatory
                                    bundle.putString("callback_url", HttpURL.EKO_AEPS_CALLBACK_URL);
                                    // mandatory
                                    bundle.putString("user_code", jsondata.getString("user_code"));
                                    // mandatory
                                    bundle.putString("initiator_logo_url", HttpURL.LOGO);
                                    // mandatory
                                    bundle.putString("partner_name" , jsondata.getString("partner_name"));
                                    // mandatory
                                    bundle.putString("language", language);
                                    // Optional

                                    // bundle.putString("callback_url_custom_params", callback_url_custom_params);
                                    // Optional

                                    // bundle.putString("callback_url_custom_headers",  callback_url_custom_headers);
                                    // Optional

                                    Intent intent = new Intent(getApplicationContext(), EkoPayActivity.class);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, AEPS_REQUEST_CODE);

                                } else {
                                    //Toast.makeText(AEPSActivity.this, "Server Error : " + object.optString("response-message").toString(), Toast.LENGTH_SHORT).show();
                                    errorAlert(object.optString("message"));
                                }
                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    //================For errorAlert===============
    public void errorAlert(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(getApplicationContext(), R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.new_dmr_sender_resister_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnOK =  d.findViewById(R.id.btn_resister_ok);
        final Button btnNO =  d.findViewById(R.id.btn_resister_no);
        final TextView tvMessage = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);

        tvMessage.setText(msg);
        btnNO.setVisibility(View.GONE);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AEPS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) { //user taps CLOSE button after transaction -- case 1
                String response = data.getStringExtra("result");
                //--------- response is  transaction data
            } else if(resultCode == Activity.RESULT_CANCELED) { // user presses back button
                if (data == null) {
                    // ------ If user pressed back without transaction -- case 2
                } else {
                    String response = data.getStringExtra("result");
                    if (response!=null && !response.equalsIgnoreCase("")) {
                        //------ If there is some error in partner parameters, response is that error
                        //------ when user performs the transaction, response is transaction data -- case 1
                    } else {

                    }
                }
            }
        }
    }
}
