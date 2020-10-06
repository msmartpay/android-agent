package msmartpay.in;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;

/**
 * Created by Harendra on 7/17/2017.
 */

public class OTPRegisterActivity extends BaseActivity {
    private EditText et_res_otp;
    private Button btn_register;
    private TextView btn_otp_resend;
    private Context ctx;
    private String url= HttpURL.OTP_MOBILE_REGISTER,resend= HttpURL.NEW_MOBILE_REGISTER;
    private ProgressDialog pd;
    private Dialog dialog_status;
    private boolean flag=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.otp_register);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      /*  setTitle("Verify OTP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        ctx=OTPRegisterActivity.this;
        et_res_otp =  findViewById(R.id.et_res_otp);
        btn_register =  findViewById(R.id.btn_res_otp);
        btn_otp_resend= (TextView) findViewById(R.id.btn_otp_resend);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectionAvailable()) {
                    if (!TextUtils.isEmpty(et_res_otp.getText().toString())&&et_res_otp.getText().toString().length()==6) {
                        sendOTP();
                    } else {
                        Toast.makeText(ctx, "Please,enter 6 digits otp!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ctx,"Internet Connection Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_otp_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectionAvailable()) {
                        resendOTP();
                }else{
                    Toast.makeText(ctx,"Internet Connection Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendOTP() {
        pd = new ProgressDialog(ctx);
        pd = ProgressDialog.show(ctx, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        try {
            JSONObject jsonObject=  new JSONObject().put("mobile",getIntent().getStringExtra("mob") )
                    .put("otp", et_res_otp.getText().toString());
            L.m2("request_data",jsonObject.toString());
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url,jsonObject
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject data) {
                    try {
                        pd.dismiss();
                        L.m2("url-->", url);
                        L.m2("data receive", data.toString());

                        try {
                            if (data.get("Status") != null && data.get("Status").equals("0")) {
                                flag=true;
                                showDiallog(data);
                            }else if(data.get("Status") != null && data.get("Status").equals("2")){
                                flag=false;
                                showDiallog(data);
                            }else if(data.get("Status") != null && data.get("Status").equals("1")){
                                flag=false;
                                Toast.makeText(ctx, "Some Technical Problem.\n "+data.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        showDiallog(data);
                    }catch (Exception e) {
                        pd.dismiss();
                        e.printStackTrace();
                        L.m2("data failuer",e.toString());
                    }
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Log.e("Volley : ",error.toString());
                    Toast.makeText(ctx, "Volley "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(ctx).addToRequsetque(objectRequest);
        } catch (Exception e) {
            pd.dismiss();
            L.m2("data failuer", e.toString());
            Toast.makeText(ctx, ""+e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void resendOTP() {
        pd = new ProgressDialog(ctx);
        pd = ProgressDialog.show(ctx, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        try {
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, resend,
                    new JSONObject().put("mobile", getIntent().getStringExtra("mob"))
                            .put("version", "5.0"), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject data) {
                    try {
                        pd.dismiss();
                        L.m2("url-->", url);
                        L.m2("data receive", data.toString());
                        try {
                            if (data.get("Status") != null && data.get("Status").equals("0")) {
                                Toast.makeText(ctx, "data failuer "+data.get("rmessage"), Toast.LENGTH_SHORT).show();
                            }else if(data.get("Status") != null && data.get("Status").equals("2")){
                                Toast.makeText(ctx, "data failuer "+data.get("message"), Toast.LENGTH_SHORT).show();
                            }else if(data.get("Status") != null && data.get("Status").equals("1")){
                                Toast.makeText(ctx, "data failuer "+data.get("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e) {
                        pd.dismiss();
                        e.printStackTrace();
                        Toast.makeText(ctx, "data failuer "+e.toString(), Toast.LENGTH_SHORT).show();
                        L.m2("data failuer",e.toString());
                    }
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Log.e("Volley : ",error.toString());
                    Toast.makeText(ctx, "Volley "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(ctx).addToRequsetque(objectRequest);
        } catch (Exception e) {
            pd.dismiss();
            L.m2("data failuer", e.toString());
            Toast.makeText(ctx, ""+e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private   void showDiallog(JSONObject data){
        dialog_status = new Dialog(ctx);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert);
        dialog_status.setCancelable(true);
        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
        statusImage.setImageResource(R.drawable.about);
        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);

        try {
            text.setText((String) data.get("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Button trans_status =  dialog_status.findViewById(R.id.trans_status_button);
        trans_status.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(flag==false){
                    dialog_status.dismiss();
                }else{
                    dialog_status.dismiss();
                    Intent intent = new Intent();
                    intent.setClass(ctx, RegisterComplete.class);
                    intent.putExtra("mob", getIntent().getStringExtra("mob"));
                    startActivity(intent);
                    finish();
                }

            }
        });

        dialog_status.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
