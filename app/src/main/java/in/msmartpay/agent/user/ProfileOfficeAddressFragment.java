package in.msmartpay.agent.user;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseFragment;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileOfficeAddressFragment extends BaseFragment {

    private String agentID,  txn_key = "";
    private Context context;
    TextView tv_profile_state,tv_profile_address1,tv_profile_city,
            tv_profile_district,tv_profile_pincode;



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_office_address_details, container, false);

        context = getActivity();
        agentID = Util.LoadPrefData(getActivity(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getActivity(), Keys.TXN_KEY);

        tv_profile_address1 = view.findViewById(R.id.tv_profile_off_address1);
        tv_profile_city =  view.findViewById(R.id.tv_profile_off_city);
        tv_profile_district =view.findViewById(R.id.tv_profile_off_district);
        tv_profile_pincode = view.findViewById(R.id.tv_profile_off_pincode);
        tv_profile_state = view.findViewById(R.id.tv_profile_off_state);

        tv_profile_address1.setText(Util.LoadPrefData(getActivity(),Keys.AGENT_OFF_ADD));
        tv_profile_city.setText(Util.LoadPrefData(getActivity(),Keys.AGENT_OFF_CITY));
        tv_profile_district.setText(Util.LoadPrefData(getActivity(),Keys.AGENT_OFF_DISTRICT));
        tv_profile_pincode.setText(Util.LoadPrefData(getActivity(),Keys.AGENT_OFFICE_PINCODE));
        tv_profile_state.setText(Util.LoadPrefData(getActivity(),Keys.AGENT_OFF_STATE));

        return view;
    }


}
