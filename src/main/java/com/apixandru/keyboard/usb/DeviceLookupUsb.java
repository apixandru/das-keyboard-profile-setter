package com.apixandru.keyboard.usb;

import com.apixandru.keyboard.ActualDevice;
import com.apixandru.keyboard.DeviceLookup;
import org.usb4java.javax.Services;

import javax.usb.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DeviceLookupUsb implements DeviceLookup {

    @SuppressWarnings("unchecked")
    private static UsbEndpoint findOutEndpoint(UsbDevice usbDevice) {
        List<UsbEndpoint> endpoints = new ArrayList<>();
        UsbConfiguration activeUsbConfiguration = usbDevice.getActiveUsbConfiguration();
        List<UsbInterface> usbInterfaces = activeUsbConfiguration.getUsbInterfaces();
        for (UsbInterface usbInterface : usbInterfaces) {
            List<UsbEndpoint> usbEndpoints = usbInterface.getUsbEndpoints();
            for (UsbEndpoint usbEndpoint : usbEndpoints) {
                if (usbEndpoint.getDirection() == UsbConst.ENDPOINT_DIRECTION_OUT) {
                    endpoints.add(usbEndpoint);
                }
            }
        }
        if (endpoints.size() > 1) {
            throw new IllegalStateException("Found more than one OUT endpoint.");
        } else if (endpoints.isEmpty()) {
            throw new IllegalStateException("Did not find any OUT endpoints.");
        }
        return endpoints.get(0);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static UsbDevice findDevice(UsbHub rootUsbHub, int vendorId, int productId) {
        List<UsbDevice> devices = new ArrayList<>();
        LinkedList<UsbDevice> queue = new LinkedList<>(rootUsbHub.getAttachedUsbDevices());
        while (!queue.isEmpty()) {
            UsbDevice usbDevice = queue.removeFirst();
            if (usbDevice instanceof UsbHub) {
                UsbHub hub = (UsbHub) usbDevice;
                queue.addAll(hub.getAttachedUsbDevices());
            }
            UsbDeviceDescriptor desc = usbDevice.getUsbDeviceDescriptor();
            if (desc.idVendor() == vendorId && desc.idProduct() == productId) {
                devices.add(usbDevice);
            }
        }
        if (devices.size() > 1) {
            throw new IllegalStateException("Found more than one device matching vendor/product.");
        } else if (devices.isEmpty()) {
            throw new IllegalStateException("Did not find any device matching vendor/product");
        }
        return devices.get(0);
    }

    @Override
    public ActualDevice findDevice(int vendorId, int productId) throws Exception {
        Services service = (org.usb4java.javax.Services) UsbHostManager.getUsbServices();
        UsbDevice device = findDevice(service.getRootUsbHub(), vendorId, productId);
        UsbEndpoint outEndpoint = findOutEndpoint(device);
        return new ActualDeviceUsb(outEndpoint);
    }

}
