package com.ahmetkizilay.controls.osc.mock;

import android.content.Context;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ahmetkizilay on 08.09.2014.
 */
public class QuickOSCMockContext extends RenamingDelegatingContext {

    private final static String OSC_SETTINGS_FILE = "qosc_osc.cfg";
    private String mFixtureName;

    public QuickOSCMockContext(Context context) {
        super(context, "com.ahmetkizily.controls.osc");
        this.mFixtureName = "osc_settings_fixture.txt";
    }

    @Override
    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        if(name == OSC_SETTINGS_FILE) {
            try {
                return new FixtureFromAssetsFileInputStream(getResources().getAssets().open(this.mFixtureName));
            }
            catch(IOException io){
                throw new FileNotFoundException("Unable to Open Fixture File");
            }
        }

        return super.openFileInput(name);
    }

    public void setFixtureName(String fixtureName) {
        this.mFixtureName = fixtureName;
    }

    private class FixtureFromAssetsFileInputStream extends FileInputStream {
        InputStream is;

        public FixtureFromAssetsFileInputStream(InputStream is) {
            super(FileDescriptor.in);
            this.is = is;
        }


        @Override
        public int read() throws IOException {
            return is.read();
        }

        @Override
        public int read(byte[] buffer) throws IOException {
            return is.read(buffer);
        }

        @Override
        public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            return is.read(buffer, byteOffset, byteCount);
        }

        @Override
        public void close() throws IOException {
            super.close();
            is.close();
        }
    }
}
