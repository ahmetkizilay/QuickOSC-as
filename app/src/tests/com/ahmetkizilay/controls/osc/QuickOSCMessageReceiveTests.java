package com.ahmetkizilay.controls.osc;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.ToggleButton;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by ahmetkizilay on 08.09.2014.
 */
public class QuickOSCMessageReceiveTests  extends ActivityInstrumentationTestCase2<QuickOSCActivity> {

    public QuickOSCMessageReceiveTests() {
        super(QuickOSCActivity.class);
    }

    /**
     * This property is used in all tests to send OSC messages to the activity.
     */
    OSCPortOut mOscOut;

    /**
     * Initializing OSC out before each test.
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(false);

        if(mOscOut != null) {
            mOscOut.close();
            mOscOut = null;
        }

        mOscOut = new OSCPortOut(InetAddress.getByName("192.168.56.101"), 8092);
    }

    /**
     * Closing OSC Out at the end of each test
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if(mOscOut != null) {
            mOscOut.close();
        }
    }

    /**
     * Testing /btnN/label OSC message
     * @throws Throwable
     */
    public void testSetButtonLabelWithOSC() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add("hello");

        new AsyncSendOSCTask(getActivity(), QuickOSCMessageReceiveTests.this.mOscOut).execute(new OSCMessage("/btn1/label", args));

        try {

            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button btn1 = (Button) getActivity().findViewById(R.id.button1);
        assertEquals("btn1 text should be", "hello", btn1.getText().toString());
    }

    /**
     * Testing /togN/labelOff OSC message
     * @throws Throwable
     */
    public void testSetToggleOffLabelWithOSC() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add("hello");

        new AsyncSendOSCTask(getActivity(), QuickOSCMessageReceiveTests.this.mOscOut).execute(new OSCMessage("/tog1/labelOff", args));

        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ToggleButton btn1 = (ToggleButton) getActivity().findViewById(R.id.toggleButton1);
        assertEquals("tog1 off should be", "hello", btn1.getTextOff().toString());
    }

    /**
     * Testing /togN/labelOn OSC message
     * @throws Throwable
     */
    public void testSetToggleOnStateWithOSC() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add(1);

        new AsyncSendOSCTask(getActivity(), QuickOSCMessageReceiveTests.this.mOscOut).execute(new OSCMessage("/tog1/value", args));

        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ToggleButton btn1 = (ToggleButton) getActivity().findViewById(R.id.toggleButton1);
        assertTrue("tog1 should be on", btn1.isChecked());
    }

    /**
     * Testing /togN/value 0
     * @throws Throwable
     */
    public void testSetToggleStateOffWithOSC() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add(0);

        new AsyncSendOSCTask(getActivity(), QuickOSCMessageReceiveTests.this.mOscOut).execute(new OSCMessage("/tog1/value", args));

        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ToggleButton btn1 = (ToggleButton) getActivity().findViewById(R.id.toggleButton1);
        assertFalse("tog1 should be off", btn1.isChecked());
    }

    /**
     * Testing /togN/value 1 and /togN/value 0
     * @throws Throwable
     */
    public void testSetToggleStateOnAndOffWithOSC() throws Throwable {
        ToggleButton btn1 = (ToggleButton) getActivity().findViewById(R.id.toggleButton1);

        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add(1);

        new AsyncSendOSCTask(getActivity(), QuickOSCMessageReceiveTests.this.mOscOut).execute(new OSCMessage("/tog1/value", args));

        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue("tog1 should be on", btn1.isChecked());

        final CountDownLatch signal2 = new CountDownLatch(1);

        final Collection<Object> args2 = new ArrayList<Object>();
        args2.add(0);

        new AsyncSendOSCTask(getActivity(), QuickOSCMessageReceiveTests.this.mOscOut).execute(new OSCMessage("/tog1/value", args2));

        try {
            signal2.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse("tog1 should be off", btn1.isChecked());
    }
}
