package com.apixandru.keyboard.hid;

import com.apixandru.keyboard.ActualDevice;
import org.hid4java.HidDevice;

import java.io.Closeable;

final class ActualDeviceHid implements ActualDevice, Closeable {

    private final HidDevice hidDevice;

    ActualDeviceHid(HidDevice hidDevice) {
        this.hidDevice = hidDevice;
    }

    @Override
    public void write(byte[] message) {
        int writtenBytes = hidDevice.write(message, message.length, (byte) 0);
        int expectedWritten = message.length + 1;
        if (writtenBytes != expectedWritten) {
            throw new IllegalStateException("Expected " + expectedWritten + " but got " + writtenBytes + ".");
        }
    }

    @Override
    public void close() {
        hidDevice.close();
    }

    @Override
    public void open() {
        hidDevice.open();
    }

    @Override
    public String getDescriptor() {
        return hidDevice.getPath();
    }

}
