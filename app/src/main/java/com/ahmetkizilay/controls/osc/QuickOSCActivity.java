package com.ahmetkizilay.controls.osc;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.ahmetkizilay.controls.osc.fragments.AboutMeDialogFragment;
import com.ahmetkizilay.controls.osc.fragments.NetworkDialogFragment;
import com.ahmetkizilay.controls.osc.fragments.PromoDialogFragment;
import com.ahmetkizilay.controls.osc.fragments.WifiSettingsDialogFragment;
import com.ahmetkizilay.modules.donations.PaymentDialogFragment;
import com.ahmetkizilay.modules.donations.ThankYouDialogFragment;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/***
 * Entry point for the application.
 * 
 * Each view (Button, Toggle and SeekBar) has a corresponding Wrapper object which takes care of storing 
 * OSC message strings, managing listeners and passing OSC messages to the parent activity class to be sent to the host.
 * 
 * 
 * @author ahmetkizilay
 *
 */
public class QuickOSCActivity extends FragmentActivity {
	private final static int BUTTON_OSC_INTENT_RESULT = 1;
    private final static int TOGGLE_OSC_INTENT_RESULT = 2;
    private final static int SEEKBAR_OSC_INTENT_RESULT = 3;

    private final static String PROMO_DIALOG = "dlg-promo";
    private final static String DONATE_DIALOG = "dlg-donate";
    private final static String THANKS_DIALOG = "dlg-thanks";
    private final static String NETWORK_DIALOG = "dlg-network";
    private final static String ABOUT_DIALOG = "dlg-about";
    private final static String WIFI_ALERT_DIALOG = "dlg-wifi";

    private final static String NETWORK_SETTINGS_FILE = "qosc_network.cfg";
    private final static String OSC_SETTINGS_FILE = "qosc_osc.cfg";
    private final static String PREF_FILE = "qosc_pref";
    private final static String PROMO_SHOWN = "promo_shown";
            
    private List<ButtonOSCWrapper> buttonOSCWrapperList = new ArrayList<ButtonOSCWrapper>();
    private List<ToggleOSCWrapper> toggleOSCWrapperList = new ArrayList<ToggleOSCWrapper>();
    private List<SeekBarOSCWrapper> seekBarOSCWrapperList = new ArrayList<SeekBarOSCWrapper>();
    private Hashtable<String, String> oscSettingsHashtable = new Hashtable<String, String>();
    
    private boolean editMode = false;

    private String ipAddress = "127.0.0.1";
    private int port = 8000;
    private OSCPortOut oscPortOut = null;

    private boolean mListenIncoming = true;
    private int inPort = 8090;
    private OSCPortIn oscPortIn;

    private OSCListener btnListener;
    private OSCListener toggleListener;
    private OSCListener seekBarListener;
    private OSCListener saveListener;

	TextView debugTextView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		restoreOSCSettingsFromFile();

        debugTextView = (TextView) findViewById(R.id.textView1);
        
