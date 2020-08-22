package com.apixandru.keyboard;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;

import java.util.List;
import java.util.stream.Collectors;

class HidDevices {

    static List<HidDevice> findDevices(int vendorId, int productId, int interfaceNumber) {
        HidServices hidServices = HidManager.getHidServices();
        return hidServices.getAttachedHidDevices()
                .stream()
                .filter(device -> vendorId == device.getVendorId())
                .filter(device -> productId == device.getProductId())
                .filter(device -> interfaceNumber == device.getInterfaceNumber())
                .collect(Collectors.toList());
    }

    static HidDevice findDevice(int vendorId, int productId, int interfaceNumber) {
        List<HidDevice> devices = findDevices(vendorId, productId, interfaceNumber);
        if (devices.size() > 1) {
            throw new IllegalArgumentException("Too many devices match the specified criteria!");
        } else if (devices.isEmpty()) {
            throw new IllegalArgumentException("Cannot find the device!");
        }
        return devices.get(0);
    }

}
