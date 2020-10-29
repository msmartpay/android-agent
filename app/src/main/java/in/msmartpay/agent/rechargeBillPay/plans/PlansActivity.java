package in.msmartpay.agent.rechargeBillPay.plans;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import in.msmartpay.agent.R;
import in.msmartpay.agent.rechargeBillPay.CustomOperatorClass;
import in.msmartpay.agent.rechargeBillPay.OperatorListModel;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlansActivity extends BaseActivity {
    private static final int REQUEST_PLAN = 0212;
    private Spinner spinner_oprater, spinner_circle, spinner_mob_dth;
    private ViewPager view_pager;
    private TabLayout tab_layout;
    private PlanPagerAdapter planPagerAdapter;
    private ProgressDialog pd;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key = "";
    private ArrayList<OperatorListModel> OperatorList = null;
    private ArrayList<String> arrayList;
    private ArrayList<ArrayList<PlanListModel>> plansList;
    private CustomOperatorClass operatorAdaptor;
    private OperatorListModel listModel = null;
    private String opname = null, circle = null, type = null, mobile = null;
    private String plan_url = HttpURL.PLAN_VIEW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_view_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Plan");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        spinner_mob_dth = findViewById(R.id.spinner_mob_dth);
        spinner_circle = findViewById(R.id.spinner_circle);
        spinner_oprater = findViewById(R.id.spinner_oprater);
        tab_layout = findViewById(R.id.tab_layout);
        view_pager = findViewById(R.id.view_pager);
        spinner_mob_dth.setVisibility(View.GONE);
        spinner_oprater.setVisibility(View.GONE);
        context = PlansActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txn_key = sharedPreferences.getString("txn-key", null);
        agentID = sharedPreferences.getString("agentonlyid", null);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("operator") != null)
                opname = intent.getStringExtra("operator");

            if (intent.getStringExtra("mobile") != null)
                mobile = intent.getStringExtra("mobile");

            if (!mobile.equals(""))
                getSupportActionBar().setTitle("View Offer");

            if (intent.getStringExtra("type") != null) {
                type = intent.getStringExtra("type");

                if (type.equalsIgnoreCase("dth")) {
                    spinner_circle.setVisibility(View.GONE);
                    getPlansViews();
                }
            }
        } else {
            finish();
        }

        spinner_oprater.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > -1) {
                    listModel = OperatorList.get(position);
                    opname = listModel.getOperatorName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_circle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    }

    private void getPlansViews() {
        if (isOnline()) {
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();

            try {
                JSONObject jsonObjectReq = new JSONObject();
                jsonObjectReq.put("agent_id", agentID);
                jsonObjectReq.put("type", type);
                if (type.equalsIgnoreCase("mobile"))
                    jsonObjectReq.put("cricle", circle);
                jsonObjectReq.put("operator", opname);
                jsonObjectReq.put("mobile", mobile);
                jsonObjectReq.put("client", HttpURL.CLIENT);
                L.m2("url-operators", plan_url);
                L.m2("Request--Plans", jsonObjectReq.toString());
                JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, plan_url, jsonObjectReq,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject data) {
                                pd.dismiss();
                                L.m2("Response--Plans", data.toString());
                                try {
                                    if (arrayList != null)
                                        arrayList.clear();
                                    else
                                        arrayList = new ArrayList<>();
                                    if (plansList != null)
                                        plansList.clear();
                                    else
                                        plansList = new ArrayList<>();
                                    if (data.getString("status") != null && (data.getString("status").equals("1"))) {

                                        Object records = data.get("records");
                                        // JSONObject jsonObject = data.getJSONObject("records");
                                        if (records instanceof JSONArray) {
                                            // It's an array
                                            JSONArray recordsJsonArray = (JSONArray) records;
                                            ArrayList<PlanListModel> modelArrayList = new ArrayList<>();
                                            for (int i = 0; i < recordsJsonArray.length(); i++) {
                                                JSONObject object = recordsJsonArray.getJSONObject(i);
                                                PlanListModel model = new PlanListModel();
                                                model.setRs(object.getString("rs"));
                                                model.setDesc(object.getString("desc"));
                                                if (!object.getString("desc").equalsIgnoreCase("Plan Not Available"))
                                                    modelArrayList.add(model);
                                            }
                                            if (modelArrayList.size() > 0) {
                                                arrayList.add("Offer");
                                                plansList.add(modelArrayList);
                                            }
                                        } else if (records instanceof JSONObject) {
                                            // It's an object
                                            JSONObject recordsObject = (JSONObject) records;

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
                                                        ArrayList<PlanListModel> modelArrayList = new ArrayList<>();
                                                        for (int i = 0; i < tapArray.length(); i++) {
                                                            JSONObject object = tapArray.getJSONObject(i);
                                                            PlanListModel model = new PlanListModel();
                                                            model.setRs(object.getString("rs"));
                                                            model.setDesc(object.getString("desc"));
                                                            model.setLast_update(object.getString("last_update"));
                                                            modelArrayList.add(model);
                                                        }
                                                        plansList.add(modelArrayList);
                                                    } else if (type.equalsIgnoreCase("dth")) {
                                                        HashMap<String, ArrayList<PlanListModel>> map = new HashMap<>();
                                                        for (int i = 0; i < tapArray.length(); i++) {
                                                            JSONObject object = tapArray.getJSONObject(i);
                                                            JSONObject object1 = object.getJSONObject("rs");
                                                            Iterator keys1 = object1.keys();
                                                            String dynamicKey1 = "";
                                                            while (keys1.hasNext()) {
                                                                dynamicKey1 = (String) keys1.next();
                                                                PlanListModel model = new PlanListModel();
                                                                model.setRs(object1.getString(dynamicKey1));
                                                                model.setDesc(dynamicKey1 + "  " + object.getString("desc"));
                                                                model.setLast_update(object.getString("last_update"));

                                                                ArrayList<PlanListModel> modelArrayList = new ArrayList<>();
                                                                if (map.get(dynamicKey1) == null) {
                                                                    modelArrayList.add(model);
                                                                    map.put(dynamicKey1, modelArrayList);
                                                                } else {
                                                                    modelArrayList = (ArrayList<PlanListModel>) map.get(dynamicKey1);
                                                                    modelArrayList.add(model);
                                                                    map.remove(dynamicKey1);
                                                                    map.put(dynamicKey1, modelArrayList);
                                                                }

                                                            }
                                                        }
                                                        for (Map.Entry<String, ArrayList<PlanListModel>> entry : map.entrySet()) {
                                                            arrayList.add(entry.getKey());
                                                            plansList.add((ArrayList<PlanListModel>) entry.getValue());
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
                                    }
                                } catch (JSONException e) {
                                    view_pager.setVisibility(View.GONE);
                                    tab_layout.setVisibility(View.GONE);
                                    Toast.makeText(context, "No Plan Available", Toast.LENGTH_SHORT).show();
                                    if (!type.equalsIgnoreCase("mobile"))
                                        finish();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Toast.makeText(context, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                        L.m2("Server Error", error.toString());
                    }
                });
                getSocketTimeOut(jsonrequest);
                Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
            } catch (Exception exp) {
                pd.dismiss();
                exp.printStackTrace();
            }
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
