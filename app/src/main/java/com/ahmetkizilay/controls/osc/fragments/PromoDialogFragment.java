package com.ahmetkizilay.controls.osc.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ahmetkizilay.controls.osc.R;

/**
 * A general purpose dialog class intended to be used in promotional alerts where,
 * users can chose not to see the same alert again.
 *
 * The checkbox status is sent with the onDialogClosed callback method, so that developers
 * can figure out whether or not the promo should be displayed again.
 * Created by ahmetkizilay on 16.07.2014
 */
public class PromoDialogFragment extends DialogFragment{
    private DialogClosedListener mCallback;

    public static PromoDialogFragment newInstance(int iconId, String title, String message, String buttonLabel) {
        PromoDialogFragment frg = new PromoDialogFragment();
        Bundle args = new Bundle();
        args.putInt("icon", iconId);
        args.putString("title", title);
        args.putString("message", message);
        args.putString("buttonLabel", buttonLabel);
        frg.setArguments(args);
        return frg;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        int iconId = args.getInt("icon");
        String title = (String) args.get("title");
        String message = (String) args.get("message");
        String buttonLabel = (String) args.get("buttonLabel");

        final View view = getActivity().getLayoutInflater().inflate(R.layout.androsc_promo, null);
        final CheckBox cbNeverShowAgain = (CheckBox) view.findViewById(R.id.cbNeverShow);
        TextView twPromoText = (TextView) view.findViewById(R.id.tw_promoText);
        twPromoText.setText(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true)
                .setIcon(iconId)
                .setTitle(title)
                .setNeutralButton(buttonLabel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallback != null) {
                            mCallback.onPromoRequested();
                        }
                    }
                })
                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallback != null) {
                            mCallback.onDialogClosed(cbNeverShowAgain.isChecked());
                        }
                    }
                }).setView(view);

        return builder.create();
    }

    public void setDialogClosedListener(DialogClosedListener callback) {
        this.mCallback = callback;
    }

    public interface DialogClosedListener {
        public void onPromoRequested();
        public void onDialogClosed(boolean neverOpen);
    }
}
