package com.ahmetkizilay.controls.osc;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.ToggleButton;

import com.ahmetkizilay.controls.osc.mock.QuickOSCMockContext;

/**
 * Created by ahmetkizilay on 08.09.2014.
 */
public class QuickOSCActivityTesting extends ActivityUnitTestCase<QuickOSCActivity> {

    public QuickOSCActivityTesting() {
        super(QuickOSCActivity.class);
    }

    /**
     * Simply testing if the activity is defined
     */
    public void testQuickOSCActivityShouldExist() {
        startActivity(new Intent(getInstrumentation().getTargetContext(), QuickOSCActivity.class),
            null, null);

        assertNotNull("QuickOSCActivity should be defined", getActivity());
    }

    /**
     * Checking if all widgets are defined on screen.
     */
    public void testQuickOSCActivityWidgets() {
        startActivity(new Intent(getInstrumentation().getTargetContext(), QuickOSCActivity.class),
                null, null);

        assertNotNull("QuickOSCActivity should be defined", getActivity());

        assertNotNull("btn1 should be defined" , getActivity().findViewById(R.id.button1));
        assertNotNull("btn2 should be defined" , getActivity().findViewById(R.id.button2));
        assertNotNull("btn3 should be defined" , getActivity().findViewById(R.id.button3));
        assertNotNull("btn4 should be defined" , getActivity().findViewById(R.id.button4));
        assertNotNull("btn5 should be defined" , getActivity().findViewById(R.id.button5));
        assertNotNull("btn6 should be defined" , getActivity().findViewById(R.id.button6));
        assertNotNull("btn7 should be defined" , getActivity().findViewById(R.id.button7));
        assertNotNull("btn8 should be defined" , getActivity().findViewById(R.id.button8));
        assertNotNull("btn9 should be defined" , getActivity().findViewById(R.id.button9));
        assertNotNull("btn10 should be defined", getActivity().findViewById(R.id.button10));
        assertNotNull("btn11 should be defined", getActivity().findViewById(R.id.button11));
        assertNotNull("btn12 should be defined", getActivity().findViewById(R.id.button12));
        assertNotNull("btn13 should be defined", getActivity().findViewById(R.id.button13));
        assertNotNull("btn14 should be defined", getActivity().findViewById(R.id.button14));
        assertNotNull("btn15 should be defined", getActivity().findViewById(R.id.button15));
        assertNotNull("btn16 should be defined", getActivity().findViewById(R.id.button16));

        assertNotNull("toggle1 should be defined" , getActivity().findViewById(R.id.toggleButton1));
        assertNotNull("toggle2 should be defined" , getActivity().findViewById(R.id.toggleButton2));
        assertNotNull("toggle3 should be defined" , getActivity().findViewById(R.id.toggleButton3));
        assertNotNull("toggle4 should be defined" , getActivity().findViewById(R.id.toggleButton4));
        assertNotNull("toggle5 should be defined" , getActivity().findViewById(R.id.toggleButton5));
        assertNotNull("toggle6 should be defined" , getActivity().findViewById(R.id.toggleButton6));
        assertNotNull("toggle7 should be defined" , getActivity().findViewById(R.id.toggleButton7));
        assertNotNull("toggle8 should be defined" , getActivity().findViewById(R.id.toggleButton8));

        assertNotNull("seekBar1 should be defined" , getActivity().findViewById(R.id.seekBar1));
        assertNotNull("seekBar2 should be defined" , getActivity().findViewById(R.id.seekBar2));
        assertNotNull("seekBar3 should be defined" , getActivity().findViewById(R.id.seekBar3));
        assertNotNull("seekBar4 should be defined" , getActivity().findViewById(R.id.seekBar4));
    }

