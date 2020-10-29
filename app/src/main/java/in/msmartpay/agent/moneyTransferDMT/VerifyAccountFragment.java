package in.msmartpay.agent.moneyTransferDMT;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import in.msmartpay.agent.R;

/**
 * Created by Smartkinda on 7/8/2017.
 */

public class VerifyAccountFragment extends Fragment {

    private EditText editBeneName, editBeneMobile, editBeneIFSC, editBeneAccountNo;
    private Button btnVerifyAcc;
    private ProgressDialog pd;
    private JSONObject jsonObject;
  //  private String url_verify_account = Utility.VERIFY_ACCOUNT;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dmr_verify_account_activity, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        editBeneName =  view.findViewById(R.id.edit_beneficiary_name);
        editBeneMobile =  view.findViewById(R.id.edit_beneficiary_mobile);
        editBeneIFSC =  view.findViewById(R.id.edit_ifsc_code);
        editBeneAccountNo =  view.findViewById(R.id.edit_account_no);
        btnVerifyAcc =  view.findViewById(R.id.btn_verify_account);


        //Validation start from here...
        btnVerifyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(editBeneName.getText().toString().trim()))
                {
                    editBeneName.requestFocus();
                    Toast.makeText(getActivity(), "Please Enter Beneficiary Name", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editBeneMobile.getText().toString().trim()))
                {
                    editBeneMobile.requestFocus();
                    Toast.makeText(getActivity(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editBeneIFSC.getText().toString().trim()))
                {
                    editBeneIFSC.requestFocus();
                    Toast.makeText(getActivity(), "Please Enter IFSC Code", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editBeneAccountNo.getText().toString().trim()))
                {
                    editBeneAccountNo.requestFocus();
                    Toast.makeText(getActivity(), "Please Enter Account Number", Toast.LENGTH_SHORT).show();
                } else{
                    //VerifyAccountRequest();
                }

            }
        });


        return view;
    }

  /*  private void VerifyAccountRequest() {
        pd = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("", editBeneName.getText().toString())
                    .put("", editBeneMobile.getText().toString())
                    .put("", editBeneIFSC.getText().toString())
                    .put("", editBeneAccountNo.getText().toString());
            Log.e("Request",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_verify_account, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject=object;
                            System.out.println("Object----1>"+object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url-called", url_verify_account);
                                    L.m2("url data", object.toString());
//                                    showConfirmationDialog(object.getString("message").toString());
                                    showConfirmationDialog("Success");
                                }
                                else {
                                    pd.dismiss();
                                    showConfirmationDialog(object.getString("message").toString());
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse (VolleyError error){
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
           Mysingleton.getInstance(getActivity()).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }

    }
*/
    //Confirmation Dialog
    public void showConfirmationDialog(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(getActivity(), R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.confirmation_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnSubmit =  d.findViewById(R.id.btn_push_submit);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);

        final TextView tvConfirmation = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        tvConfirmation.setText(msg);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(jsonObject.getString("Status").equalsIgnoreCase("0")){
                        Intent intent = new Intent(getActivity(), VerifyAccountFragment.class);
                        startActivity(intent);
                        d.dismiss();
                    }else{
                        d.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                d.cancel();
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
}
