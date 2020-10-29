package agent.msmartpay.in.myWallet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.L;

/**
 * Created by Roran on 10/27/2017.
 */

public class StatementAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private ArrayList<TransactionItems> tranListAdd;
    private ArrayList<TransactionItems> tranListAdd2;
    private ItemFilter mFilter=new ItemFilter();

    public  StatementAdapter(Context context, ArrayList<TransactionItems> navItems) {
        mContext = context;
        tranListAdd = navItems;
        tranListAdd2 = navItems;
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
        TextView tv_current_amt = (TextView) view.findViewById(R.id.tv_current_amt);
        tv_current_amt.setText("Cur. Amt. "+"\u20B9"+" "+tranListAdd.get(position).Agent_F_balAmt);
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
                Intent intent = new Intent(mContext, TransactionHistoryReceipt.class);
                intent.putExtra("position", position);
                intent.putExtra("tranList", tranListAdd);
                L.m2("tranListAdd--->", tranListAdd.toString()+"--->"+position);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<TransactionItems> list = tranListAdd2;

            int count = list.size();
            final ArrayList<TransactionItems> nlist = new ArrayList<>(count);

            for (TransactionItems model:list) {
                if(filterString.equalsIgnoreCase("in")){
                    if(model.Action_on_bal_amt.equalsIgnoreCase("Credit"))
                        nlist.add(model);
                }else if(filterString.equalsIgnoreCase("out")){
                    if(model.Action_on_bal_amt.equalsIgnoreCase("Debit"))
                        nlist.add(model);
                }else {
                    nlist.add(model);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tranListAdd = (ArrayList<TransactionItems>) results.values;
            notifyDataSetChanged();
        }

    }

}