    /**
     * Using a mock config file to check if button labels are set from the config
     * file.
     */
    public void testButtonLabelsShouldComeFromConfigFile() {
        setActivityContext(new QuickOSCMockContext(getInstrumentation().getTargetContext()));
        startActivity(new Intent(), null, null);

        assertNotNull("QuickOSCActivity shold not be null", getActivity());

        Button btn1 = (Button) getActivity().findViewById(R.id.button1);
        assertEquals("btn1 text is wrong", "btn_test1", btn1.getText().toString());

        Button btn2 = (Button) getActivity().findViewById(R.id.button2);
        assertEquals("btn2 text is wrong", "btn_test2", btn2.getText().toString());

        Button btn3 = (Button) getActivity().findViewById(R.id.button3);
        assertEquals("btn3 text is wrong", "btn_test3", btn3.getText().toString());

        Button btn4 = (Button) getActivity().findViewById(R.id.button4);
        assertEquals("btn4 text is wrong", "btn_test4", btn4.getText().toString());

        Button btn5 = (Button) getActivity().findViewById(R.id.button5);
        assertEquals("btn5 text is wrong", "btn_test5", btn5.getText().toString());

        Button btn6 = (Button) getActivity().findViewById(R.id.button6);
        assertEquals("btn6 text is wrong", "btn_test6", btn6.getText().toString());

        Button btn7 = (Button) getActivity().findViewById(R.id.button7);
        assertEquals("btn7 text is wrong", "btn_test7", btn7.getText().toString());

        Button btn8 = (Button) getActivity().findViewById(R.id.button8);
        assertEquals("btn8 text is wrong", "btn_test8", btn8.getText().toString());

        Button btn9 = (Button) getActivity().findViewById(R.id.button9);
        assertEquals("btn9 text is wrong", "btn_test9", btn9.getText().toString());

        Button btn10 = (Button) getActivity().findViewById(R.id.button10);
        assertEquals("btn10 text is wrong", "btn_test10", btn10.getText().toString());

        Button btn11 = (Button) getActivity().findViewById(R.id.button11);
        assertEquals("btn11 text is wrong", "btn_test11", btn11.getText().toString());

        Button btn12 = (Button) getActivity().findViewById(R.id.button12);
        assertEquals("btn12 text is wrong", "btn_test12", btn12.getText().toString());

        Button btn13 = (Button) getActivity().findViewById(R.id.button13);
        assertEquals("btn13 text is wrong", "btn_test13", btn13.getText().toString());

        Button btn14 = (Button) getActivity().findViewById(R.id.button14);
        assertEquals("btn14 text is wrong", "btn_test14", btn14.getText().toString());

        Button btn15 = (Button) getActivity().findViewById(R.id.button15);
        assertEquals("btn15 text is wrong", "btn_test15", btn15.getText().toString());

        Button btn16 = (Button) getActivity().findViewById(R.id.button16);
        assertEquals("btn16 text is wrong", "btn_test16", btn16.getText().toString());
    }

    /**
     * Using a mock config file to check if all toggle labels are set by the config file.
     */
    public void testToggleLabelsShouldComeFromConfigFile() {
        setActivityContext(new QuickOSCMockContext(getInstrumentation().getTargetContext()));
        startActivity(new Intent(), null, null);

        assertNotNull("QuickOSCActivity shold not be null", getActivity());

        ToggleButton tog1 = (ToggleButton) getActivity().findViewById(R.id.toggleButton1);
        assertEquals("tog1 on label is wrong", "tog_on1", tog1.getTextOn().toString());
        assertEquals("tog1 off label is wrong", "tog_off1", tog1.getTextOff().toString());

        ToggleButton tog2 = (ToggleButton) getActivity().findViewById(R.id.toggleButton2);
        assertEquals("tog2 on label is wrong", "tog_on2", tog2.getTextOn().toString());
        assertEquals("tog2 off label is wrong", "tog_off2", tog2.getTextOff().toString());

        ToggleButton tog3 = (ToggleButton) getActivity().findViewById(R.id.toggleButton3);
        assertEquals("tog3 on label is wrong", "tog_on3", tog3.getTextOn().toString());
        assertEquals("tog3 off label is wrong", "tog_off3", tog3.getTextOff().toString());

        ToggleButton tog4 = (ToggleButton) getActivity().findViewById(R.id.toggleButton4);
        assertEquals("tog4 on label is wrong", "tog_on4", tog4.getTextOn().toString());
        assertEquals("tog4 off label is wrong", "tog_off4", tog4.getTextOff().toString());

        ToggleButton tog5 = (ToggleButton) getActivity().findViewById(R.id.toggleButton5);
        assertEquals("tog5 on label is wrong", "tog_on5", tog5.getTextOn().toString());
        assertEquals("tog5 off label is wrong", "tog_off5", tog5.getTextOff().toString());

        ToggleButton tog6 = (ToggleButton) getActivity().findViewById(R.id.toggleButton6);
        assertEquals("tog6 on label is wrong", "tog_on6", tog6.getTextOn().toString());
        assertEquals("tog6 off label is wrong", "tog_off6", tog6.getTextOff().toString());

        ToggleButton tog7 = (ToggleButton) getActivity().findViewById(R.id.toggleButton7);
        assertEquals("tog7 on label is wrong", "tog_on7", tog7.getTextOn().toString());
        assertEquals("tog7 off label is wrong", "tog_off7", tog7.getTextOff().toString());

        ToggleButton tog8 = (ToggleButton) getActivity().findViewById(R.id.toggleButton8);
        assertEquals("tog8 on label is wrong", "tog_on8", tog8.getTextOn().toString());
        assertEquals("tog8 off label is wrong", "tog_off8", tog8.getTextOff().toString());
    }
}
