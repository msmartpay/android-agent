package in.msmartpay.agent.user;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainRequest;
import in.msmartpay.agent.network.model.user.DistrictData;
import in.msmartpay.agent.network.model.user.DistrictRequest;
import in.msmartpay.agent.network.model.user.DistrictResponse;
import in.msmartpay.agent.network.model.user.KycStatusResponse;
import in.msmartpay.agent.network.model.user.KycUpdateRequest;
import in.msmartpay.agent.network.model.user.ProfileResponse;
import in.msmartpay.agent.network.model.user.ProfileUpdateRequest;
import in.msmartpay.agent.network.model.user.StateData;
import in.msmartpay.agent.network.model.user.StateResponse;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;


public class MyProfileold extends BaseActivity {
    private String agentID;
    private static final int SELECTED_PICTURE = 1;
    final int PIC_CROP = 3;
    private ImageView displayphoto;
    private JSONObject data = null;
    private Uri picUri;
    public static Bitmap thePic = null;
    private Button btn_addAmount, btn_kycApply, btn_edit;
    private String balance, agent_id_full, emailId, agentName, agentMobile, kycstatus;
    private String txn_key, addmoney, firmname;
    private TextView bal_value, tv_info_text;
    private ImageView iv_kyc;
    private AutoCompleteTextView actv_p_gender, actv_p_country, actv_p_state, actv_p_district, actv_p_city;
    private EditText et_p_name, et_p_age, et_p_mob, et_p_email, et_p_address, et_p_adhar, et_p_pan_no, et_p_pin, et_p_last_name, et_p_holder_name;
    private TextInputLayout til_adhar_holder, til_adhar;
    private Context context;
    private ProgressDialogFragment pd, pd2;
    private ProgressBar pb_p_district, pb_p_state;
    boolean flag = true, flagKYC = true;
    private Bitmap dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sk_my_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("My Profile");
        context = MyProfileold.this;

