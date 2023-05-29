package in.msmartpay.agent.rechargeBillPay.plans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import in.msmartpay.agent.R;
import in.msmartpay.agent.network.AppMethods;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.PlanRequest;
import in.msmartpay.agent.network.model.PlanResponse;
import in.msmartpay.agent.network.model.wallet.OperatorModel;
import in.msmartpay.agent.network.model.wallet.PlanModel;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;

public class PlansActivity extends BaseActivity {
    private static final int REQUEST_PLAN = 0212;
    private Spinner spinner_oprater, spinner_circle, spinner_mob_dth;
    private ViewPager view_pager;
    private TabLayout tab_layout;
    private PlanPagerAdapter planPagerAdapter;
    private ProgressDialogFragment pd;
    private Context context;
    private String agentID, txn_key = "";
    private ArrayList<OperatorModel> operatorList = null;
    private ArrayList<String> arrayList;
    private ArrayList<ArrayList<PlanModel>> plansList;
    private OperatorModel listModel = null;
    private String opname = null, circle = null, type = null, mobile = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_view_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Plan");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        spinner_mob_dth = findViewById(R.id.spinner_mob_dth);
        //spinner_circle = findViewById(R.id.spinner_circle);
        spinner_oprater = findViewById(R.id.spinner_oprater);
        tab_layout = findViewById(R.id.tab_layout);
        view_pager = findViewById(R.id.view_pager);
        spinner_mob_dth.setVisibility(View.GONE);
        spinner_oprater.setVisibility(View.GONE);
        context = PlansActivity.this;

        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        Intent intent = getIntent();
        if (intent != null) {

            circle=intent.getStringExtra("circle");

            if (intent.getStringExtra("operator") != null)
                opname = intent.getStringExtra("operator");



            if (intent.getStringExtra("mobile") != null)
                mobile = intent.getStringExtra("mobile");

            if (!mobile.equals(""))
                getSupportActionBar().setTitle("View Offer");

            if (intent.getStringExtra("type") != null) {
                type = intent.getStringExtra("type");

                if (type.equalsIgnoreCase("dth")) {
                    //spinner_circle.setVisibility(View.GONE);
                    getPlansViews();
                }
            }
            if(circle!=null && !"".equalsIgnoreCase(circle)){

                getPlansViews();
            }

        } else {
            finish();
        }

