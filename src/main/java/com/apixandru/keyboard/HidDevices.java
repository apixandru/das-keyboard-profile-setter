package com.apixandru.keyboard;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;

import java.util.List;
import java.util.stream.Collectors;

class HidDevices {

    private static List<HidDevice> findDevices(int vendorId, int productId, int interfaceNumber) {
        HidServices hidServices = HidManager.getHidServices();
        return hidServices.getAttachedHidDevices()
                .stream()
                .filter(device -> vendorId == device.getVendorId())
                .filter(device -> productId == device.getProductId())
                .filter(device -> interfaceNumber == device.getInterfaceNumber())
                .collect(Collectors.toList());
    }

    private static HidDevice findDevice(int vendorId, int productId, int interfaceNumber) {
        List<HidDevice> devices = findDevices(vendorId, productId, interfaceNumber);
        if (devices.size() > 1) {
            throw new IllegalArgumentException("Too many devices match the specified criteria!");
        } else if (devices.isEmpty()) {
            throw new IllegalArgumentException("Cannot find the device!");
        }
        return devices.get(0);
    }

    public static void execute(HidDeviceIo action, int vendorId, int productId, int interfaceNumber) throws Exception {
        HidDevice device = findDevice(vendorId, productId, interfaceNumber);
        executeManaged(device, action);
    }

    private static void executeManaged(HidDevice device, HidDeviceIo action) throws Exception {
        System.out.println("Executing on device " + device.getPath());
        device.open();
        try {
            action.send(bytes -> writeMessage(device, bytes));
        } finally {
            device.close();
        }
    }

    private static void writeMessage(HidDevice device, byte[] message) {
        int writtenBytes = device.write(message, message.length, (byte) 0);
        int expectedWritten = message.length + 1;
        if (writtenBytes != expectedWritten) {
            throw new IllegalStateException("Expected " + expectedWritten + " but got " + writtenBytes + ".");
        }
    }

}
