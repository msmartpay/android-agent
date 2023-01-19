package in.msmartpay.agent.rechargeBillPay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.aepssdkssz.util.Utility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.databinding.BillPostpaidRechargeActivityBinding;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.OperatorsRequest;
import in.msmartpay.agent.network.model.OperatorsResponse;
import in.msmartpay.agent.network.model.post.OperatorData;
import in.msmartpay.agent.network.model.post.OperatorResponse;
import in.msmartpay.agent.network.model.wallet.OperatorModel;
import in.msmartpay.agent.rechargeBillPay.operator.OperatorSearchActivity;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Service;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;

public class PostpaidMobileActivity extends BaseActivity {
    private BillPostpaidRechargeActivityBinding binding;
    private Uri uriContact;
    private ProgressDialogFragment pd;
    private Context context;
    private String agentID, txn_key = "";
    private List<OperatorModel> operatorList = null;
    private OperatorModel opreatorModel = null;

    private String mob, amt, operator;
    private OperatorData operatorData = null;
    private boolean isAd1 = false, isAd2 = false, isAd3 = false;
    private String ad1 = "", ad2 = "", ad3 = "";
    private final ActivityResultLauncher<Intent> activityContactPickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Log.d("Contact", "Response: " + data.toString());
                        uriContact = data.getData();
                        retrieveContactNumber();
                    }
                }
            });
    private final ActivityResultLauncher<Intent> activityOperatorLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.hasExtra(Keys.ARRAY_LIST))
                            operatorList = (ArrayList<OperatorModel>) Util.getListFromJson(data.getStringExtra(Keys.ARRAY_LIST), OperatorModel.class);
                        opreatorModel = Util.getGson().fromJson(data.getStringExtra(Keys.OBJECT), OperatorModel.class);
                        Objects.requireNonNull(binding.tilOperator.getEditText()).setText(opreatorModel.getDisplayName());
                        operatorDetailsRequest();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BillPostpaidRechargeActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mobile Postpaid");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = PostpaidMobileActivity.this;
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        Util.hideView(binding.tidAd1);
        Util.hideView(binding.tidAd2);
        Util.hideView(binding.tidAd3);
        Util.hideView(binding.ivContact);
        Util.showView(binding.btnProceed);
        binding.ivContact.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            activityContactPickLauncher.launch(intent);
        });

        //For operatorList
        operatorsCodeRequest();


        binding.etOperator.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OperatorSearchActivity.class);
            intent.putExtra(Keys.ARRAY_LIST, Util.getJsonFromModel(operatorList));
            activityOperatorLauncher.launch(intent);
        });
        binding.btnProceed.setOnClickListener(view -> {
            if (isConnectionAvailable()) {
                mob = Objects.requireNonNull(binding.tidConsumerNo.getEditText()).getText().toString();
                amt = Objects.requireNonNull(binding.tidAmount.getEditText()).getText().toString();
                if (mob.isEmpty() || mob.trim().length() < 10) {
                    Objects.requireNonNull(binding.tidConsumerNo.getEditText()).requestFocus();
                    Toast.makeText(context, "Enter 10 digit mobile no. !!!", Toast.LENGTH_SHORT).show();
                } else if (opreatorModel == null) {
                    Toast.makeText(context, "Select Operator !!!", Toast.LENGTH_SHORT).show();
                } else if (amt.isEmpty()) {
                    Objects.requireNonNull(binding.tidAmount.getEditText()).requestFocus();
                    Toast.makeText(context, "Enter Amount !!!", Toast.LENGTH_SHORT).show();
                } else {
                    //proceedConfirmationDialog();
                    Intent intent = new Intent(context, BillPayActivity.class);
                    intent.putExtra("CN", mob);
                    intent.putExtra("CN_hint", "Mobile Number");
                    intent.putExtra("Amt", amt);
                    intent.putExtra("AD1", ad1);
                    intent.putExtra("AD2", ad2);
                    intent.putExtra("AD3", ad3);
                    intent.putExtra(getString(R.string.pay_operator_data),Util.getJsonFromModel(operatorData));
                    intent.putExtra(getString(R.string.pay_operator_model), Util.getGson().toJson(opreatorModel));
                     startActivity(intent);
                 }
             } else {
                 Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
             }
         });
    }

    //===========operatorCodeRequest==============
    private void operatorsCodeRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {

            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Operators...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            OperatorsRequest request = new OperatorsRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setService("POSTPAID");

            RetrofitClient.getClient(getApplicationContext())
                    .operators(request).enqueue(new Callback<OperatorsResponse>() {
                @Override
                public void onResponse(@NotNull Call<OperatorsResponse> call, @NotNull retrofit2.Response<OperatorsResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        OperatorsResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                            operatorList = (ArrayList<OperatorModel>) res.getData();
                        } else {
                            L.toastS(context, res.getResponseMessage());
                        }
                    }
                }

                        @Override
                        public void onFailure(@NotNull Call<OperatorsResponse> call, @NotNull Throwable t) {
                            L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                            pd.dismiss();
                        }
                    });
        }
    }

    //===========operatorDetailsRequest==============
    private void operatorDetailsRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Operator Details...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            OperatorsRequest request = new OperatorsRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setOperatorId(opreatorModel.getOpCode());

            RetrofitClient.getClient(getApplicationContext())
                    .getOperatorDetails(request).enqueue(new Callback<OperatorResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<OperatorResponse> call, @NotNull retrofit2.Response<OperatorResponse> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                OperatorResponse res = response.body();
                                if ("0".equals(res.getStatus()) && res.getData()!=null) {
                                        operatorData = res.getData();
                                    isAd1  = false;
                                    isAd2  = false;
                                    isAd3  = false;
                                    Util.hideView(binding.tidAd1);
                                    Util.hideView(binding.tidAd2);
                                    Util.hideView(binding.tidAd3);
                                    if (!"0".equals(operatorData.getAd1DName())) {
                                            isAd1 = true;
                                            Util.showView(binding.tidAd1);
                                            Objects.requireNonNull(binding.tidAd1.getEditText()).setText("");
                                            binding.tidAd1.setHint(operatorData.getAd1DName());
                                        }
                                        if (!"0".equals(operatorData.getAd2DName())) {
                                            isAd2 = true;
                                            Util.showView(binding.tidAd2);
                                            Objects.requireNonNull(binding.tidAd2.getEditText()).setText("");
                                            binding.tidAd2.setHint(operatorData.getAd2DName());

                                        }
                                        if (!"0".equals(operatorData.getAd3DName())) {
                                            isAd3 = true;
                                            Util.showView(binding.tidAd3);
                                            Objects.requireNonNull(binding.tidAd3.getEditText()).setText("");
                                            binding.tidAd3.setHint(operatorData.getAd3DName());
                                        }

                                } else {
                                    L.toastS(context, res.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<OperatorResponse> call, @NotNull Throwable t) {
                            L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                            pd.dismiss();
                        }
                    });
        }
    }

    private void retrieveContactNumber() {
        Dexter.withContext(context)
                .withPermissions(Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            Utility.loge("myPermissions", "areAllPermissionsGranted");
                            getContact();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Utility.loge("myPermissions", "isAnyPermissionPermanentlyDenied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .withErrorListener(dexterError -> {
                    L.toastS(context, "" + dexterError.name());
                }).onSameThread().check();
    }

    public void getContact() {
        String contactNumber = null;
        Cursor c = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
        if (c.moveToFirst()) {
            @SuppressLint("Range") String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
            phones.moveToFirst();
            @SuppressLint("Range") String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (cNumber.equals(null)) {
                Toast.makeText(context, "Invalid Number", Toast.LENGTH_LONG).show();
            } else if (cNumber.equals("") || cNumber.length() < 10) {

            }
            if (cNumber.length() >= 10) {
                Service sc = new Service();
                String correctno = sc.validateMobileNumber(cNumber);

                if (null != correctno) {
                    Objects.requireNonNull(binding.tidConsumerNo.getEditText()).setText(correctno);
                } else {
                    Toast.makeText(context, "Invalid Contact Details", Toast.LENGTH_LONG).show();
                }
            }
            phones.close();
            Log.d("Contact", "Contact Phone Number: " + contactNumber);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }
}
