package com.aepssdkssz.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.aepssdkssz.R;


public class DialogProgressFragment extends androidx.fragment.app.DialogFragment {
    public static final String ARG_TITLE = "Titel";
    public static final String ARG_MESSAGE = "Message";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading_view, container);
        TextView tvTitle = view.findViewById(R.id.tvProgress_title);
        TextView tvMessage = view.findViewById(R.id.tvProgress_message);
        String  title = requireArguments().getString(ARG_TITLE);
        String message = requireArguments().getString(ARG_MESSAGE);
        tvTitle.setText(title);
        tvMessage.setText(message);
        return view;

    }

    public static DialogProgressFragment newInstance(String title, String message) {
        DialogProgressFragment fragment = new DialogProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    public static void showDialog(DialogProgressFragment progressDialogFragment, FragmentManager manager){
        progressDialogFragment.show(manager,"Show Progress");
    }

    public static void hideDialog(DialogProgressFragment progressDialogProgressFragment){
        progressDialogProgressFragment.dismiss();
    }
}