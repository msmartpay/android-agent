package agent.msmartpay.in.moneyTransferNew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import agent.msmartpay.in.R;

/**
 * Created by Roran on 12/6/2017.
 */

public class CustomAdaptorClass extends BaseAdapter {

    private Context mContext;
    private ArrayList<BankListModel> bankList;

    public CustomAdaptorClass(Context context, ArrayList<BankListModel> bank) {
        mContext = context;
        bankList = bank;
    }

    @Override
    public int getCount() {
        return bankList.size();
    }

    @Override
    public Object getItem(int i) {
        return bankList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spinner_textview_layout, null);
        }
        else {
            view = convertView;
        }
        TextView custom_spinner = view.findViewById(R.id.custom_spinner);
        custom_spinner.setText(bankList.get(position).getBankName());

        return view;
    }
}
