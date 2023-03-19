package in.msmartpayagent.audit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Objects;

import in.msmartpayagent.R;
import in.msmartpayagent.kyc.DocumentTypeDataModel;
import in.msmartpayagent.network.model.audit.AuditRequest;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.Util;

public class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.ViewHolder> {
    private List<AuditRequest> list;
    private Context context;
    private AuditListener listener;

    public AuditAdapter(Context context, List<AuditRequest> list, AuditListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audit_request_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.bind(list.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id, tv_date,tv_date_approve,tv_view,tv_status,tv_remark;


        public ViewHolder(View view) {
            super(view);
            tv_id = view.findViewById(R.id.tv_id);
            tv_date = view.findViewById(R.id.tv_date);
            tv_date_approve = view.findViewById(R.id.tv_approve_date);
            tv_view = view.findViewById(R.id.tv_view);
            tv_status = view.findViewById(R.id.tv_status);
            tv_remark = view.findViewById(R.id.tv_remark);
        }

        public void bind(AuditRequest model, int position, AuditListener listener) {
            tv_id.setText(model.getAuditId());
            tv_date.setText(model.getRequestDate()+" "+model.getRequestTime());
            tv_date_approve.setText(model.getApprovedDate());
            tv_status.setText(model.getStatus());
            tv_remark.setText(model.getRemarks());
            tv_view.setOnClickListener(view2 -> {
                listener.viewAuditTxn(model, position);
            });
        }
    }

    interface AuditListener {
        void viewAuditTxn(AuditRequest model, int index);
    }

}
