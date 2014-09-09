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
public class QuickOSCSeekBarOSCTesting extends ActivityUnitTestCase<QuickOSCActivity> {

    public QuickOSCSeekBarOSCTesting() {
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
        mockContext.setFixtureName("sb1_fixtures.txt");
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
     * Testing /sbN/range
     */
    public void testSettingRangeWithOSC() throws IOException {
        SeekBarOSCWrapper sb = getActivity().getSeekBarWrappers().get(0);
        assertEquals("Initial minValue of seekBar should be set by config", 25.0, sb.getMinValue(), 0.001);
        assertEquals("Initial maxValue of seekBar should be set by config", 50.0, sb.getMaxValue(), 0.001);

        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add(-100.0);
        args.add(100);

        mOscOut.send(new OSCMessage("/sb1/range", args));
        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Initial minValue of seekBar should be set by config", -100.0, sb.getMinValue(), 0.001);
        assertEquals("Initial maxValue of seekBar should be set by config", 100.0, sb.getMaxValue(), 0.001);
    }

    /**
     * Testing /sbN/msgValueChanged
     */
    public void testSettingMsgValueChangedWithOSC() throws IOException {
        SeekBarOSCWrapper sb = getActivity().getSeekBarWrappers().get(0);
        assertEquals("Initial msgValueChanged value of seekBar should be set by config", "/sb1/trig/$", sb.getMsgValueChanged());

        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add("/hello world $");

        mOscOut.send(new OSCMessage("/sb1/msgValueChanged", args));
        try {
            signal.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("msgValueChanged value of seekBar should be set by OSC", "/hello world $", sb.getMsgValueChanged());
    }
}
