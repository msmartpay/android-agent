package agent.msmartpay.in;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;
import agent.msmartpay.in.utility.Service;

/**
 * Created by Harendra on 7/17/2017.
 */

public class RegisterComplete extends BaseActivity {
    private ProgressDialog pd;
    private Context context = null;
    private Dialog dialog_status;
    private LinearLayout linear_proceed_signup;
    private EditText fName, lName, email, mobile,etAddress,pincode;
    private String fNameText, lNameText, emailText, mobileText, companyText="",address,postalCode,cityName="",stateNmae="";
    private String sigup = HttpURL.REGISTER_URL;
    private boolean flag=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.signup_activity);
        context = RegisterComplete.this;
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        /*setTitle("New Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        fName =  findViewById(R.id.fname);
        lName =  findViewById(R.id.lname);
        email =  findViewById(R.id.email);
        mobile =  findViewById(R.id.mob);
        etAddress =  findViewById(R.id.address);
        pincode =   findViewById(R.id.pincode);
        linear_proceed_signup = (LinearLayout) findViewById(R.id.linear_proceed_signup);
        mobile.setText(getIntent().getStringExtra("mob"));
        linear_proceed_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRequest();
            }
        });
    }


    public void getRequest() {
      if(isConnectionAvailable()){
        fNameText = fName.getText().toString();
        lNameText = lName.getText().toString();
        emailText = email.getText().toString();
        mobileText = mobile.getText().toString();
        address =etAddress.getText().toString();
        postalCode =pincode.getText().toString();
        if (TextUtils.isEmpty(fNameText)) {
            Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(lNameText)) {
            Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(emailText)) {
            Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
        } else if (mobileText.length() < 10) {
            Toast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
        } /*else if (TextUtils.isEmpty(companyText)) {
            Toast.makeText(getActivity().getApplicationContext(), "Please Enter Firm Name", Toast.LENGTH_SHORT).show();
        }*/ else if (!Service.isValidEmail(emailText)) {
            Toast.makeText(this, "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please Enter Address", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(postalCode)) {
            Toast.makeText(this, "Please Enter Pincode", Toast.LENGTH_SHORT).show();
        }else if (postalCode.length()<6) {
            Toast.makeText(this, "Please Enter correct Pincode", Toast.LENGTH_SHORT).show();
        }else {
            pd = new ProgressDialog(this);
            pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            L.m2("test", sigup);
            try {
                JSONObject postParam= new JSONObject();
                postParam.put("firstname", fNameText);
                postParam.put("lastname", lNameText);
                postParam.put("firmname", companyText);
                postParam.put("email", emailText);
                postParam.put("mobileno", mobileText);
                postParam.put("address", address);
                postParam.put("state", stateNmae);
                postParam.put("city",cityName);
                postParam.put("pincode", postalCode);
                postParam .put("longitude", "");
                postParam.put("latitude", "");
                postParam.put("dsid",HttpURL.DSID);

                L.m2("test", postParam.toString());

                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, sigup,postParam
                        , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {
                        pd.dismiss();
                        try {
                            pd.dismiss();
                            if (data.get("Status") != null && data.get("Status").equals("0")) {
                                flag=true;
                                showDiallog(data);

                            } else if (data.get("Status") != null && data.get("Status").equals("1")) {
                                flag=false;
                                showDiallog(data);
                                Toast.makeText(context, (String) data.get("message"), Toast.LENGTH_SHORT).show();

                            } else if (data.get("Status") != null && data.get("Status").equals("2")) {
                                flag=false;
                                showDiallog(data);
                                Toast.makeText(context, (String) data.get("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                flag=false;
                                Toast.makeText(context, "Wrong UserName or Password! ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            pd.dismiss();
                            L.m2("Volley","Error "+ e.toString());
                            e.printStackTrace();
                        }
                        dialog_status.show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        L.m2("Volley","Error "+ error.toString());
                        Toast.makeText(context, "Something Error Occur ..!! Please Try Later "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                getSocketTimeOut(objectRequest);
                Mysingleton.getInstance(context).addToRequsetque(objectRequest);
            } catch (Exception e) {
                pd.dismiss();
                L.m2("Exception","Error "+ e.toString());
                e.printStackTrace();
            }
        }
    }else {
        Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
    }
    }
    private   void showDiallog(JSONObject data){
        dialog_status = new Dialog(RegisterComplete.this);
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
                    intent.setClass(RegisterComplete.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
