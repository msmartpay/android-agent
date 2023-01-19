package in.msmartpay.agent.kyc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.msmartpay.agent.R;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {
    private List<DocumentDataResponse> list;
    private Context context;


    public DocumentAdapter(List<DocumentDataResponse> list,Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kyc_document_item, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.bind(list.get(position),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_doc_type;
        TextView tv_doc_name;
        TextView tv_doc_status;
        TextView tv_doc_no;
        TextView tv_doc_remark;
        ImageView iv_download;
        public ViewHolder(View view) {
            super(view);
            tv_doc_name = view.findViewById(R.id.tv_doc_name);
            tv_doc_type = view.findViewById(R.id.tv_doc_type);
            tv_doc_status = view.findViewById(R.id.tv_doc_status);
            iv_download = view.findViewById(R.id.iv_kyc_download);
            tv_doc_no=view.findViewById(R.id.tv_doc_no);
            tv_doc_remark=view.findViewById(R.id.tv_doc_remark);

        }

        public void bind(DocumentDataResponse data, int position) {
            tv_doc_name.setText(data.getDocument_type_id());
            tv_doc_status.setText(data.getDocument_status());
            tv_doc_type.setText(data.getDocument_type());
            tv_doc_no.setText(data.getDocument_no());
            tv_doc_remark.setText(data.getRemark());
            iv_download.setOnClickListener(view -> {
                DocumentDataResponse res = list.get(position);
                Uri uri = Uri.parse(res.getDocument_file_name());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
            });
        }
    }

}
