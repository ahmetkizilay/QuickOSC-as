package com.ahmetkizilay.controls.osc.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.ahmetkizilay.controls.osc.R;

/**
 * A DialogFragment class to display the AboutMe section of the app.
 * Defines an interface for two callback methods for donate and rate buttons clicked.
 * Created by ahmetkizilay on 17.07.2014.
 */
public class AboutMeDialogFragment extends DialogFragment {
    public AboutMeDialogListener mCallback;

    public static AboutMeDialogFragment newInstance() {
        AboutMeDialogFragment frg = new AboutMeDialogFragment();
        Bundle args = new Bundle();

        frg.setArguments(args);
        return frg;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflator = LayoutInflater.from(getActivity());
        final View aboutView = inflator.inflate(R.layout.dialog_about, null);

        // get version name
        PackageManager pm = getActivity().getPackageManager();
        String versionName = "1.0.0";
        try {
            versionName = pm.getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (Exception e) {}

        String title = getResources().getString(R.string.app_name) + " - v" + versionName;

        AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
        alert.setView(aboutView);
        alert.setCancelable(true);
        alert.setButton(Dialog.BUTTON_NEUTRAL, "DONATE", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if(mCallback != null) {
                    mCallback.onDonateSelected();
                }
            }
        });

        alert.setButton(Dialog.BUTTON_POSITIVE, "RATE ME", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if(mCallback != null) {
                    mCallback.onRateMeSelected();
                }
            }
        });

        alert.setTitle(title);
        alert.setIcon(R.drawable.qosc);
        return alert;
    }

    public void setAboutMeDialogListener(AboutMeDialogListener callback) {
        this.mCallback = callback;
    }

    public interface AboutMeDialogListener {
        public void onRateMeSelected();
        public void onDonateSelected();
    }

}
