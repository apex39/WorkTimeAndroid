package bak.mateusz.worktime.activities.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.List;

import bak.mateusz.worktime.R;
import bak.mateusz.worktime.models.ShopsResponse;

/**
 * Created by phpstorm on 12.02.17.
 */

public class ShopDialogFragment extends DialogFragment {

    String[] shopsAddresses;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        shopsAddresses=getArguments().getStringArray("shops_addresses");

        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select shop")
                .setItems(shopsAddresses, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });
        return builder.create();
    }
}
