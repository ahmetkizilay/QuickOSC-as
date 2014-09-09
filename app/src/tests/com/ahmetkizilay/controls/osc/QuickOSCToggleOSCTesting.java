package com.ahmetkizilay.controls.osc;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.SeekBar;

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
public class QuickOSCToggleOSCTesting extends ActivityUnitTestCase<QuickOSCActivity> {

    public QuickOSCToggleOSCTesting() {
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
        mockContext.setFixtureName("tog1_fixtures.txt");
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
     * Testing /togN/labelOn
     */
    public void testSettingLabelOnWithOSC() throws IOException {
        ToggleOSCWrapper tog = getActivity().getToggleWrappers().get(0);
        assertEquals("Initially toggle on label should be set by config", "tog_on1", tog.getOnLabel());

        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add("hello");
        args.add("world");

        mOscOut.send(new OSCMessage("/tog1/labelOn", args));
        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("tog labelOn should be set by OSC", "hello world", tog.getOnLabel());
    }

    /**
     * Testing /togN/labelOff
     */
    public void testSettingLabelOffWithOSC() throws IOException {
        ToggleOSCWrapper tog = getActivity().getToggleWrappers().get(0);
        assertEquals("Initially toggle off label should be set by config", "tog_off1", tog.getOffLabel());

        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add("hello");
        args.add("world");

        mOscOut.send(new OSCMessage("/tog1/labelOff", args));
        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("tog labelOff should be set by OSC", "hello world", tog.getOffLabel());
    }

    /**
     * Testing /togN/msgToggledOn
     */
    public void testSettingToggledOnMsgWithOSC() throws IOException {
        ToggleOSCWrapper tog = getActivity().getToggleWrappers().get(0);
        assertEquals("Initially toggle msgToggledOn value should be set by config", "/tog1/1", tog.getMessageToggleOnRaw());

        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add("/hello");
        args.add("world");

        mOscOut.send(new OSCMessage("/tog1/msgToggledOn", args));
        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("tog msgToggledOn value should be set by OSC", "/hello world", tog.getMessageToggleOnRaw());
    }

    /**
     * Testing /togN/msgToggledOff
     */
    public void testSettingToggledOffMsgWithOSC() throws IOException {
        ToggleOSCWrapper tog = getActivity().getToggleWrappers().get(0);
        assertEquals("toggle msgToggledOff value should be set by config", "/tog1/0", tog.getMessageToggleOffRaw());

        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add("/hello");
        args.add("world");

        mOscOut.send(new OSCMessage("/tog1/msgToggledOff", args));
        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("tog msgToggledOff value should be set by OSC", "/hello world", tog.getMessageToggleOffRaw());
    }
}
