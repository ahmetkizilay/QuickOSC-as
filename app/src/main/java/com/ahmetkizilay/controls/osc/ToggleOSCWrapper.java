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
	private String name;

   private ToggleButton toggle;
	private int index;
	
	/**
	 * Default constructor publicly available for other classes.
	 * @param name Only used to set the default OSC message.
	 * @param button
	 * @param parentActivity
	 * @return
	 */
	public static ToggleOSCWrapper createInstance(int index, String name, ToggleButton button, QuickOSCActivity parentActivity) {
		return new ToggleOSCWrapper(index, name, button, parentActivity);
	}
	
	public static ToggleOSCWrapper createInstance(int index, String name, String msgToggleOn, String msgToggleOff, ToggleButton button, QuickOSCActivity parentActivity) {
		return new ToggleOSCWrapper(index, name, msgToggleOn, msgToggleOff, button, parentActivity);
	}
	
	private ToggleOSCWrapper(int index, String name, ToggleButton button, QuickOSCActivity parentActivity) {
		this.index = index;
		this.parentActivity = parentActivity;
		this.name = name;
		
		this.messageToggleOnAddr = "/" + name + "/1";
		this.messageToggleOnArgs = null;
		this.messageToggleOnRaw = this.messageToggleOnAddr;
		
		this.messageToggleOffAddr = "/" + name + "/0";
		this.messageToggleOffArgs = null;
		this.messageToggleOffRaw = this.messageToggleOffAddr;
		
		this.toggle = button;
		this.toggle.setOnCheckedChangeListener(this);
        this.toggle.setText(name);
        this.toggle.setTextOn(name);
        this.toggle.setTextOff(name);
	}
	
	private ToggleOSCWrapper(int index, String name, String msgToggleOn, String msgToggleOff, ToggleButton button, QuickOSCActivity parentActivity) {
		this.index = index;
		this.parentActivity = parentActivity;
		this.name = name;

		this.setMessageToggleOn(msgToggleOn);
		this.setMessageToggleOff(msgToggleOff);

        this.toggle = button;
        this.toggle.setOnCheckedChangeListener(this);
        this.toggle.setText(name);
        this.toggle.setTextOn(name);
        this.toggle.setTextOff(name);
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
			this.messageToggleOnAddr = "/" + name + "/1";
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
			this.messageToggleOffAddr = "/" + name + "/0";
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
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getIndex() {
		return this.index;
	}
	
}
