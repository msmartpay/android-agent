package in.msmartpay.agent.moneyTransferDMT;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;

/**
 * Created by Smartkinda on 7/4/2017.
 */

public class MoneyTransferActivity extends BaseActivity {

    private EditText editMobileNo, editFirstName, editLastName, editDOB, editPinCode, editAddress;
    private String MobileNumber, FirstName, LastName, DateOfBirth, PinCodeNumber, Address;
    private Button btnSubmit;
    private String mobileNo;
    private DatePickerDialog chddDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private FloatingActionButton floatingButton;
    private String url_update_details = HttpURL.UPDATE_DETAILS;
    private LinearLayout listView;
    private static String[] prgmNameList={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};
    private Context context;
    private TextView beneficiaryName, accountNumber, BankName, btnStatus;
    private Button btnIMPS, btnNEFT;
    LayoutInflater inflater = null;
    View localView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_sender_details_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Money Transfer");

        context = MoneyTransferActivity.this;
        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.requestFocusFromTouch();
                return false;
            }
        });

        mobileNo = getIntent().getStringExtra("MobileNo");

        editMobileNo =  findViewById(R.id.id_mobile_number);
        editFirstName =  findViewById(R.id.id_first_name);
        editLastName =  findViewById(R.id.id_last_name);
        editDOB =  findViewById(R.id.id_dob);
        editPinCode =  findViewById(R.id.id_pincode);
        editAddress =  findViewById(R.id.id_address);
        btnSubmit =  findViewById(R.id.id_update_details);

        //=====================================================================
        //Show list through the linear layout
