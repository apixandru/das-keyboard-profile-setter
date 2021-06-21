package com.apixandru.keyboard.usb;

import com.apixandru.keyboard.ActualDevice;

import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;

public class ActualDeviceUsb implements ActualDevice, AutoCloseable {

    private final UsbPipe usbPipe;
    private final UsbInterface usbInterface;

    public ActualDeviceUsb(UsbEndpoint usbEndpoint) {
        this.usbInterface = usbEndpoint.getUsbInterface();
        this.usbPipe = usbEndpoint.getUsbPipe();
    }

    @Override
    public void write(byte[] message) {
        try {
            int writtenBytes = usbPipe.syncSubmit(message);
            int expectedWritten = message.length;
            if (writtenBytes != expectedWritten) {
                throw new IllegalStateException("Expected " + expectedWritten + " but got " + writtenBytes + ".");
            }
        } catch (UsbException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws Exception {
        usbPipe.close();
        usbInterface.release();
    }

    @Override
    public void open() throws Exception {
        usbInterface.claim();
        usbPipe.open();
    }

    @Override
    public String getDescriptor() {
        return usbPipe.getUsbEndpoint().getUsbInterface().toString();
    }

}