        addmoney = Util.LoadPrefData(getApplicationContext(), Keys.ADD_MONEY);
        agent_id_full = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_FULL);
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        emailId = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_EMAIL);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        agentName = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_NAME);
        agentMobile = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_MOB);
        balance = Util.LoadPrefData(getApplicationContext(), Keys.BALANCE);

        tv_info_text = (TextView) findViewById(R.id.tv_info_text);
        bal_value = (TextView) findViewById(R.id.tv_amount);
        bal_value.setText("\u20B9 " + balance);

        et_p_name = (EditText) findViewById(R.id.et_p_name);
        et_p_age = (EditText) findViewById(R.id.et_p_age);
        et_p_mob = (EditText) findViewById(R.id.et_p_mob);
        et_p_address = (EditText) findViewById(R.id.et_p_address);
        et_p_email = (EditText) findViewById(R.id.et_p_email);
        et_p_adhar = (EditText) findViewById(R.id.et_p_adhar);
        et_p_pan_no = (EditText) findViewById(R.id.et_p_pan_no);
        et_p_pin = (EditText) findViewById(R.id.et_p_pin);
        et_p_last_name = (EditText) findViewById(R.id.et_p_last_name);
        et_p_holder_name = (EditText) findViewById(R.id.et_p_holder_name);


        actv_p_gender = (AutoCompleteTextView) findViewById(R.id.actv_p_gender);
        actv_p_country = (AutoCompleteTextView) findViewById(R.id.actv_p_country);
        actv_p_state = (AutoCompleteTextView) findViewById(R.id.actv_p_state);
        actv_p_district = (AutoCompleteTextView) findViewById(R.id.actv_p_district);
        actv_p_city = (AutoCompleteTextView) findViewById(R.id.actv_p_city);
        pb_p_state = (ProgressBar) findViewById(R.id.pb_p_state);
        pb_p_district = (ProgressBar) findViewById(R.id.pb_p_district);
        //Non-Editable
        nonEditable();

        til_adhar = (TextInputLayout) findViewById(R.id.til_adhar);
        til_adhar_holder = (TextInputLayout) findViewById(R.id.til_adhar_holder);
        btn_kycApply = (Button) findViewById(R.id.btn_kycApply);
        til_adhar_holder.setVisibility(View.GONE);
        til_adhar.setVisibility(View.GONE);

        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_addAmount = (Button) findViewById(R.id.btn_addAmount);
        btn_addAmount.setVisibility(View.GONE);
        iv_kyc = (ImageView) findViewById(R.id.tv_kyc);
        getProfile();

        btn_kycApply.setOnClickListener(view -> {
            String text = btn_kycApply.getText().toString();
            if (text.equalsIgnoreCase("Request Now")) {
                btn_kycApply.setText("Apply");
                et_p_adhar.setEnabled(true);
                et_p_adhar.setFocusableInTouchMode(true);
                et_p_holder_name.setEnabled(true);
                et_p_holder_name.setFocusableInTouchMode(true);
                til_adhar_holder.setVisibility(View.VISIBLE);
                til_adhar.setVisibility(View.VISIBLE);
            } else if (text.equalsIgnoreCase("Apply")) {
                if (TextUtils.isEmpty(et_p_adhar.getText().toString())) {
                    Toast.makeText(context, "  Enter Adhar Number!  ", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(et_p_holder_name.getText().toString())) {
                    Toast.makeText(context, "  Enter Adhar Holder Name!  ", Toast.LENGTH_SHORT).show();
                } else {
                    getKYC();
                }
            } else if (text.equalsIgnoreCase("View")) {
                btn_kycApply.setText("Hide");
                et_p_adhar.setEnabled(false);
                et_p_adhar.setFocusableInTouchMode(false);
                et_p_holder_name.setEnabled(false);
                et_p_holder_name.setFocusableInTouchMode(false);
                til_adhar_holder.setVisibility(View.VISIBLE);
                til_adhar.setVisibility(View.VISIBLE);
            } else if (text.equalsIgnoreCase("Hide")) {
                btn_kycApply.setText("View");
                til_adhar_holder.setVisibility(View.GONE);
                til_adhar.setVisibility(View.GONE);
            }
        });
        btn_edit.setOnClickListener(view -> {
            String edit_done = btn_edit.getText().toString();
            if (edit_done.equalsIgnoreCase("Edit")) {
                getState();
                btn_edit.setText("Done");
                editable();
            } else {
                getProfileUpdate();
            }
        });
        et_p_age.setOnClickListener(view -> {
            Calendar newCalendar = Calendar.getInstance();
            DatePickerDialog fromDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    try {
                        String colDepartDate = Util.getDate(year, monthOfYear, dayOfMonth);
                        if (!colDepartDate.equalsIgnoreCase("")) {

                            et_p_age.setText(colDepartDate);
                        } else {
                            Toast.makeText(context, "Invalid From Date.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            //fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            fromDatePickerDialog.show();
        });

        actv_p_state.setOnItemClickListener((adapterView, view, i, l) ->
                getDistrict((String) adapterView.getItemAtPosition(i)));


        displayphoto = (ImageView) findViewById(R.id.id_my_profile_picc);
        if (null != dp) {
            displayphoto.setImageBitmap(dp);
        }

        //chang picture
        displayphoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SELECTED_PICTURE);
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getProfile() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Profile ...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            MainRequest request = new MainRequest();
            request.setAgentID(agentID);
            request.setTxn_key(txn_key);

            RetrofitClient.getClient(getApplicationContext())
                    .getProfile(request).enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(@NotNull Call<ProfileResponse> call, @NotNull retrofit2.Response<ProfileResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        ProfileResponse res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {
                            kycstatus = res.getProfile().getKycstatus();
                            getKYCStatus();
                            if (kycstatus.equalsIgnoreCase("1")) {//0-not Activated and 1-Activated
                                btn_kycApply.setText("View");
                            } else if (kycstatus.equalsIgnoreCase("0")) {
                                btn_kycApply.setText("Apply");
                            }
                            firmname = res.getProfile().getFirmname();
                            et_p_name.setText(res.getProfile().getFirstname());
                            et_p_last_name.setText(res.getProfile().getLastname());
                            et_p_age.setText(res.getProfile().getDateOfBirth());
                            et_p_mob.setText(res.getProfile().getMobile());
                            actv_p_gender.setText(res.getProfile().getGender());
                            et_p_email.setText(res.getProfile().getEmailID());
                            actv_p_country.setText(res.getProfile().getCountry());
                            actv_p_state.setText(res.getProfile().getState());
                            actv_p_city.setText(res.getProfile().getCity());
                            et_p_address.setText(res.getProfile().getAddress());
                            actv_p_district.setText(res.getProfile().getDistrict());
                            et_p_pan_no.setText(res.getProfile().getPannumber());
                            et_p_pin.setText(res.getProfile().getPincode());
                            et_p_adhar.setText(res.getProfile().getAdharnumber());
                            L.toastS(getApplicationContext(), res.getMessage());
                        } else {

                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ProfileResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    private void getKYCStatus() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
             pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Kyc status ...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            MainRequest request = new MainRequest();
            request.setAgentID(agentID);
            request.setTxn_key(txn_key);

            RetrofitClient.getClient(getApplicationContext())
                    .getKycDetails(request).enqueue(new Callback<KycStatusResponse>() {
                @Override
                public void onResponse(@NotNull Call<KycStatusResponse> call, @NotNull retrofit2.Response<KycStatusResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        KycStatusResponse res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {
                            til_adhar_holder.setVisibility(View.GONE);
                            til_adhar.setVisibility(View.GONE);
                            tv_info_text.setVisibility(View.GONE);
                            til_adhar_holder.setVisibility(View.GONE);
                            til_adhar.setVisibility(View.GONE);
                            iv_kyc.setVisibility(View.GONE);
                            btn_kycApply.setVisibility(View.VISIBLE);
                            tv_info_text.setVisibility(View.VISIBLE);
                            et_p_adhar.setText(res.getAdharcardNo());
                            et_p_holder_name.setText(res.getAdharHoldername());
                            if (!res.getKycRequeststatus().equalsIgnoreCase("Pending")) {
                                tv_info_text.setText(res.getKycRequeststatus());
                                btn_kycApply.setText("View");
                            } else {
                                btn_kycApply.setText("View");
                                tv_info_text.setText("Your request is under process.");
                            }
                            L.toastS(getApplicationContext(), res.getMessage());
                        } else if (res.getStatus() != null && res.getStatus().equals("1")) {
                            iv_kyc.setVisibility(View.GONE);
                            tv_info_text.setText("Upgrade KYC,to get exclusive benefits.");
                            btn_kycApply.setText("Request Now");
                        } else {
                            L.toastS(getApplicationContext(), "No Response");
                        }
                    } else {
                        L.toastS(getApplicationContext(), "No Response");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<KycStatusResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    private void getKYC() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Kyc ...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            KycUpdateRequest request = new KycUpdateRequest();
            request.setAgentID(agentID);
            request.setTxn_key(txn_key);
            request.setAdharcardNo(et_p_adhar.getText().toString());
            request.setAdharHoldername(et_p_holder_name.getText().toString());
            request.setEmail(et_p_email.getText().toString());
            request.setMobileno(et_p_mob.getText().toString());

            RetrofitClient.getClient(getApplicationContext())
                    .updateKyc(request).enqueue(new Callback<KycStatusResponse>() {
                @Override
                public void onResponse(@NotNull Call<KycStatusResponse> call, @NotNull retrofit2.Response<KycStatusResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        KycStatusResponse res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {
                            til_adhar_holder.setVisibility(View.GONE);
                            til_adhar.setVisibility(View.GONE);
                            tv_info_text.setVisibility(View.VISIBLE);
                            tv_info_text.setText(res.getMessage());
                            btn_kycApply.setVisibility(View.GONE);
                            iv_kyc.setVisibility(View.GONE);
                            L.toastS(getApplicationContext(), res.getMessage());
                        } else if (res.getStatus() != null && res.getStatus().equals("1")) {
                            L.toastS(getApplicationContext(), res.getMessage());
                        } else if (res.getStatus() != null && res.getStatus().equals("2")) {
                            til_adhar_holder.setVisibility(View.GONE);
                            til_adhar.setVisibility(View.GONE);
                            tv_info_text.setVisibility(View.GONE);
                            btn_kycApply.setVisibility(View.GONE);
                            iv_kyc.setVisibility(View.GONE);
                            L.toastS(getApplicationContext(), res.getMessage());
                        } else {
                            L.toastS(getApplicationContext(), "No Response");
                        }
                    } else {
                        L.toastS(getApplicationContext(), "No Response");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<KycStatusResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    private void getState() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pb_p_state.setVisibility(View.VISIBLE);
            MainRequest request = new MainRequest();
            request.setAgentID(agentID);
            request.setTxn_key(txn_key);

            RetrofitClient.getClient(getApplicationContext())
                    .getState(request).enqueue(new Callback<StateResponse>() {
                @Override
                public void onResponse(@NotNull Call<StateResponse> call, @NotNull retrofit2.Response<StateResponse> response) {
                    pb_p_state.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        StateResponse res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {
                            ArrayList<String> arrayListState = new ArrayList<>();
                            if (res.getStateList() != null)
                                for (StateData data : res.getStateList()) {
                                    arrayListState.add(data.getState());
                                }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayListState);
                            actv_p_state.setAdapter(adapter);
                            actv_p_state.setThreshold(1);
                            if (flag) {
                                flag = false;
                                getDistrict(actv_p_state.getText().toString());
                            }
                            L.toastS(getApplicationContext(), res.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<StateResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pb_p_state.setVisibility(View.GONE);
                }
            });
        }
    }

    private void getDistrict(String state) {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pb_p_district.setVisibility(View.VISIBLE);
            DistrictRequest request = new DistrictRequest();
            request.setAgentID(agentID);
            request.setTxn_key(txn_key);
            request.setState(state);

            RetrofitClient.getClient(getApplicationContext())
                    .getStateDistrict(request).enqueue(new Callback<DistrictResponse>() {
                @Override
                public void onResponse(@NotNull Call<DistrictResponse> call, @NotNull retrofit2.Response<DistrictResponse> response) {
                    pb_p_district.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        DistrictResponse res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {
                            ArrayList<String> arrayListDistrict = new ArrayList<>();
                            if (res.getDistrictList() != null)
                                for (DistrictData data : res.getDistrictList()) {
                                    arrayListDistrict.add(data.getDistrict());
                                }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayListDistrict);
                            actv_p_district.setAdapter(adapter);
                            actv_p_district.setThreshold(1);
                        } else {
                            L.toastS(getApplicationContext(), res.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DistrictResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pb_p_district.setVisibility(View.GONE);
                }
            });
        }
    }

    private void getProfileUpdate() {
        if (getValidation()) {

            if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
                pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Updating Profile ...");
                ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

                ProfileUpdateRequest request = new ProfileUpdateRequest();
                request.setAgentID(agentID);
                request.setTxn_key(txn_key);
                request.setFirmname(firmname);
                request.setFirstname(et_p_name.getText().toString());
                request.setLastname(et_p_last_name.getText().toString());
                request.setDateOfBirth(et_p_age.getText().toString());
                request.setMobile(et_p_mob.getText().toString());
                request.setGender(actv_p_gender.getText().toString());
                request.setEmailID(et_p_email.getText().toString());
                request.setCountry(actv_p_country.getText().toString());
                request.setState(actv_p_state.getText().toString());
                request.setCity(actv_p_city.getText().toString());
                request.setAddress(et_p_address.getText().toString());
                request.setDistrict(actv_p_district.getText().toString());
                request.setPannumber(et_p_pan_no.getText().toString());
                request.setPincode(et_p_pin.getText().toString());

                RetrofitClient.getClient(getApplicationContext())
                        .getUpdateProfile(request).enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<ProfileResponse> call, @NotNull retrofit2.Response<ProfileResponse> response) {
                        pd.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            ProfileResponse res = response.body();
                            if (res.getStatus() != null && res.getStatus().equals("0")) {
                                btn_edit.setText("Edit");
                                nonEditable();
                                L.toastS(context, res.getMessage());
                            } else {
                                L.toastS(context, res.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ProfileResponse> call, @NotNull Throwable t) {
                        L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                        pd.dismiss();
                    }
                });
            }
        }

    }

    private void nonEditable() {
        et_p_last_name.setEnabled(false);
        et_p_last_name.setFocusableInTouchMode(false);
        et_p_pin.setEnabled(false);
        et_p_pin.setFocusableInTouchMode(false);
        et_p_pan_no.setEnabled(false);
        et_p_pan_no.setFocusableInTouchMode(false);
        et_p_name.setEnabled(false);
        et_p_name.setFocusableInTouchMode(false);
        et_p_age.setEnabled(false);
        et_p_age.setFocusableInTouchMode(false);
        et_p_mob.setEnabled(false);
        et_p_mob.setFocusableInTouchMode(false);
        actv_p_gender.setEnabled(false);
        actv_p_gender.setFocusableInTouchMode(false);
        et_p_email.setEnabled(false);
        et_p_email.setFocusableInTouchMode(false);
        actv_p_country.setEnabled(false);
        actv_p_country.setFocusableInTouchMode(false);
        actv_p_state.setEnabled(false);
        actv_p_state.setFocusableInTouchMode(false);
        actv_p_city.setEnabled(false);
        actv_p_city.setFocusableInTouchMode(false);
        et_p_address.setEnabled(false);
        et_p_address.setFocusableInTouchMode(false);
        actv_p_district.setEnabled(false);
        actv_p_district.setFocusableInTouchMode(false);
    }

    private void editable() {
        et_p_last_name.setEnabled(true);
        et_p_last_name.setFocusableInTouchMode(true);
        et_p_pin.setEnabled(true);
        et_p_pin.setFocusableInTouchMode(true);
        et_p_pan_no.setEnabled(true);
        et_p_pan_no.setFocusableInTouchMode(true);
        et_p_name.setEnabled(true);
        et_p_name.setFocusableInTouchMode(true);
        et_p_age.setEnabled(true);
        et_p_age.setFocusableInTouchMode(true);
        actv_p_gender.setEnabled(true);
        actv_p_gender.setFocusableInTouchMode(true);
        actv_p_district.setEnabled(true);
        actv_p_district.setFocusableInTouchMode(true);
        // actv_p_country.setEnabled(true);
        // actv_p_country.setFocusableInTouchMode(true);
        actv_p_state.setEnabled(true);
        actv_p_state.setFocusableInTouchMode(true);
        actv_p_city.setEnabled(true);
        actv_p_city.setFocusableInTouchMode(true);
        et_p_address.setEnabled(true);
        et_p_address.setFocusableInTouchMode(true);
    }

    private boolean getValidation() {

        if (TextUtils.isEmpty(et_p_name.getText().toString())) {
            Toast.makeText(context, "Please First Name!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_p_last_name.getText().toString())) {
            Toast.makeText(context, "Please Last Name!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_p_age.getText().toString())) {
            Toast.makeText(context, "Please Enter Age!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(actv_p_gender.getText().toString())) {
            Toast.makeText(context, "Please Enter Gender Type!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_p_pin.getText().toString())) {
            Toast.makeText(context, "Please Enter Pin Code!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(actv_p_state.getText().toString())) {
            Toast.makeText(context, "Please Enter State Name!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(actv_p_district.getText().toString())) {
            Toast.makeText(context, "Please Enter District Name!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(actv_p_city.getText().toString())) {
            Toast.makeText(context, "Please Enter City Name!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_p_address.getText().toString())) {
            Toast.makeText(context, "Please Enter Address!", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECTED_PICTURE:
                if (resultCode == RESULT_OK) {
                    picUri = data.getData();
                    performCrop();
                }
                break;
            case PIC_CROP:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    thePic = (Bitmap) extras.get("data");
                    displayphoto.setImageBitmap(thePic);
                }
                break;
            default:
                break;
        }
    }

    public void performCrop() {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
