package in.msmartpayagent.kyc;

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
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.Util;

public class DocumentTypeAdapter extends RecyclerView.Adapter<DocumentTypeAdapter.ViewHolder> {
    private List<DocumentTypeDataModel> list;
    private Context context;
    private DocumentTypeListener listener;

    public DocumentTypeAdapter(Context context, List<DocumentTypeDataModel> list,DocumentTypeListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kyc_document_type_item, parent, false);
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
        TextView tv_name, tv_kyc_sample_click;
        ImageView iv_img;
        Button btn_upload_kyc_document;
        TextInputLayout til_kyc_document_number;

        public ViewHolder(View view) {
            super(view);
            tv_kyc_sample_click = view.findViewById(R.id.tv_kyc_sample_click);
            tv_name = view.findViewById(R.id.tv_name);
            iv_img = view.findViewById(R.id.iv_img);
            til_kyc_document_number = view.findViewById(R.id.til_kyc_document_number);
            btn_upload_kyc_document = view.findViewById(R.id.btn_upload_kyc_document);
        }

        @SuppressLint("SuspiciousIndentation")
        public void bind(DocumentTypeDataModel model, int position, DocumentTypeListener listener) {
            tv_name.setText(model.getDisplayName());
            if (model.getDocumentType().equalsIgnoreCase("photo")
                    || model.getDocumentType().equalsIgnoreCase("shopphoto1")
                    || model.getDocumentType().equalsIgnoreCase("shopphoto2")) {
                Util.hideView(til_kyc_document_number);
            }
            if (!model.getUploadUrl().isEmpty())
            Picasso.get().load(new File(model.getUploadUrl())).into(iv_img);
            iv_img.setOnClickListener(view2 -> {
                listener.captureImage(model, position);
            });
            Objects.requireNonNull(til_kyc_document_number.getEditText()).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    listener.onDcoNumber(s.toString(), position);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            btn_upload_kyc_document.setOnClickListener(view1 -> {
                listener.onUploadImage(model, position);

            });

            tv_kyc_sample_click.setOnClickListener(view4 -> {
                if (model.getSampleFilePath()!=null && model.getSampleFilePath().contains("http"))
                 confirmationSampleDialog(model.getSampleFilePath());
                else L.toastL(context,"No Sample Available");
            });

        }
    }


    public void confirmationSampleDialog(String path) {
        final Dialog dialog_status = new Dialog(context);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert_sample_image);
        dialog_status.setCancelable(true);
        ImageView statusImage = dialog_status.findViewById(R.id.statusImage);
        Picasso.get().load(path).into(statusImage);
        final Button trans_status = dialog_status.findViewById(R.id.trans_status_button);
        trans_status.setOnClickListener(v -> {
            dialog_status.dismiss();
        });
        dialog_status.show();
    }

    interface DocumentTypeListener {
        void onUploadImage(DocumentTypeDataModel model, int index);

        void captureImage(DocumentTypeDataModel model, int index);

        void onDcoNumber(String docNumber, int index);
    }

}
