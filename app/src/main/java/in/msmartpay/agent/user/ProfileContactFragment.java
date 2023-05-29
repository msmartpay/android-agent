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
public class ProfileContactFragment extends BaseFragment {


    private TextView tv_mobile, tv_email, tv_dob, tv_gender,tv_agency,tv_pan;
    private String agentID="",txn_key="";
    private Context context;



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_contact_details, container, false);

        context = getActivity();
        agentID = Util.LoadPrefData(getActivity(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getActivity(), Keys.TXN_KEY);

        tv_mobile = view.findViewById(R.id.tv_profile_mobile);
        tv_email = view.findViewById(R.id.tv_profile_email);
        tv_dob =  view.findViewById(R.id.tv_profile_db);
        tv_gender =view.findViewById(R.id.tv_profile_gender);
        tv_agency = view.findViewById(R.id.tv_profile_agency);
        tv_pan = view.findViewById(R.id.tv_profile_pan);


        tv_mobile.setText(Util.LoadPrefData(getActivity(), Keys.AGENT_MOB));
        tv_email.setText(Util.LoadPrefData(getActivity(), Keys.AGENT_EMAIL));
        tv_dob.setText(Util.LoadPrefData(getActivity(), Keys.AGENT_DOB));
        tv_agency.setText(Util.LoadPrefData(getActivity(),Keys.AGENT_COMPANY));
        tv_gender.setText(Util.LoadPrefData(getActivity(),Keys.AGENT_GENDER));
        tv_pan.setText(Util.LoadPrefData(getActivity(),Keys.AGENT_PAN));

        return view;
    }

}
