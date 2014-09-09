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
public class QuickOSCButtonOSCTesting2 extends ActivityUnitTestCase<QuickOSCActivity> {

    public QuickOSCButtonOSCTesting2() {
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
        mockContext.setFixtureName("btn1_fixtures2.txt");
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
     * Testing /btnN/triggerOnButtonReleased 0
     */
    public void testSettingTriggerOnButtonReleaseValueWithOSC() {
        ButtonOSCWrapper btn = getActivity().getButtonWrappers().get(0);
        assertTrue("btn triggerOnButtonReleased should be true at first", btn.getTriggerWhenButtonReleased());

        final CountDownLatch signal = new CountDownLatch(1);

        final Collection<Object> args = new ArrayList<Object>();
        args.add(0);

        new AsyncSendOSCTask(getActivity(), QuickOSCButtonOSCTesting2.this.mOscOut).execute(new OSCMessage("/btn1/triggerOnButtonReleased", args));
        try {
            signal.await(2, TimeUnit.SECONDS);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        assertFalse("triggerOnButtonRelease should be false", btn.getTriggerWhenButtonReleased());
    }
}
