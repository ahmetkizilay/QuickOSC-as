package com.ahmetkizilay.controls.osc;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.SeekBar;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by ahmetkizilay on 09.09.2014.
 */
public class QuickOSCSeekBarInstrumentTest  extends ActivityInstrumentationTestCase2<QuickOSCActivity> {

    public QuickOSCSeekBarInstrumentTest() {
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
     * set range of the seekbar to -100-100
     * set value to 0
     * expect seekbar progress to be 50
     * @throws Throwable
     */
    public void testSetButtonLabelWithOSC() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add(-100f);
        args.add(100f);

        final Collection<Object> args2 = new ArrayList<Object>();
        args2.add(0);



        new AsyncSendOSCTask(getActivity(), QuickOSCSeekBarInstrumentTest.this.mOscOut).execute(new OSCMessage("/sb1/range", args));
        new AsyncSendOSCTask(getActivity(), QuickOSCSeekBarInstrumentTest.this.mOscOut).execute(new OSCMessage("/sb1/value", args2));

        try {

            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SeekBar sb1 = (SeekBar) getActivity().findViewById(R.id.seekBar1);
        assertEquals("seekbar progress should be 50", 50, sb1.getProgress());
    }

    /**
     * set range of the seekbar to -150-600
     * set value to -200
     * expect seekbar progress to be 0
     * @throws Throwable
     */
    public void testSetSeekBarValueInsideTheRange() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add(-150f);
        args.add(600f);

        final Collection<Object> args2 = new ArrayList<Object>();
        args2.add(-200f);



        new AsyncSendOSCTask(getActivity(), QuickOSCSeekBarInstrumentTest.this.mOscOut).execute(new OSCMessage("/sb1/range", args));
        new AsyncSendOSCTask(getActivity(), QuickOSCSeekBarInstrumentTest.this.mOscOut).execute(new OSCMessage("/sb1/value", args2));

        try {

            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SeekBar sb1 = (SeekBar) getActivity().findViewById(R.id.seekBar1);
        assertEquals("seekbar progress should be 0", 0, sb1.getProgress());
    }

    /**
     * set range of the seekbar to -150-600
     * set value to 700
     * expect seekbar progress to be 0
     * @throws Throwable
     */
    public void testSetSeekbarValueAboveTheRange() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add(-150f);
        args.add(600f);

        final Collection<Object> args2 = new ArrayList<Object>();
        args2.add(700f);



        new AsyncSendOSCTask(getActivity(), QuickOSCSeekBarInstrumentTest.this.mOscOut).execute(new OSCMessage("/sb1/range", args));
        new AsyncSendOSCTask(getActivity(), QuickOSCSeekBarInstrumentTest.this.mOscOut).execute(new OSCMessage("/sb1/value", args2));

        try {

            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SeekBar sb1 = (SeekBar) getActivity().findViewById(R.id.seekBar1);
        assertEquals("seekbar progress should be 100", 100, sb1.getProgress());
    }


}
