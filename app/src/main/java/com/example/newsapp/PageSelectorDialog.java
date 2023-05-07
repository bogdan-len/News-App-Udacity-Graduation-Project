package com.example.newsapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PageSelectorDialog extends AppCompatDialogFragment {

    private EditText etPageNumber;
    private PageSelectorDialogListener listener;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_page_selector_dialog, null);

        etPageNumber = view.findViewById(R.id.et_page_input);
        etPageNumber.requestFocus();

        builder.setView(view)
                .setTitle("Select the page:")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String pageNumber = etPageNumber.getText().toString();
                        listener.applyPageNumber(pageNumber);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PageSelectorDialogListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() +
                    "PageSelectorDialogListener not implemented");
        }
    }

    public interface PageSelectorDialogListener {
        void applyPageNumber(String pageNumber);
    }
}