//        listView = (LinearLayout) findViewById(R.id.listview);
        for(int i = 0; i< prgmNameList.length; i++) {

            inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            localView = inflater.inflate(R.layout.listview_text, null);

            beneficiaryName = (TextView) localView.findViewById(R.id.tv_bene_name);
            accountNumber = (TextView) localView.findViewById(R.id.tv_account_no);
            BankName = (TextView) localView.findViewById(R.id.tv_bank_name);
      //      btnIMPS =  localView.findViewById(R.id.btn_ipms);
      //      btnNEFT =  localView.findViewById(R.id.btn_neft);
            btnStatus = (TextView) localView.findViewById(R.id.btn_status);
            beneficiaryName.setText(prgmNameList[i]);
            listView.addView(localView, i);
            btnStatus.setOnClickListener(buttonstatus);
            btnStatus.setTag(Integer.toString(listView.indexOfChild(btnStatus)));

            btnNEFT.setOnClickListener(buttonNEFT);
            btnNEFT.setTag(Integer.toString(listView.indexOfChild(btnNEFT)));

            btnIMPS.setOnClickListener(buttonIMPS);
            btnIMPS.setTag(Integer.toString(listView.indexOfChild(btnIMPS)));


           /* btnStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeBeneficiaryStatusDialog(prgmNameList[listView.indexOfChild(localView)]);
                }
            });

            btnIMPS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImpsNeftActivity.class);
                    //               intent.putExtra("beneficiaryName",arrayListData.get(position).getBeneficiaryName());
                    //               intent.putExtra("accountNumber",arrayListData.get(position).getAccountNumber());
                    //               intent.putExtra("BankName",arrayListData.get(position).getBankName());
                    startActivity(intent);
                }
            });

            btnNEFT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, NEFTActivity.class);
                    //             intent.putExtra("AgentID",arrayListData.get(position).getAccountNumber());
                    //             intent.putExtra("agentList", ListData);
                    startActivity(intent);
                }
            });

        btnIMPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImpsNeftActivity.class);
 //               intent.putExtra("beneficiaryName",arrayListData.get(position).getBeneficiaryName());
 //               intent.putExtra("accountNumber",arrayListData.get(position).getAccountNumber());
 //               intent.putExtra("BankName",arrayListData.get(position).getBankName());
                startActivity(intent);
            }
        });

        btnNEFT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, NEFTActivity.class);
   //             intent.putExtra("AgentID",arrayListData.get(position).getAccountNumber());
   //             intent.putExtra("agentList", ListData);
                startActivity(intent);
            }
        });
  */
            //=====================================================================
        }
        floatingButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent floatIntent = new Intent(context, AddBeneficiaryActivity.class);
                startActivity(floatIntent);
            }
        });

        editMobileNo.setText(mobileNo);

        MobileNumber = editMobileNo.getText().toString().trim();
        FirstName = editFirstName.getText().toString().trim();
        LastName = editLastName.getText().toString().trim();
        DateOfBirth = editDOB.getText().toString().trim();
        PinCodeNumber = editPinCode.getText().toString().trim();
        Address = editAddress.getText().toString().trim();

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chddDatePickerDialog.show();
            }
        });
        findViewsById();
        setchDDDateTimeField();

        //Validation start from here...
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MobileNumber.length()<=9)
                {
                    editMobileNo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }else if(FirstName.length()<=0)
                {
                    editFirstName.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
                }else if(LastName.length()<=0)
                {
                    editLastName.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                }else if(DateOfBirth.length()<=0)
                {
                    Toast.makeText(getApplicationContext(), "Please Set Your Date Of Birth", Toast.LENGTH_SHORT).show();
                }else if(PinCodeNumber.length()<=5)
                {
                    editPinCode.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Pin Code", Toast.LENGTH_SHORT).show();
                }else if(Address.length()<=0)
                {
                    editAddress.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Address", Toast.LENGTH_SHORT).show();
                } else{
                    UpdateDetailsRequest();
                }

            }
        });
    }

    //For Set Date Of Birth
    private void findViewsById() {
        editDOB.setInputType(InputType.TYPE_NULL);
    }

    private void setchDDDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        chddDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                System.out.println("DateOfBirth--->"+editDOB.getText().toString());
                editDOB.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void UpdateDetailsRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("Mobile", MobileNumber)
                    .put("Firstname", FirstName)
                    .put("Lastname", LastName)
                    .put("dateofbirth", editDOB.getText().toString())
                    .put("Pincode", PinCodeNumber)
                    .put("Address", Address);
            Log.e("Request",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_update_details, jsonObjectReq,
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
                                    L.m2("url-called", url_update_details);
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
                    Toast.makeText(getApplicationContext(), "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }

    }

    //For Activte or Delete/Disable benificiary account status (Dialog)
    public void changeBeneficiaryStatusDialog(String data) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);

        d.setCancelable(false);

        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        d.setContentView(R.layout.dialog_sender_mobile_dmt);

        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnSubmit =  d.findViewById(R.id.btn_push_submit);
        Button btnClosed =  d.findViewById(R.id.close_push_button);
        final EditText editMobileNo =  d.findViewById(R.id.edit_push_balance);
        final TextView tViewTitle = (TextView) d.findViewById(R.id.title);
        final TextView tViewOTP = (TextView) d.findViewById(R.id.tview_otp_info);

        tViewOTP.setVisibility(View.VISIBLE);
        tViewOTP.setText("Please Enter OTP Sent On Sender Mobile Number to Validate Beneficiary.");
        editMobileNo.setText(data.toString());
       // editMobileNo.setHint("Enter OPT");
        tViewTitle.setText("Confirmation OTP");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editMobileNo.getText().toString().trim())) {
                    Toast.makeText(context, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();

                } else {
                    final String MobileNo = editMobileNo.getText().toString().trim();

                    Intent intent = new Intent(context, MoneyTransferActivity.class);
                    intent.putExtra("MobileNo", MobileNo);
                    startActivity(intent);
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

    //Confirmation Dialog
    public void showConfirmationDialog(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
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
                    if(jsonObject.getString("status").equalsIgnoreCase("0")){
                        Intent intent = new Intent(context, MoneyTransferActivity.class);
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

    View.OnClickListener buttonstatus = new View.OnClickListener() {
        public void onClick(View v) {

 //           int idxStr = Integer.parseInt(v.getTag());
 //           L.m2("idxStr--->", idxStr+"");

        }
    };
    View.OnClickListener buttonNEFT = new View.OnClickListener() {
        public void onClick(View v) {

            String idxStr = (String)v.getTag();

        }
    };
    View.OnClickListener buttonIMPS = new View.OnClickListener() {
        public void onClick(View v) {

            String idxStr = (String)v.getTag();

        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
