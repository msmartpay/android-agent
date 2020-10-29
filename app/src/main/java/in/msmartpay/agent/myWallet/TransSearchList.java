package in.msmartpay.agent.myWallet;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;

public class TransSearchList extends BaseActivity {
    private ListView transactionList;
    private String agent_id = "", agent_id_full = "";
    private SharedPreferences sharedPreferences = null;
    private ProgressDialog pd;
    private Context context = null;
    public ArrayList<TransactionItems> tranList = new ArrayList<TransactionItems>();
    private JSONObject data = null;
    private String url;
    String connesctionNo = null, date = "";
    private String SearchTransUrl= HttpURL.SEARCH_TRAIN;
    private String txn_key = "", agentID;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_search_list);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction History");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = TransSearchList.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        agent_id = sharedPreferences.getString("agent_id", null);
        agentID = sharedPreferences.getString("agentonlyid", null);
        txn_key = sharedPreferences.getString("txn-key", null);
        agent_id_full = sharedPreferences.getString("agent_id_full", null);
        connesctionNo = getIntent().getExtras().getString("connNo");
        date = getIntent().getExtras().getString("date");

        transactionList = (ListView) findViewById(R.id.id_transactionList);

        pd = new ProgressDialog(context);
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(true);
        try{
            JSONObject jsonObjectReq = new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txn_key)
                    .put("connection_no", connesctionNo)
                    .put("date", date);

            L.m2("url-search", SearchTransUrl);
            L.m2("Request--search",jsonObjectReq.toString());
            JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.POST, SearchTransUrl, jsonObjectReq, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject data) {

                    pd.dismiss();
                    L.m2("Response--search", data.toString());
                    try {
                        if (data.toString() == null) {
                            Toast.makeText(context, "No Transaction Available! ", Toast.LENGTH_LONG).show();
                            finish();
                        } else if (data.get("response-code") != null && (data.get("response-code").equals("1")||data.get("response-code").equals("2"))) {
                            Toast.makeText(context, (String) data.get("response-message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            final JSONArray parentArray = (JSONArray) data.get("dataJSon");
                            if (parentArray.length() > 0) {
                                for (int i = 0; i < parentArray.length(); i++) {
                                    final JSONObject obj = (JSONObject) parentArray.get(i);
                                    TransactionItems item = new TransactionItems(
                                            obj.getString("Id_No")+ "",
                                            obj.getString("tran_id")+ "",
                                            obj.getString("mobile_operator")+ "",
                                            obj.getString("mobile_number") + "",
                                            obj.getString("service")+ "",
                                            obj.getString("Action_on_bal_amt")+ "",
                                            obj.getString("tran_status")+ "",
                                            obj.getString("net_amout")+ "",
                                            obj.getString("DeductedAmt")+ "",
                                            obj.getString("dot") + "",
                                            obj.getString("tot") + "",
                                            obj.getString("Agent_balAmt_b_Ded") + "",
                                            obj.getString("Agent_F_balAmt") + "");
                                    tranList.add(item);
                                }
                                transactionList.setAdapter(new StatementAdapter(context, tranList));
                            }else {
                                Toast.makeText(context, "No Transaction Available! ", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(context, MainActivity.class));
                                finish();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, "MainActivity onResume() "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
/*

    //==================StatementAdapter=================================

    public class StatementAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<TransactionItems> tranListAdd;

        public  TransSearchAdapter(Context context, ArrayList<TransactionItems> navItems) {
            mContext = context;
            tranListAdd = navItems;
        }

        @Override
        public int getCount() {
            return tranListAdd.size();
        }

        @Override
        public Object getItem(int position) {
            return tranListAdd.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.wallet_history_items, null);
            }
            else {
                view = convertView;
            }
            TextView ac = (TextView) view.findViewById(R.id.ac);
            TextView amt = (TextView) view.findViewById(R.id.amt);
            TextView status = (TextView) view.findViewById(R.id.status);
            TextView txnId = (TextView) view.findViewById(R.id.txn_id);
            TextView date = (TextView) view.findViewById(R.id.date);
            TextView cd = (TextView) view.findViewById(R.id.cd);

            ac.setText( tranListAdd.get(position).service +"\n"+ tranListAdd.get(position).mobile_number +" "+tranListAdd.get(position).mobile_operator);
            status.setText(tranListAdd.get(position).tran_status);
            amt.setText(tranListAdd.get(position).net_amout+"");
            txnId.setText(tranListAdd.get(position).tran_id);
            date.setText(tranListAdd.get(position).dot+"\n"+tranListAdd.get(position).tot);
            String st=tranListAdd.get(position).tran_status;
            String aType=tranListAdd.get(position).Action_on_bal_amt;
            if(aType.equalsIgnoreCase("credit"))
                cd.setText("+");
            if(aType.equalsIgnoreCase("debit"))
                cd.setText("-");

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TransactionHistoryReceipt.class);
                    intent.putExtra("position", position);
                    intent.putExtra("tranList", tranListAdd);
                    L.m2("tranListAdd--->", tranListAdd.toString()+"--->"+position);
                    startActivity(intent);
                }
            });



            return view;
        }
    }

    //=======================================================================
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }
}
