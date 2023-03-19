package in.msmartpayagent.dmrPaySprint.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import in.msmartpayagent.R;
import in.msmartpayagent.network.model.dmr.SenderDetails;
import in.msmartpayagent.network.model.dmr.SenderDetailsResponse;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.Util;

/**
 * Created by Smartkinda on 7/8/2017.
 */

public class DetailsFragment extends Fragment {

    private EditText editMobileNo, editFirstName, edittotalLimit,  editUsedLimit, editAvailableLimit;


    private String agentID, txnKey, SessionID, mobileNumber;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dmr_ps_sender_details_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        agentID = Util.LoadPrefData(getActivity(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getActivity(), Keys.TXN_KEY);
        mobileNumber = Util.LoadPrefData(getActivity(), Keys.SENDER_MOBILE);

        editMobileNo = (EditText) view.findViewById(R.id.id_mobile_number);
        editFirstName = (EditText) view.findViewById(R.id.id_first_name);
        edittotalLimit = (EditText) view.findViewById(R.id.id_total_limit);
        editUsedLimit = (EditText) view.findViewById(R.id.id_used_limit);
        editAvailableLimit = (EditText) view.findViewById(R.id.id_available_limit);

        editMobileNo.setEnabled(false);
        editFirstName.requestFocus();

        SenderDetailsResponse response = Util.getGson().fromJson(Util.LoadPrefData(getActivity(), Keys.SENDER),SenderDetailsResponse.class);
        if (response.getSenderDetails()!=null) {
            SenderDetails details1 = response.getSenderDetails();
            editMobileNo.setText(details1.getSenderId());
            editFirstName.setText(details1.getName());
            edittotalLimit.setText(response.getSenderLimitDetails().getSenderLimit()+"");
            editUsedLimit.setText(response.getSenderLimitDetails().getUsedLimit()+"");
            editAvailableLimit.setText(response.getSenderLimitDetails().getAvailableLimit()+"");

        }


        return view;
    }

}
