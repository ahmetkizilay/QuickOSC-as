package com.ahmetkizilay.controls.osc;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * ToggleOSCWrappper
 * This is the wrapper class for ToggleButton controls.
 * Stores OSC messages, and manages the touch listener to pass OSC messages to the parent activity.
 * @author ahmetkizilay
 *
 */
public class ToggleOSCWrapper implements OnCheckedChangeListener{
	
	private QuickOSCActivity parentActivity;
	
	private String messageToggleOnAddr;
	private List<Object> messageToggleOnArgs;
	private String messageToggleOnRaw;
	
	private String messageToggleOffAddr;
	private List<Object> messageToggleOffArgs;
	private String messageToggleOffRaw;
	private String onLabel;
    private String offLabel;

    private ToggleButton toggle;
	private int index;

	public static ToggleOSCWrapper createInstance(int index, String onLabel, String offLabel, String msgToggleOn, String msgToggleOff, ToggleButton button, QuickOSCActivity parentActivity) {
		return new ToggleOSCWrapper(index, onLabel, offLabel, msgToggleOn, msgToggleOff, button, parentActivity);
	}

	private ToggleOSCWrapper(int index, String onLabel, String offLabel, String msgToggleOn, String msgToggleOff, ToggleButton button, QuickOSCActivity parentActivity) {
		this.index = index;
		this.parentActivity = parentActivity;
		this.onLabel = onLabel;
        this.offLabel = offLabel;

		this.setMessageToggleOn(msgToggleOn);
		this.setMessageToggleOff(msgToggleOff);

        this.toggle = button;
        this.toggle.setOnCheckedChangeListener(this);
        this.toggle.setText(offLabel);
        this.toggle.setTextOn(onLabel);
        this.toggle.setTextOff(offLabel);
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(parentActivity.isEditMode()) {
			parentActivity.callToggleOSCSetter(this);
			return;
		}	
		
		if(isChecked) {
			parentActivity.sendOSC(this.messageToggleOnAddr, this.messageToggleOnArgs);
			parentActivity.setDebugMessage(this.messageToggleOnRaw);
		}
		else {
			parentActivity.sendOSC(this.messageToggleOffAddr, this.messageToggleOffArgs);
			parentActivity.setDebugMessage(this.messageToggleOffRaw);
		}

	}

	public String getMessageToggleOnRaw() {
		return this.messageToggleOnRaw;
	}

	public void setMessageToggleOn(String messageToggleOn) {
		if(messageToggleOn == null || messageToggleOn.equals("")) {
			this.messageToggleOnAddr = "/tog" + (index+1) + "/1";
			this.messageToggleOnArgs = null;
			this.messageToggleOnRaw = this.messageToggleOnAddr;
		}
		else {
			this.messageToggleOnRaw = messageToggleOn;
			
			String[] messageToggleOnParts = messageToggleOn.split(" ");
			this.messageToggleOnAddr = messageToggleOnParts[0];
			if(messageToggleOnParts.length > 0) {
				this.messageToggleOnArgs = new ArrayList<Object>();
				for(int i = 1; i < messageToggleOnParts.length; i++) {
					this.messageToggleOnArgs.add(Utils.simpleParse(messageToggleOnParts[i]));
				}
			}
		}
	}

	public String getMessageToggleOffRaw() {
		return messageToggleOffRaw;
	}

	public void setMessageToggleOff(String messageToggleOff) {
		if(messageToggleOff == null || messageToggleOff.equals("")) {
			this.messageToggleOffAddr = "/tog" + (index+1) + "/0";
			this.messageToggleOffArgs = null;
			this.messageToggleOffRaw = this.messageToggleOffAddr;
		}
		else {
			this.messageToggleOffRaw = messageToggleOff;
			
			String[] messageToggleOffParts = messageToggleOff.split(" ");
			this.messageToggleOffAddr = messageToggleOffParts[0];
			if(messageToggleOffParts.length > 0) {
				this.messageToggleOffArgs = new ArrayList<Object>();
				for(int i = 1; i < messageToggleOffParts.length; i++) {
					this.messageToggleOffArgs.add(Utils.simpleParse(messageToggleOffParts[i]));
				}
			}
		}
	}

    public void setToggled(final boolean toggled) {
        this.parentActivity.runOnUiThread(new Runnable() {
            public void run() {
                ToggleOSCWrapper.this.toggle.setChecked(toggled);
            }
        });
    }

    public String getOnLabel() {
        return this.onLabel;
    }

    public String getOffLabel() {
        return this.offLabel;
    }

    public void setOnLabel(String onLabel) {
        this.onLabel = onLabel;
        this.parentActivity.runOnUiThread(new Runnable() {
            public void run() {
                if(ToggleOSCWrapper.this.toggle.isChecked()) {
                    ToggleOSCWrapper.this.toggle.setText(ToggleOSCWrapper.this.onLabel);
                }
                ToggleOSCWrapper.this.toggle.setTextOn(ToggleOSCWrapper.this.onLabel);
            }
        });
    }

    public void setOffLabel(String offLabel) {
        this.offLabel = offLabel;
        this.parentActivity.runOnUiThread(new Runnable() {
            public void run() {
                if(!ToggleOSCWrapper.this.toggle.isChecked()) {
                    ToggleOSCWrapper.this.toggle.setText(ToggleOSCWrapper.this.offLabel);
                }
                ToggleOSCWrapper.this.toggle.setTextOff(ToggleOSCWrapper.this.offLabel);
            }
        });
    }
	
	public int getIndex() {
		return this.index;
	}
	
}
