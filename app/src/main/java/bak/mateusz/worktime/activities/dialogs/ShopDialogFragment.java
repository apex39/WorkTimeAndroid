package bak.mateusz.worktime.activities.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

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
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select shop")
                .setItems(shopsAddresses, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences settings = getActivity().getSharedPreferences("preferences", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("registered_shop", shopsAddresses[which]);
                        editor.apply();
                        getActivity().setTitle(shopsAddresses[which]);
                        Toast.makeText(getActivity().getApplicationContext(),"Shop successfully changed",Toast.LENGTH_LONG).show();
                    }
                });
        return builder.create();
    }

}
