package in.msmartpay.agent.rechargeBillPay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import in.msmartpay.agent.R;
import in.msmartpay.agent.rechargeBillPay.plans.PlanAdapter;
import in.msmartpay.agent.network.model.wallet.PlanModel;
import in.msmartpay.agent.rechargeBillPay.plans.PlansActivity;
import in.msmartpay.agent.utility.BaseFragment;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.PlanAdListener;
import in.msmartpay.agent.utility.Util;

import java.util.ArrayList;
import java.util.List;

public class DynamicFragment extends BaseFragment implements PlanAdListener {

    private ArrayList<PlanModel> planListModels;
    private RecyclerView rv_plans;
    private PlanAdapter planAdapter;


    public DynamicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
            planListModels = Util.getGson().fromJson(getArguments().getString("Array"),new TypeToken<List<PlanModel>>(){}.getType());
        else
            planListModels = new ArrayList<>();
        L.m2("Arrays",Util.getGson().toJson(planListModels));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.plan_frag_dynamic, container, false);
        rv_plans = view.findViewById(R.id.rv_plans);
        rv_plans.setLayoutManager(new LinearLayoutManager(getActivity()));
        planAdapter = new PlanAdapter(getActivity(),planListModels,this);
        rv_plans.setAdapter(planAdapter);
        return  view;
    }

    public static DynamicFragment newInstance(ArrayList<PlanModel> planListModels) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putString("Array", Util.getGson().toJson(planListModels));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClickPlan(String str) {
        Intent intent = new Intent(getActivity(), PlansActivity.class);
        intent.putExtra("price",str);
        getActivity().setResult(Activity.RESULT_OK,intent);
        getActivity().finish();
    }
}