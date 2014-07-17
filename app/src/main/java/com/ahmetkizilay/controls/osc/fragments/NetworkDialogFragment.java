package com.ahmetkizilay.controls.osc.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.ahmetkizilay.controls.osc.R;

/**
 * Created by ahmetkizilay on 17.07.2014.
 */
public class NetworkDialogFragment extends DialogFragment{
    public static NetworkDialogFragment newInstance(String ipAddress, int port) {
        NetworkDialogFragment frg = new NetworkDialogFragment();
        Bundle args = new Bundle();
        args.putString("ipAddress", ipAddress);
        args.putInt("port", port);
        frg.setArguments(args);
        return frg;
    }

    private String mIpAddress;
    private int mPort;
    private NetworkDialogListener mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        this.mIpAddress = (String) args.get("ipAddress");
        this.mPort = args.getInt("port");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View networkView = inflater.inflate(R.layout.dialog_network_settings, null);
        EditText etNetworkIP = (EditText) networkView.findViewById(R.id.etNetworkIP);
        etNetworkIP.setText(this.mIpAddress);

        EditText etNetworkPort = (EditText) networkView.findViewById(R.id.etNetworkPort);
        etNetworkPort.setText(Integer.toString(mPort));

        final AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
        alert.setView(networkView);
        alert.setTitle("Network Settings");
        alert.setButton(Dialog.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText etNetworkIP = (EditText) alert.findViewById(R.id.etNetworkIP);
                String newIpAddress = etNetworkIP.getText().toString();

                EditText etNetworkPort = (EditText) alert.findViewById(R.id.etNetworkPort);
                int newPort = Integer.parseInt(etNetworkPort.getText().toString());

                if(mCallback != null) {
                    mCallback.onSettingsSaved(newIpAddress, newPort);
                }
            }
        });
        return alert;
    }

    public void setNetworkDialogListener(NetworkDialogListener callback) {
        this.mCallback = callback;
    }

    public interface NetworkDialogListener {
        public void onSettingsSaved(String ipAddress, int port);
    }
}