        spinner_oprater.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > -1) {
                    listModel = operatorList.get(position);
                    opname = listModel.getOperatorName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*spinner_circle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > -1) {
                    circle = spinner_circle.getSelectedItem().toString();
                    getPlansViews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */

    }

    private void getPlansViews() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Plans...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            PlanRequest request = new PlanRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setOperator(opname);
            request.setClient(AppMethods.CLIENT);
            request.setType(type);
            if ("mobile".equalsIgnoreCase(type))
                request.setCricle(circle);

            RetrofitClient.getClient(getApplicationContext()).getPlans(request)
                    .enqueue(new Callback<PlanResponse>() {
                        @Override
                        public void onResponse(Call<PlanResponse> call, retrofit2.Response<PlanResponse> response) {
                            try {
                                pd.dismiss();
                                if (response.isSuccessful() && response.body() != null) {
                                    PlanResponse res = response.body();
                                    if (res.getStatus() == 1) {
                                        if (arrayList != null)
                                            arrayList.clear();
                                        else
                                            arrayList = new ArrayList<>();
                                        if (plansList != null)
                                            plansList.clear();
                                        else
                                            plansList = new ArrayList<>();
                                        try {
                                            if (res.getRecords() instanceof JsonArray) {
                                                // It's an array
                                                JSONArray recordsJsonArray = new JSONArray(res.getRecords().toString());
                                                ArrayList<PlanModel> modelArrayList = new ArrayList<>();
                                                for (int i = 0; i < recordsJsonArray.length(); i++) {
                                                    JSONObject object = recordsJsonArray.getJSONObject(i);
                                                    PlanModel model = new PlanModel();
                                                    model.setRs(object.getString("rs"));
                                                    model.setDesc(object.getString("desc"));
                                                    if (!object.getString("desc").equalsIgnoreCase("Plan Not Available"))
                                                        modelArrayList.add(model);
                                                }
                                                if (modelArrayList.size() > 0) {
                                                    arrayList.add("Offer");
                                                    plansList.add(modelArrayList);
                                                }
                                            } else if (res.getRecords() instanceof JsonObject) {
                                                // It's an object
                                                JSONObject recordsObject = new JSONObject(res.getRecords().toString());

                                                Iterator keys = recordsObject.keys();

                                                while (keys.hasNext()) {
                                                    try {
                                                        //Your dynamic key
                                                        String dynamicKey = (String) keys.next();
                                                        if (type.equalsIgnoreCase("mobile"))
                                                            arrayList.add(dynamicKey);
                                                        //Your json object for that dynamic key
                                                        JSONArray tapArray = recordsObject.getJSONArray(dynamicKey);

                                                        if (type.equalsIgnoreCase("mobile")) {
                                                            ArrayList<PlanModel> modelArrayList = new ArrayList<>();
                                                            for (int i = 0; i < tapArray.length(); i++) {
                                                                JSONObject object = tapArray.getJSONObject(i);
                                                                PlanModel model = new PlanModel();
                                                                model.setRs(object.getString("rs"));
                                                                model.setDesc(object.getString("desc"));
                                                                model.setLast_update(object.getString("last_update"));
                                                                modelArrayList.add(model);
                                                            }
                                                            plansList.add(modelArrayList);
                                                        } else if (type.equalsIgnoreCase("dth")) {
                                                            HashMap<String, ArrayList<PlanModel>> map = new HashMap<>();
                                                            for (int i = 0; i < tapArray.length(); i++) {
                                                                JSONObject object = tapArray.getJSONObject(i);
                                                                JSONObject object1 = object.getJSONObject("rs");
                                                                Iterator keys1 = object1.keys();
                                                                String dynamicKey1 = "";
                                                                while (keys1.hasNext()) {
                                                                    dynamicKey1 = (String) keys1.next();
                                                                    PlanModel model = new PlanModel();
                                                                    model.setRs(object1.getString(dynamicKey1));
                                                                    model.setDesc(dynamicKey1 + "  " + object.getString("desc"));
                                                                    model.setLast_update(object.getString("last_update"));

                                                                    ArrayList<PlanModel> modelArrayList = new ArrayList<>();
                                                                    if (map.get(dynamicKey1) == null) {
                                                                        modelArrayList.add(model);
                                                                        map.put(dynamicKey1, modelArrayList);
                                                                    } else {
                                                                        modelArrayList = (ArrayList<PlanModel>) map.get(dynamicKey1);
                                                                        modelArrayList.add(model);
                                                                        map.remove(dynamicKey1);
                                                                        map.put(dynamicKey1, modelArrayList);
                                                                    }

                                                                }
                                                            }
                                                            for (Map.Entry<String, ArrayList<PlanModel>> entry : map.entrySet()) {
                                                                arrayList.add(entry.getKey());
                                                                plansList.add((ArrayList<PlanModel>) entry.getValue());
                                                            }
                                                        }
                                                    } catch (JSONException e) {
                                                        view_pager.setVisibility(View.GONE);
                                                        tab_layout.setVisibility(View.GONE);
                                                        Toast.makeText(context, "No Plan Available", Toast.LENGTH_SHORT).show();
                                                        if (!type.equalsIgnoreCase("mobile"))
                                                            finish();
                                                    }
                                                }
                                            }
                                        } catch (JSONException e) {
                                        }
                                        // JSONObject jsonObject = data.getJSONObject("records");

                                        if (arrayList.size() > 0 && plansList.size() > 0) {
                                            view_pager.setVisibility(View.VISIBLE);
                                            tab_layout.setVisibility(View.VISIBLE);
                                            planPagerAdapter = new PlanPagerAdapter(getSupportFragmentManager(), arrayList, plansList);
                                            view_pager.setAdapter(planPagerAdapter);
                                            tab_layout.setupWithViewPager(view_pager);
                                        } else {
                                            view_pager.setVisibility(View.GONE);
                                            tab_layout.setVisibility(View.GONE);
                                            Toast.makeText(context, "No Plan Available", Toast.LENGTH_SHORT).show();
                                            if (!type.equalsIgnoreCase("mobile"))
                                                finish();
                                        }
                                    } else {
                                        L.toastS(getApplicationContext(), "No Plans Available");
                                        finish();
                                    }
                                } else {
                                    L.toastS(getApplicationContext(), "No Response");
                                    finish();
                                }
                            }catch (Exception e){}
                        }

                        @Override
                        public void onFailure(Call<PlanResponse> call, Throwable t) {
                            try {
                                pd.dismiss();
                                L.toastS(getApplicationContext(), "Error " + t.getMessage());
                                finish();
                            }catch (Exception e){}
                        }
                    });
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