        Button button1 = (Button) findViewById(R.id.button1);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(0,
                oscSettingsHashtable.containsKey("btn1-lbl") ? oscSettingsHashtable.get("btn1-lbl") : "btn1",
                oscSettingsHashtable.get("btn1-butpres"),
                Boolean.parseBoolean(oscSettingsHashtable.get("btn1-trgbutrel")),
                oscSettingsHashtable.get("btn1-butrel"),
                button1, this));
                
        Button button2 = (Button) findViewById(R.id.button2);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(1,
                oscSettingsHashtable.containsKey("btn2-lbl") ? oscSettingsHashtable.get("btn2-lbl") : "btn2",
        		oscSettingsHashtable.get("btn2-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn2-trgbutrel")),
        		oscSettingsHashtable.get("btn2-butrel"),
        		button2, this));
        
        Button button3 = (Button) findViewById(R.id.button3);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(2,
                oscSettingsHashtable.containsKey("btn3-lbl") ? oscSettingsHashtable.get("btn3-lbl") : "btn3",
        		oscSettingsHashtable.get("btn3-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn3-trgbutrel")),
        		oscSettingsHashtable.get("btn3-butrel"),
        		button3, this));
        
        Button button4 = (Button) findViewById(R.id.button4);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(3,
                oscSettingsHashtable.containsKey("btn4-lbl") ? oscSettingsHashtable.get("btn4-lbl") : "btn4",
        		oscSettingsHashtable.get("btn4-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn4-trgbutrel")),
        		oscSettingsHashtable.get("btn4-butrel"),
        		button4, this));
        
        Button button5 = (Button) findViewById(R.id.button5);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(4,
                oscSettingsHashtable.containsKey("btn5-lbl") ? oscSettingsHashtable.get("btn5-lbl") : "btn5",
        		oscSettingsHashtable.get("btn5-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn5-trgbutrel")),
        		oscSettingsHashtable.get("btn5-butrel"),
        		button5, this));
        
        Button button6 = (Button) findViewById(R.id.button6);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(5,
                oscSettingsHashtable.containsKey("btn6-lbl") ? oscSettingsHashtable.get("btn6-lbl") : "btn6",
        		oscSettingsHashtable.get("btn6-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn6-trgbutrel")),
        		oscSettingsHashtable.get("btn6-butrel"),
        		button6, this));
        
        Button button7 = (Button) findViewById(R.id.button7);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(6,
                oscSettingsHashtable.containsKey("btn7-lbl") ? oscSettingsHashtable.get("btn7-lbl") : "btn7",
        		oscSettingsHashtable.get("btn7-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn7-trgbutrel")),
        		oscSettingsHashtable.get("btn7-butrel"),
        		button7, this));
        
        Button button8 = (Button) findViewById(R.id.button8);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(7,
                oscSettingsHashtable.containsKey("btn8-lbl") ? oscSettingsHashtable.get("btn8-lbl") : "btn8",
        		oscSettingsHashtable.get("btn8-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn8-trgbutrel")),
        		oscSettingsHashtable.get("btn8-butrel"),
        		button8, this));
        
        Button button9 = (Button) findViewById(R.id.button9);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(8,
                oscSettingsHashtable.containsKey("btn9-lbl") ? oscSettingsHashtable.get("btn9-lbl") : "btn9",
        		oscSettingsHashtable.get("btn9-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn9-trgbutrel")),
        		oscSettingsHashtable.get("btn9-butrel"),
        		button9, this));
        
        Button button10 = (Button) findViewById(R.id.button10);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(9,
                oscSettingsHashtable.containsKey("btn10-lbl") ? oscSettingsHashtable.get("btn10-lbl") : "btn10",
        		oscSettingsHashtable.get("btn10-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn10-trgbutrel")),
        		oscSettingsHashtable.get("btn10-butrel"),
        		button10, this));
        
        Button button11 = (Button) findViewById(R.id.button11);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(10,
                oscSettingsHashtable.containsKey("btn11-lbl") ? oscSettingsHashtable.get("btn11-lbl") : "btn11",
        		oscSettingsHashtable.get("btn11-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn11-trgbutrel")),
        		oscSettingsHashtable.get("btn11-butrel"),
        		button11, this));
        
        Button button12 = (Button) findViewById(R.id.button12);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(11,
                oscSettingsHashtable.containsKey("btn12-lbl") ? oscSettingsHashtable.get("btn12-lbl") : "btn12",
        		oscSettingsHashtable.get("btn12-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn12-trgbutrel")),
        		oscSettingsHashtable.get("btn12-butrel"),
        		button12, this));
        
        Button button13 = (Button) findViewById(R.id.button13);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(12,
                oscSettingsHashtable.containsKey("btn13-lbl") ? oscSettingsHashtable.get("btn13-lbl") : "btn13",
        		oscSettingsHashtable.get("btn13-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn13-trgbutrel")),
        		oscSettingsHashtable.get("btn13-butrel"),
        		button13, this));
        
        Button button14 = (Button) findViewById(R.id.button14);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(13,
                oscSettingsHashtable.containsKey("btn14-lbl") ? oscSettingsHashtable.get("btn14-lbl") : "btn14",
        		oscSettingsHashtable.get("btn14-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn14-trgbutrel")),
        		oscSettingsHashtable.get("btn14-butrel"),
        		button14, this));
        
        Button button15 = (Button) findViewById(R.id.button15);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(14,
                oscSettingsHashtable.containsKey("btn15-lbl") ? oscSettingsHashtable.get("btn15-lbl") : "btn15",
        		oscSettingsHashtable.get("btn15-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn15-trgbutrel")),
        		oscSettingsHashtable.get("btn15-butrel"),
        		button15, this));
        
        Button button16 = (Button) findViewById(R.id.button16);
        buttonOSCWrapperList.add(ButtonOSCWrapper.createInstance(15,
                oscSettingsHashtable.containsKey("btn16-lbl") ? oscSettingsHashtable.get("btn16-lbl") : "btn16",
        		oscSettingsHashtable.get("btn16-butpres"), 
        		Boolean.parseBoolean(oscSettingsHashtable.get("btn16-trgbutrel")),
        		oscSettingsHashtable.get("btn16-butrel"),
        		button16, this));
        
        
        
        ToggleButton toggle1 = (ToggleButton) findViewById(R.id.toggleButton1);
        toggleOSCWrapperList.add(ToggleOSCWrapper.createInstance(0,
                oscSettingsHashtable.containsKey("tog1-onlbl") ? oscSettingsHashtable.get("tog1-onlbl") : "tog1",
                oscSettingsHashtable.containsKey("tog1-offlbl") ? oscSettingsHashtable.get("tog1-offlbl") : "tog1",
        		oscSettingsHashtable.get("tog1-togon"),
        		oscSettingsHashtable.get("tog1-togoff"),
        		toggle1, this));
        
        ToggleButton toggle2 = (ToggleButton) findViewById(R.id.toggleButton2);
        toggleOSCWrapperList.add(ToggleOSCWrapper.createInstance(1,
                oscSettingsHashtable.containsKey("tog2-onlbl") ? oscSettingsHashtable.get("tog2-onlbl") : "tog2",
                oscSettingsHashtable.containsKey("tog2-offlbl") ? oscSettingsHashtable.get("tog2-offlbl") : "tog2",
                oscSettingsHashtable.get("tog2-togon"),
        		oscSettingsHashtable.get("tog2-togoff"),
        		toggle2, this));
        
        ToggleButton toggle3 = (ToggleButton) findViewById(R.id.toggleButton3);
        toggleOSCWrapperList.add(ToggleOSCWrapper.createInstance(2,
                oscSettingsHashtable.containsKey("tog3-onlbl") ? oscSettingsHashtable.get("tog3-onlbl") : "tog3",
                oscSettingsHashtable.containsKey("tog3-offlbl") ? oscSettingsHashtable.get("tog3-offlbl") : "tog3",
                oscSettingsHashtable.get("tog3-togon"),
        		oscSettingsHashtable.get("tog3-togoff"),
        		toggle3, this));
        
        ToggleButton toggle4 = (ToggleButton) findViewById(R.id.toggleButton4);
        toggleOSCWrapperList.add(ToggleOSCWrapper.createInstance(3,
                oscSettingsHashtable.containsKey("tog4-onlbl") ? oscSettingsHashtable.get("tog4-onlbl") : "tog4",
                oscSettingsHashtable.containsKey("tog4-offlbl") ? oscSettingsHashtable.get("tog4-offlbl") : "tog4",
                oscSettingsHashtable.get("tog4-togon"),
        		oscSettingsHashtable.get("tog4-togoff"),
        		toggle4, this));
        
        ToggleButton toggle5 = (ToggleButton) findViewById(R.id.toggleButton5);
        toggleOSCWrapperList.add(ToggleOSCWrapper.createInstance(4,
                oscSettingsHashtable.containsKey("tog5-onlbl") ? oscSettingsHashtable.get("tog5-onlbl") : "tog5",
                oscSettingsHashtable.containsKey("tog5-offlbl") ? oscSettingsHashtable.get("tog5-offlbl") : "tog5",
                oscSettingsHashtable.get("tog5-togon"),
        		oscSettingsHashtable.get("tog5-togoff"),
        		toggle5, this));
        
        ToggleButton toggle6 = (ToggleButton) findViewById(R.id.toggleButton6);
        toggleOSCWrapperList.add(ToggleOSCWrapper.createInstance(5,
                oscSettingsHashtable.containsKey("tog6-onlbl") ? oscSettingsHashtable.get("tog6-onlbl") : "tog6",
                oscSettingsHashtable.containsKey("tog6-offlbl") ? oscSettingsHashtable.get("tog6-offlbl") : "tog6",
                oscSettingsHashtable.get("tog6-togon"),
        		oscSettingsHashtable.get("tog6-togoff"),
        		toggle6, this));
        
        ToggleButton toggle7 = (ToggleButton) findViewById(R.id.toggleButton7);
        toggleOSCWrapperList.add(ToggleOSCWrapper.createInstance(6,
                oscSettingsHashtable.containsKey("tog7-onlbl") ? oscSettingsHashtable.get("tog7-onlbl") : "tog7",
                oscSettingsHashtable.containsKey("tog7-offlbl") ? oscSettingsHashtable.get("tog7-offlbl") : "tog7",
                oscSettingsHashtable.get("tog7-togon"),
        		oscSettingsHashtable.get("tog7-togoff"),
        		toggle7, this));
        
        ToggleButton toggle8 = (ToggleButton) findViewById(R.id.toggleButton8);
        toggleOSCWrapperList.add(ToggleOSCWrapper.createInstance(7,
                oscSettingsHashtable.containsKey("tog8-onlbl") ? oscSettingsHashtable.get("tog8-onlbl") : "tog8",
                oscSettingsHashtable.containsKey("tog8-offlbl") ? oscSettingsHashtable.get("tog8-offlbl") : "tog8",
                oscSettingsHashtable.get("tog8-togon"),
        		oscSettingsHashtable.get("tog8-togoff"),
        		toggle8, this));



        SeekBar seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBarOSCWrapperList.add(SeekBarOSCWrapper.createInstance(0, "seekBar1",
        		oscSettingsHashtable.get("seekBar1-valcng"),
        		safeFloatParse(oscSettingsHashtable.get("seekBar1-minval"), 0),
        		safeFloatParse(oscSettingsHashtable.get("seekBar1-maxval"), 100),
        		seekBar1, this));
        
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBarOSCWrapperList.add(SeekBarOSCWrapper.createInstance(1, "seekBar2",
        		oscSettingsHashtable.get("seekBar2-valcng"),
        		safeFloatParse(oscSettingsHashtable.get("seekBar2-minval"), 0),
        		safeFloatParse(oscSettingsHashtable.get("seekBar2-maxval"), 100),
        		seekBar2, this));
        
        SeekBar seekBar3 = (SeekBar) findViewById(R.id.seekBar3);
        seekBarOSCWrapperList.add(SeekBarOSCWrapper.createInstance(2, "seekBar3",
        		oscSettingsHashtable.get("seekBar3-valcng"),
        		safeFloatParse(oscSettingsHashtable.get("seekBar3-minval"), 0),
        		safeFloatParse(oscSettingsHashtable.get("seekBar3-maxval"), 100),
        		seekBar3, this));
        
        SeekBar seekBar4 = (SeekBar) findViewById(R.id.seekBar4);
        seekBarOSCWrapperList.add(SeekBarOSCWrapper.createInstance(3, "seekBar4",
        		oscSettingsHashtable.get("seekBar4-valcng"),
        		safeFloatParse(oscSettingsHashtable.get("seekBar4-minval"), 0),
        		safeFloatParse(oscSettingsHashtable.get("seekBar4-maxval"), 100),
        		seekBar4, this));





        // DEFINING OSC LISTENERS HERE
        this.btnListener = new OSCListener() {
            public void acceptMessage(java.util.Date time, OSCMessage message) {
            String[] addressParts = message.getAddress().split("/");
            int index = extractIndex(addressParts[1], "btn");
            if(index < 1 || index > 16) {
                return;
            }
            ButtonOSCWrapper btn = buttonOSCWrapperList.get(index - 1);

            String action = addressParts[2];

            if(action.equals("msgButtonPressed")) {
                btn.setMessageButtonPressed(Utils.convertToString(message.getArguments()));
            }
            else if(action.equals("msgButtonReleased")) {
                btn.setMessageButtonReleased(Utils.convertToString(message.getArguments()));
            }
            else if(action.equals("triggerOnButtonReleased")) {
                // only check the first arguments
                List<Object> arguments = message.getArguments();
                if(arguments.size() > 0) {
                    Object value = arguments.get(0);
                    if(value instanceof Integer) {
                        int intValue = ((Integer) value).intValue();
                        if(intValue == 0) {
                            btn.setTriggerWhenButtonReleased(false);
                        }
                        else if(intValue == 1) {
                            btn.setTriggerWhenButtonReleased(true);
                        }
                    }
                }
            }
            else if(action.equals("label")) {
                btn.setName(Utils.convertToString(message.getArguments()));
            }
            }
        };

        this.toggleListener = new OSCListener() {
            public void acceptMessage(java.util.Date time, OSCMessage message) {
            String[] addressParts = message.getAddress().split("/");
            int index = extractIndex(addressParts[1], "tog");
            if(index < 1 || index > 8) {
                return;
            }
            ToggleOSCWrapper btn = toggleOSCWrapperList.get(index - 1);

            String action = addressParts[2];

            if(action.equals("msgToggledOn")) {
                btn.setMessageToggleOn(Utils.convertToString(message.getArguments()));
            }
            else if(action.equals("msgToggledOff")) {
                btn.setMessageToggleOff(Utils.convertToString(message.getArguments()));
            }
            else if(action.equals("value")) {
                // only check the first arguments
                List<Object> arguments = message.getArguments();
                if(arguments.size() > 0) {
                    Object value = arguments.get(0);
                    if(value instanceof Integer) {
                        int intValue = ((Integer) value).intValue();
                        if(intValue == 0) {
                            btn.setToggled(false);
                        }
                        else if(intValue == 1) {
                            btn.setToggled(true);
                        }
                    }
                }
            }
            else if(action.equals("labelOn")) {
                btn.setOnLabel(Utils.convertToString(message.getArguments()));
            }
            else if(action.equals("labelOff")) {
                btn.setOffLabel(Utils.convertToString(message.getArguments()));
            }
            }
        };

        this.seekBarListener = new OSCListener() {
            public void acceptMessage(java.util.Date time, OSCMessage message) {
            String[] addressParts = message.getAddress().split("/");
            int index = extractIndex(addressParts[1], "sb");
            if(index < 1 || index > 4) {
                return;
            }
            SeekBarOSCWrapper seekBar = seekBarOSCWrapperList.get(index - 1);

            String action = addressParts[2];

            if(action.equals("msgValueChanged")) {
                seekBar.setMsgValueChanged(Utils.convertToString(message.getArguments()));
            }
            else if(action.equals("range")) {
                List<Object> arguments = message.getArguments();
                if(arguments.size() < 2) {
                    return;
                }
                float minValue = 0f;
                float maxValue = 0f;
                try {
                    if(arguments.get(0) instanceof Integer) {
                        minValue = (float) ((Integer) arguments.get(0)).intValue();
                    }
                    else if(arguments.get(0) instanceof Float) {
                        minValue = ((Float) arguments.get(0)).floatValue();
                    }
                    else {
                        return;
                    }

                    if(arguments.get(1) instanceof Integer) {
                        maxValue = (float) ((Integer) arguments.get(1)).intValue();
                    }
                    else if(arguments.get(1) instanceof Float) {
                        maxValue = ((Float) arguments.get(1)).floatValue();
                    }
                    else {
                        return;
                    }
                }
                catch(NumberFormatException nfe) {
                    return;
                }
                seekBar.setMinValue(minValue);
                seekBar.setMaxValue(maxValue);
            }
            else if(action.equals("value")) {
                List<Object> arguments = message.getArguments();
                if(arguments.size() < 1) {
                    return;
                }
                float value = 0f;
                try {
                    if(arguments.get(0) instanceof Integer) {
                        value = (float)((Integer) arguments.get(0)).intValue();
                    }
                    else if(arguments.get(0) instanceof Float) {
                        value = ((Float) arguments.get(0)).floatValue();
                    }
                    else {
                        return;
                    }

                }
                catch(NumberFormatException nfe) {
                    return;
                }
                seekBar.setValue(value);
            }
            }
        };

        this.saveListener = new OSCListener() {
            public void acceptMessage(Date date, OSCMessage oscMessage) {
                saveOSCSettingsIntoFile();
            }
        };


        // showing the promo for AndrOSC
        SharedPreferences settings = getSharedPreferences(PREF_FILE, 0);
        boolean dialogShown = settings.getBoolean(PROMO_SHOWN, false);

        boolean wifiOn = checkWifiState();

        if(wifiOn && !dialogShown) {
            showAndrOSCPromo();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(this.oscPortIn != null && this.oscPortIn.isListening()) {
            this.oscPortIn.stopListening();
            this.oscPortIn.close();
        }

        if(this.oscPortOut != null) {
            this.oscPortOut.close();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        restoreNetworkSettingsFromFile();
        initializeOSC();
        initializeIncomingOSC();
    }

    public boolean isEditMode() {
    	return this.editMode;
    }
    
    /***
     * Normally used to display the OSC message passed by the wrappers.
     * @param message
     */
    public void setDebugMessage(String message) {
    	this.debugTextView.setText(message);
    }
    
    /***
     * In edit mode, the ButtonOSCWrapper calls this method and passes itself as the argument.
     * An intent object is created with the properties of the wrapper object attached.
     * Upon successful return of the intent result, the new values are passed back to the wrapper object
     * in the handleButtonOSCSettingResult method.
     * @param selectedButton
     */
    public void callButtonOSCSetter(ButtonOSCWrapper selectedButton) {
    	try {
    		//this.selectedButtonOSCWrapper = selectedButton;
    		
			Intent intent = new Intent(this, ButtonOSCSettingActivity.class);
			intent.setAction("com.ahmetkizilay.controls.osc.ButtonOSCSetter");
            intent.putExtra("label", selectedButton.getName());
			intent.putExtra("msgButtonPressed", selectedButton.getMessageButtonPressedRaw());
			intent.putExtra("msgButtonReleased", selectedButton.getMessageButtonReleasedRaw());
			intent.putExtra("trigButtonReleased", selectedButton.getTriggerWhenButtonReleased());
			intent.putExtra("index", selectedButton.getIndex());
			startActivityForResult(intent, BUTTON_OSC_INTENT_RESULT);
		} catch(Throwable t) {
			t.printStackTrace();
			Toast.makeText(this, "Error calling ButtonOSCSetter Intent", Toast.LENGTH_LONG).show();
		}
    }
    
    /***
     * In edit mode, the ToggleOSCWrapper calls this method and passes itself as the argument.
     * An intent object is created with the properties of the wrapper object attached.
     * Upon successful return of the intent result, the new values are passed back to the wrapper object
     * in the handleToggleOSCSettingResult method.
     * @param selectedToggle
     */
    public void callToggleOSCSetter(ToggleOSCWrapper selectedToggle) {
    	try {
    		//selectedToggleOSCWrapper = selectedToggle;
    		
			Intent intent = new Intent(this, ToggleOSCSettingActivity.class);
			intent.setAction("com.ahmetkizilay.controls.osc.ToggleOSCSetter");
            intent.putExtra("onLabel", selectedToggle.getOnLabel());
            intent.putExtra("offLabel", selectedToggle.getOffLabel());
			intent.putExtra("msgToggleOn", selectedToggle.getMessageToggleOnRaw());
			intent.putExtra("msgToggleOff", selectedToggle.getMessageToggleOffRaw());
			intent.putExtra("index", selectedToggle.getIndex());
			startActivityForResult(intent, TOGGLE_OSC_INTENT_RESULT);
		} catch(Throwable t) {
			t.printStackTrace();
			Toast.makeText(this, "Error calling ToggleOSCSetter Intent", Toast.LENGTH_LONG).show();
		}
    }
    
    /***
     * In edit mode, the SeekBarOSCWrapper calls this method and passes itself as the argument.
     * An intent object is created with the properties of the wrapper object attached.
     * Upon successful return of the intent result, the new values are passed back to the wrapper object
     * in the handleSeekBarOSCSettingResult method.
     * @param selectedSeekBar
     */
    public void callSeekBarOSCSetter(SeekBarOSCWrapper selectedSeekBar) {
    	try {
    		//selectedSeekBarOSCWrapper = selectedSeekBar;
    		
			Intent intent = new Intent(this, SeekBarOSCSettingActivity.class);
			intent.putExtra("msgValueChanged", selectedSeekBar.getMsgValueChanged());
			intent.putExtra("maxValue", selectedSeekBar.getMaxValue());
			intent.putExtra("minValue", selectedSeekBar.getMinValue());
			intent.putExtra("index", selectedSeekBar.getIndex());
			
			intent.setAction("com.ahmetkizilay.controls.osc.SeekBarOSCSetter");
			startActivityForResult(intent, SEEKBAR_OSC_INTENT_RESULT);
		} catch(Throwable t) {
			t.printStackTrace();
			Toast.makeText(this, "Error calling SeekBarOSCSetter Intent", Toast.LENGTH_LONG).show();
		}
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PaymentDialogFragment.PAYMENT_RESULT_CODE) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag("dlg-donate");
            if (fragment != null)
            {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }

		if(resultCode == Activity.RESULT_OK) {
			switch(requestCode) {
			case BUTTON_OSC_INTENT_RESULT:
				handleButtonOSCSettingResult(data);
				break;
			case TOGGLE_OSC_INTENT_RESULT:
				handleToggleOSCSettingResult(data);
				break;
			case SEEKBAR_OSC_INTENT_RESULT:
				handleSeekBarOSCSettingResult(data);
				break;
            }
		}
	}
        
    /**
     * The intent sent from the settings activity is passed into the caller wrapper object. 
     * @param intent
     */
    private void handleButtonOSCSettingResult(Intent intent) {

        String label = intent.getExtras().get("label").toString();
		String msgButtonPressed = intent.getExtras().get("msgButtonPressed").toString();
		String msgButtonReleased = (String) intent.getExtras().get("msgButtonReleased").toString();
		boolean trigButtonReleased = Boolean.parseBoolean(intent.getExtras().get("trigButtonReleased").toString());
		int indexSelectedButton = Integer.parseInt(intent.getExtras().get("index").toString());
		
		ButtonOSCWrapper selectedButton = this.buttonOSCWrapperList.get(indexSelectedButton);
        selectedButton.setName(label);
		selectedButton.setMessageButtonPressed(msgButtonPressed);
		selectedButton.setMessageButtonReleased(msgButtonReleased);
		selectedButton.setTriggerWhenButtonReleased(trigButtonReleased);
		
		saveOSCSettingsIntoFile();
    }

    /**
     * The intent sent from the settings activity is passed into the caller wrapper object. 
     * @param intent
     */
    private void handleToggleOSCSettingResult(Intent intent) {
    	String onLabel = intent.getExtras().get("onLabel").toString();
        String offLabel = intent.getExtras().get("offLabel").toString();
		String msgToggleOn = intent.getExtras().get("msgToggleOn").toString();
		String msgToggleOff = intent.getExtras().get("msgToggleOff").toString();
		int selectedIndex = Integer.parseInt(intent.getExtras().get("index").toString());
		
		ToggleOSCWrapper selectedToggle = toggleOSCWrapperList.get(selectedIndex);
        selectedToggle.setOnLabel(onLabel);
        selectedToggle.setOffLabel(offLabel);
		selectedToggle.setMessageToggleOn(msgToggleOn);
		selectedToggle.setMessageToggleOff(msgToggleOff);
		
		saveOSCSettingsIntoFile();
    }
    
    /**
     * The intent sent from the settings activity is passed into the caller wrapper object. 
     * @param intent
     */
    private void handleSeekBarOSCSettingResult(Intent intent) {
    	
		String msgValueChanged = intent.getExtras().get("msgValueChanged").toString();
		float fltMaxValue = Float.parseFloat(intent.getExtras().get("maxValue").toString());
		float fltMinValue = Float.parseFloat(intent.getExtras().get("minValue").toString());
		int selectedIndex = Integer.parseInt(intent.getExtras().get("index").toString());
		
		SeekBarOSCWrapper selectedSeekBar = seekBarOSCWrapperList.get(selectedIndex);
		selectedSeekBar.setMsgValueChanged(msgValueChanged);
		selectedSeekBar.setMaxValue(fltMaxValue);
		selectedSeekBar.setMinValue(fltMinValue);
		
		saveOSCSettingsIntoFile();
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	MenuItem editMenuItem = menu.getItem(1);
    	editMenuItem.setTitle(editMode ? "play mode" : "edit mode");
    	
    	return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.about_page:
			createAboutDialog();
			return true;
		case R.id.edit_menu:
			toggleEditMode();
			return true;
		case R.id.network_menu:
            createNetworkDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    }

    /**
     * Creates WifiAlert Fragment. Called from checkWifiState method
     * on callback, ACTION_WIFI_SETTINGS intent is called to change wifi settings.
     */
    private void createWifiAlertDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(WIFI_ALERT_DIALOG);
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final WifiSettingsDialogFragment frg = WifiSettingsDialogFragment.newInstance();
        frg.setWifiSettingsDialogListener(new WifiSettingsDialogFragment.WifiSettingsDialogListener() {

            public void onWifiSettingsRequested() {
                frg.dismiss();
                try {
                    Intent wifiIntent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                    startActivity(wifiIntent);
                }
                catch(Exception exp) {
                    Toast.makeText(QuickOSCActivity.this, "Cannot Open Wifi Settings", Toast.LENGTH_SHORT).show();
                }
            }
        });

        frg.show(ft, WIFI_ALERT_DIALOG);
    }
    
     /**
     * Creates About Me Fragment. Called from onOptionsItemSelected method
     * on callback, either donate dialog is shown or redirected to rate activity.
     * and reconnects to the network
     */
    private void createAboutDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(ABOUT_DIALOG);
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final AboutMeDialogFragment frg = AboutMeDialogFragment.newInstance();
        frg.setAboutMeDialogListener(new AboutMeDialogFragment.AboutMeDialogListener() {


            public void onRateMeSelected() {
                frg.dismiss();

                try {
                    Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                    startActivity(rateIntent);
                }
                catch(Exception exp) {
                    Toast.makeText(QuickOSCActivity.this, "Cannot Open Android Market", Toast.LENGTH_SHORT).show();
                }
            }

            public void onDonateSelected() {
                frg.dismiss();
                createDonationDialog();
            }
        });

        frg.show(ft, ABOUT_DIALOG);
    }

    private void createDonationDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(DONATE_DIALOG);
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final PaymentDialogFragment frgDonationsDialog = PaymentDialogFragment.getInstance(R.array.product_ids);
        frgDonationsDialog.setPaymentCompletedListener(new PaymentDialogFragment.PaymentCompletedListener() {
            public void onPaymentCompleted() {
               frgDonationsDialog.dismiss();
               showThankYouDialog();
            }
        });

        frgDonationsDialog.show(ft, DONATE_DIALOG);
    }

    private void showThankYouDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(THANKS_DIALOG);
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final ThankYouDialogFragment frgThankYouDialog = ThankYouDialogFragment.newInstance();
        frgThankYouDialog.show(ft, THANKS_DIALOG);
    }

    /***
     * this method is called is PROMO_SHOWN flag is still false in shared preferences.
     * the promo dialog is shown if AndrOSC activity is not present
     * and the flag is set to if the user clicks to view promo or checks the never show check box
     */
    private void showAndrOSCPromo() {
        final String andrOSCPackage = "com.ahmetkizilay.controls.androsc";
        String buttonLabel = "GO TO APP";
        String message = "If you are interested in OSC, please check out my other app AndrOSC";
        String title = "New App - AndrOSC";
        int iconId = R.drawable.qosc;

        // checking if AndrOSC is already installed
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(andrOSCPackage, PackageManager.GET_ACTIVITIES);
            saveSharedPreference(PROMO_SHOWN, true);
            return;
        }
        catch(PackageManager.NameNotFoundException nnfe) {}

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(PROMO_DIALOG);
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final PromoDialogFragment frg = PromoDialogFragment.newInstance(iconId, title, message, buttonLabel);
        frg.setDialogClosedListener(new PromoDialogFragment.DialogClosedListener() {
            public void onDialogClosed(boolean neverOpen) {
                if(neverOpen) {
                    saveSharedPreference(PROMO_SHOWN, true);
                }
            }
            public void onPromoRequested() {
                frg.dismiss();

                saveSharedPreference(PROMO_SHOWN, true);

                try {
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + andrOSCPackage));
                    startActivity(viewIntent);
                }
                catch(Exception exp) {
                    Toast.makeText(QuickOSCActivity.this, "Cannot Open Android Market", Toast.LENGTH_SHORT).show();
                }
            }
        });
        frg.show(ft, PROMO_DIALOG);
    }

    /***
     * Saves a boolean value in shared preferences.
     * @param tag string to hold the reference to the preference
     * @param value value to be saved
     */
    private void saveSharedPreference(String tag, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_FILE, 0).edit();
        editor.putBoolean(tag, value);
        editor.commit();
    }

    /**
     * Creates Network Settings Fragment. Called from onOptionsItemSelected method
     * onSettingsSaved callback from the fragment stores ipAddress and port values
     * and reconnects to the network
     */
    private void createNetworkDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(NETWORK_DIALOG);
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final NetworkDialogFragment frg = NetworkDialogFragment.newInstance(ipAddress, port, mListenIncoming, Utils.getIpAddress(true), inPort);
        frg.setNetworkDialogListener(new NetworkDialogFragment.NetworkDialogListener() {
            public void onSettingsSaved(String ipAddress, int port, boolean listenIncoming, int inPort) {
                QuickOSCActivity.this.ipAddress = ipAddress;
                QuickOSCActivity.this.port = port;
                QuickOSCActivity.this.inPort = inPort;

                saveNetworkSettinsIntoFile();
                initializeOSC();
                initializeIncomingOSC();
            }
        });
        frg.show(ft, NETWORK_DIALOG);
    }
    
    /**
     * Toggles edit mode triggered by the menu option.
     */
    private void toggleEditMode() {
    	if(isEditMode()) {
    		this.editMode = false;
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Toast.makeText(this, "Edit Mode Disabled", Toast.LENGTH_SHORT).show();
    	}
    	else {
    		this.editMode = true;
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
            Toast.makeText(this, "Edit Mode Enabled", Toast.LENGTH_SHORT).show();
    	}
    }
    
    /***
     * Initializes the OSCPortOut class with the given ipAddress and port.
     * Called once at the beginning in onCreate() method and at the end of the network settings dialog save action.
     */
    private void initializeOSC() {
    	try {
    		
    		if(oscPortOut != null) {
    			oscPortOut.close();
    		}
    		
    		oscPortOut = new OSCPortOut(InetAddress.getByName(ipAddress), port);
       	}
    	catch(Exception exp) {
    		Toast.makeText(this, "Error Initializing OSC", Toast.LENGTH_SHORT).show();
    		oscPortOut = null;
    	}
    }

    /**
     * Initializes the OSCPortIn class with with the given port
     * called from onStart method
     *
     * AVAILABLE MESSAGE FORMATS
     *
     * for Button controller, N = {1..16}
     * /btnN/msgButtonPressed <args ...>
     * /btnN/msgButtonReleased <args..>
     * /btnN/triggerOnButtonReleased <1/0>
     *
     * for Toggle controllers, N = {1..8}
     * /togN/msgToggledOn <args >
     * /togN/msgToggledOff <args>
     * /togN/value <1/0>
     *
     * for SeekBar controllers, N = {1..4}
     * /sbN/range <min> <max>
     * /sbN/msgValueChanged <args>
     * /sbN/value <val>
     *
     * To save OSC settings
     * /save
     */
    private void initializeIncomingOSC() {
        if(this.oscPortIn != null) {
            if(this.oscPortIn.isListening()) {
                this.oscPortIn.stopListening();
            }

            this.oscPortIn.close();
            this.oscPortIn = null;

        }

        if(!this.mListenIncoming) {
            return;
        }

        try {
            this.oscPortIn = new OSCPortIn(this.inPort);
            this.oscPortIn.addListener("/btn*/*", btnListener);
            this.oscPortIn.addListener("/tog*/*", toggleListener);
            this.oscPortIn.addListener("/sb*/*", seekBarListener);
            this.oscPortIn.addListener("/save", saveListener);
            this.oscPortIn.startListening();
        }
        catch(SocketException se) {
            se.printStackTrace();
            Log.d("QuickOSCActivity", se.getMessage());
        }
    }
    
    /**
     * Sends the OSC message passed by the Wrappers. Requires a successful initializeOSC() method
     * to be able to access the host.
     * @param message
     */
    public void sendOSC(String message) {    	
    	try {    		
	    	new AsyncSendOSCTask(this, this.oscPortOut).execute(new OSCMessage(message));	    	
    	}
    	catch(Exception exp) {
    		Toast.makeText(this, "Error Sending Message", Toast.LENGTH_SHORT).show();
    	}
    }
    
    /**
     * Sends the OSC message passed by the Wrappers. Requires a successful initializeOSC() method
     * to be able to access the host.
     * @param address
     * @param arguments
     */
    
    public void sendOSC(String address, List<Object> arguments) {
    	try {    		
	    	new AsyncSendOSCTask(this, this.oscPortOut).execute(new OSCMessage(address, arguments));	    	
    	}
    	catch(Exception exp) {
    		Toast.makeText(this, "Error Sending Message", Toast.LENGTH_SHORT).show();
    	}
    }
    
    
    /**
     * Saves network settings to file to be on next startup
     */
    private void saveNetworkSettinsIntoFile() {
    	try {
    		try {
        		FileOutputStream fos = openFileOutput(NETWORK_SETTINGS_FILE, Context.MODE_PRIVATE);
        		
        		String data = ipAddress + "#" + port + "#" + mListenIncoming + "#" + inPort;
        		fos.write(data.getBytes());
        		fos.close();
        	}
        	catch(Exception exp) {
        		Toast.makeText(this, "Could Not Update SCAuth File", Toast.LENGTH_SHORT).show();
        		exp.printStackTrace();
        	}
    	}
    	catch(Exception exp) {
    		Toast.makeText(this, "Error Saving Network Settings", Toast.LENGTH_SHORT).show();
    	}
    }
    
    /**
     * Restores network settings which were saved in previous sessions
     */
    private void restoreNetworkSettingsFromFile() {
    	try {	
    		FileInputStream fis = openFileInput(NETWORK_SETTINGS_FILE);
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		byte[] buffer = new byte[512];
    		int bytes_read;
    		while((bytes_read = fis.read(buffer)) != -1) {
    			baos.write(buffer, 0, bytes_read);
    		}
    		
    		String data = new String(baos.toByteArray());
    		String[] pieces = data.split("#");
    		
    		ipAddress = pieces[0];
    		port = Integer.parseInt(pieces[1]);
            if(pieces.length > 2) {
                mListenIncoming = Boolean.parseBoolean(pieces[2]);
                inPort = Integer.parseInt(pieces[3]);
            }
            else {
                mListenIncoming = false;
                inPort = 8090;
            }
    	}
    	catch(FileNotFoundException fnfe) {}
    	catch(Exception exp) {
    		Toast.makeText(this, "Could Not Read SCAuth File", Toast.LENGTH_SHORT).show();
    		ipAddress = "127.0.0.1"; port = 8000;
            mListenIncoming = false;
            inPort = 8090;
    	}
    }
    
    private void restoreOSCSettingsFromFile() {
    	try {
    		oscSettingsHashtable.clear();
    		
    		FileInputStream fis = openFileInput(OSC_SETTINGS_FILE);
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		byte[] buffer = new byte[512];
    		int bytes_read;
    		while((bytes_read = fis.read(buffer)) != -1) {
    			baos.write(buffer, 0, bytes_read);
    		}
    		
    		String data = new String(baos.toByteArray());
    		String[] pieces = data.split("#x#x#");
    		
    		for(int i = 0; i < pieces.length; i+=2) {
    			oscSettingsHashtable.put(pieces[i], pieces[i+1]);
    		}
    	}
    	catch(FileNotFoundException fnfe) {}
    	catch(Exception exp) {
    		Toast.makeText(this, "Could Not Read OSC Settings File", Toast.LENGTH_SHORT).show();
    		oscSettingsHashtable.clear();
       	}
    }
    
    /**
     * Saves network settings to file to be on next startup
     */
    private void saveOSCSettingsIntoFile() {
    	try {
    		try {
        		FileOutputStream fos = openFileOutput(OSC_SETTINGS_FILE, Context.MODE_PRIVATE);
        		
        		StringBuffer dataBuffer = new StringBuffer();
        		for(int i = 0; i < buttonOSCWrapperList.size(); i++) {
        			ButtonOSCWrapper thisButtonWrapper = buttonOSCWrapperList.get(i);
                    dataBuffer.append("btn" + (i+1) + "-lbl"       + "#x#x#" + thisButtonWrapper.getName() + "#x#x#");
                    dataBuffer.append("btn" + (i+1) + "-butpres"   + "#x#x#" + thisButtonWrapper.getMessageButtonPressedRaw() + "#x#x#");
                    dataBuffer.append("btn" + (i+1) + "-trgbutrel" + "#x#x#" + thisButtonWrapper.getTriggerWhenButtonReleased() + "#x#x#");
                    dataBuffer.append("btn" + (i+1) + "-butrel"    + "#x#x#" + thisButtonWrapper.getMessageButtonReleasedRaw() + "#x#x#");
        		}
        		
        		for(int i = 0; i < toggleOSCWrapperList.size(); i++) {
        			ToggleOSCWrapper thisToggleOSCWrapper = toggleOSCWrapperList.get(i);
                    dataBuffer.append("tog" + (i+1) + "-onlbl" + "#x#x#" + thisToggleOSCWrapper.getOnLabel() + "#x#x#");
                    dataBuffer.append("tog" + (i+1) + "-offlbl" + "#x#x#" + thisToggleOSCWrapper.getOffLabel() + "#x#x#");
        			dataBuffer.append("tog" + (i+1) + "-togon" + "#x#x#" + thisToggleOSCWrapper.getMessageToggleOnRaw() + "#x#x#");
        			dataBuffer.append("tog" + (i+1) + "-togoff" + "#x#x#" + thisToggleOSCWrapper.getMessageToggleOffRaw() + "#x#x#");
        		}
        		
        		for(int i = 0; i < seekBarOSCWrapperList.size(); i++) {
        			SeekBarOSCWrapper thisSeekBarOSCWrapper = seekBarOSCWrapperList.get(i);
        			dataBuffer.append(thisSeekBarOSCWrapper.getName() + "-valcng" + "#x#x#" + thisSeekBarOSCWrapper.getMsgValueChanged() + "#x#x#");
        			dataBuffer.append(thisSeekBarOSCWrapper.getName() + "-minval" + "#x#x#" + thisSeekBarOSCWrapper.getMinValue() + "#x#x#");
        			dataBuffer.append(thisSeekBarOSCWrapper.getName() + "-maxval" + "#x#x#" + thisSeekBarOSCWrapper.getMaxValue() + "#x#x#");
        		}
        		
        		String data = dataBuffer.toString();
        		data = data.substring(0, data.length() - 5);
        		
        		fos.write(data.toString().getBytes());
        		fos.close();
        	}
        	catch(Exception exp) {
        		Toast.makeText(this, "Could Not Update SCAuth File", Toast.LENGTH_SHORT).show();
        		exp.printStackTrace();
        	}
    	}
    	catch(Exception exp) {
    		Toast.makeText(this, "Error Saving Network Settings", Toast.LENGTH_SHORT).show();
    	}
    }
    
    private float safeFloatParse(String val, float defVal) {
    	try {
    		return Float.parseFloat(val);
    	}
    	catch(Exception nfe){
    		return defVal;
    	}
    }
    
    @Override
    public void onBackPressed() {
    	if(this.editMode) {
    		this.editMode = false;
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Toast.makeText(this, "Edit Mode Disabled", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	super.onBackPressed();
    }
    
    private boolean checkWifiState() {
    	WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    	if(!wifiManager.isWifiEnabled()) {
    		createWifiAlertDialog();
            return false;
    	}

        return true;
    }

    /**
     * A utility method to extract the id from the OSC address.
     * @param str the first part of the original OSC address
     * @param prefix the address starts with either btn, tog or sb
     * @return the number after the prefix, 0 if something is wrong
     */
    private int extractIndex(String str, String prefix) {
        int result;
        try {
            result = Integer.parseInt(str.substring(prefix.length()), 10);
            if(result < 1) {
                return 0;
            }

            return result;
        }
        catch(NumberFormatException nfe) {}
        return 0;
    }
}