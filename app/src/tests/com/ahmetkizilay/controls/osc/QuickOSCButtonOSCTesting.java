package com.ahmetkizilay.controls.osc;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.ahmetkizilay.controls.osc.mock.QuickOSCMockContext;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.io.IOException;
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

        QuickOSCMockContext mockContext = new QuickOSCMockContext(getInstrumentation().getTargetContext());
        mockContext.setFixtureName("btn1_fixtures.txt");
        setActivityContext(mockContext);
        startActivity(new Intent(), null, null);

        getInstrumentation().callActivityOnStart(getActivity());
        getInstrumentation().callActivityOnResume(getActivity());


        if(mOscOut != null) {
            mOscOut.close();
            mOscOut = null;
        }

        mOscOut = new OSCPortOut(InetAddress.getByName("192.168.56.101"), 8090);
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
    public void testSettingMsgButtonPressedValueWithOSC() throws IOException {
        ButtonOSCWrapper btn = getActivity().getButtonWrappers().get(0);
        assertEquals("Initially btn should be equal to the value in fixture", "/btn1/1", btn.getMessageButtonPressedRaw());

        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add("/hello");
        args.add("world");

        mOscOut.send(new OSCMessage("/btn1/msgButtonPressed", args));
        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("btn should have a new btnPressed value", "/hello world", btn.getMessageButtonPressedRaw());
    }

    /**
     * Testing /btnN/msgButtonReleased
     */
    public void testSettingMsgButtonReleasedValueWithOSC() throws IOException {
        ButtonOSCWrapper btn = getActivity().getButtonWrappers().get(0);
        assertEquals("btn msgButtonReleased should come from the config", "/btn1/rel1", btn.getMessageButtonReleasedRaw());

        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add("/hello");
        args.add("world");


        mOscOut.send(new OSCMessage("/btn1/msgButtonReleased", args));
        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("new msgButtonReleased should come from OSC", "/hello world", btn.getMessageButtonReleasedRaw());
    }

    /**
     * Testing /btnN/triggerOnButtonReleased 1
     */
    public void testSettingTriggerOnButtonReleaseValueWithOSC() {
        ButtonOSCWrapper btn = getActivity().getButtonWrappers().get(0);
        assertFalse("btn triggerOnButtonReleased should be false at first", btn.getTriggerWhenButtonReleased());

        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add(1);

        new AsyncSendOSCTask(getActivity(), QuickOSCButtonOSCTesting.this.mOscOut).execute(new OSCMessage("/btn1/triggerOnButtonReleased", args));
        try {
            signal.await(2, TimeUnit.SECONDS);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        assertTrue("triggerOnButtonRelease should be true", btn.getTriggerWhenButtonReleased());
    }

    /**
     * Testing /btnN/triggerOnButtonReleased 1
     */
    public void testSettingTriggerOnButtonReleaseValueWithInvalidOSC() {
        ButtonOSCWrapper btn = getActivity().getButtonWrappers().get(0);
        assertFalse("btn triggerOnButtonReleased should be false at first", btn.getTriggerWhenButtonReleased());

        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add(4);

        new AsyncSendOSCTask(getActivity(), QuickOSCButtonOSCTesting.this.mOscOut).execute(new OSCMessage("/btn1/triggerOnButtonReleased", args));
        try {
            signal.await(2, TimeUnit.SECONDS);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        assertFalse("triggerOnButtonRelease should be false", btn.getTriggerWhenButtonReleased());
    }
}
