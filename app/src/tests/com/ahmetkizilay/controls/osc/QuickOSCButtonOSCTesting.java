package com.ahmetkizilay.controls.osc;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

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
public class QuickOSCButtonOSCTesting extends ActivityUnitTestCase<QuickOSCActivity> {

    public QuickOSCButtonOSCTesting() {
        super(QuickOSCActivity.class);
    }

    OSCPortOut mOscOut;

    /**
     * Need to call resume since the OSC initialization occur in the resume method.
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        startActivity(new Intent(getInstrumentation().getTargetContext(), QuickOSCActivity.class), null, null);
        getInstrumentation().callActivityOnStart(getActivity());
        getInstrumentation().callActivityOnResume(getActivity());


        if(mOscOut != null) {
            mOscOut.close();
            mOscOut = null;
        }

        mOscOut = new OSCPortOut(InetAddress.getByName("192.168.56.101"), 8092);
    }

    /**
     * need to call pause and stop before teardown to close OSC connections.
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        getInstrumentation().callActivityOnPause(getActivity());
        getInstrumentation().callActivityOnStop(getActivity());

        super.tearDown();

        if(mOscOut != null) {
            mOscOut.close();
        }
    }

    /**
     * Testing /btnN/msgButtonPressed
     */
    public void testSettingMsgButtonPressedValueWithOSC() {
        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add("/hello");
        args.add("world");

        new AsyncSendOSCTask(getActivity(), QuickOSCButtonOSCTesting.this.mOscOut).execute(new OSCMessage("/btn1/msgButtonPressed", args));

        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(getActivity().getButtonWrappers().get(0).getMessageButtonPressedRaw(), "/hello world");
    }

    /**
     * Testing /btnN/msgButtonReleased
     */
    public void testSettingMsgButtonReleasedValueWithOSC() {
        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add("/hello");
        args.add("world");

        new AsyncSendOSCTask(getActivity(), QuickOSCButtonOSCTesting.this.mOscOut).execute(new OSCMessage("/btn1/msgButtonReleased", args));

        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(getActivity().getButtonWrappers().get(0).getMessageButtonReleasedRaw(), "/hello world");
    }
}
