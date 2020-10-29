package agent.msmartpay.in.myWallet;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.BaseFragment;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceHistoryFragment extends BaseFragment {

    private Communicator2 comm;
    private String agentID, txn_key = "";
    private SharedPreferences sharedPreferences;
    private Context context;
    private ProgressDialog pd;
    private ListView brlist;
    private TextView nodata;
    private String bal_history_url = HttpURL.WALLET_BELL_REQ_DETAIL;
    ArrayList<BalHistoryModel> balHistoryArray = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        comm = (Communicator2) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_balance_history, container, false);

        context = getActivity();
        sharedPreferences = context.getSharedPreferences("myPrefs",MODE_PRIVATE);
        agentID = sharedPreferences.getString("agentonlyid", null);
        txn_key = sharedPreferences.getString("txn-key", null);

        brlist = (ListView) view.findViewById(R.id.brlist);
        nodata = (TextView) view.findViewById(R.id.nodata);

        balanceHistoryFragmentRequest();

    return view;
    }

    private void balanceHistoryFragmentRequest() {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            try{
                JSONObject jsonObjectReq = new JSONObject()
                        .put("agent_id", agentID)
                        .put("txn_key", txn_key);

                L.m2("url-balHistory", bal_history_url);
                L.m2("Request--balHistory",jsonObjectReq.toString());
                JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.POST, bal_history_url, jsonObjectReq, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {

                        pd.dismiss();
                        L.m2("Response--balHistory", data.toString());
                        try {
                            if ((data.get("response-code") != null && data.get("response-code").equals("0"))) {
                                nodata.setVisibility(View.GONE);
                                brlist.setVisibility(View.VISIBLE);

                                JSONArray parentArray = data.getJSONArray("data");
                                for (int i = 0; i < parentArray.length(); i++) {
                                    JSONObject object = (JSONObject) parentArray.get(i);
                                    BalHistoryModel historyModel = new BalHistoryModel();
                                    historyModel.setDateBal(object.getString("date"));
                                    historyModel.setTimeBal(object.getString("time"));
                                    historyModel.setModeBal(object.getString("mode"));
                                    historyModel.setAmountBal(object.getString("amount"));
                                    historyModel.setStatusBal(object.getString("status"));
                                    historyModel.setRefIdBal(object.getString("refId"));
                                    balHistoryArray.add(historyModel);

                                    brlist.setAdapter(new BalHistoryAdaptor(context, balHistoryArray));
                                }
                            } else{
                                brlist.setVisibility(View.GONE);
                                nodata.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                    }
                });
                BaseActivity.getSocketTimeOut(objectRequest);
                Mysingleton.getInstance(context).addToRequsetque(objectRequest);
            }

            catch (Exception e){
                e.printStackTrace();
            }
    }

    //===============BalHistoryAdaptor==========================

    public class BalHistoryAdaptor extends BaseAdapter {
        private Context context;
        private ArrayList<BalHistoryModel> balHistoryArrayData = new ArrayList<>();

        public BalHistoryAdaptor(Context context, ArrayList<BalHistoryModel> balHistoryArray) {

            this.context = context;
            balHistoryArrayData = balHistoryArray;

        }

        @Override
        public int getCount() {
            return balHistoryArrayData.size();
        }

        @Override
        public Object getItem(int position) {
            return balHistoryArrayData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder {
            TextView date, time, status, mode, refid, amount;

            public ViewHolder(View v) {
                date = (TextView) v.findViewById(R.id.cstmdate);
                time = (TextView) v.findViewById(R.id.cstmtime);
                mode = (TextView) v.findViewById(R.id.cstmmode);
                refid = (TextView) v.findViewById(R.id.cstmrefid);
                amount = (TextView) v.findViewById(R.id.cstmamount);
                status = (TextView) v.findViewById(R.id.cstmstatus);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View row = convertView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.balance_history_items, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            BalHistoryModel historyModel = new BalHistoryModel();
            historyModel = balHistoryArrayData.get(position);
            holder.status.setText(historyModel.getStatusBal());
            holder.mode.setText("Remark : "+ historyModel.getModeBal());
            holder.time.setText(historyModel.getTimeBal());
            holder.date.setText(historyModel.getDateBal());
            holder.refid.setText(historyModel.getRefIdBal());
            holder.amount.setText(historyModel.getAmountBal());

            return row;
        }
    }

    //==========================================================

    public interface Communicator2 {

    }

}
