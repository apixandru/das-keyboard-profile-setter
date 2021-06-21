package com.apixandru.keyboard.hid;

import com.apixandru.keyboard.ActualDevice;
import com.apixandru.keyboard.DasKeyboard;
import com.apixandru.keyboard.DeviceLookup;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.jna.HidApi;
import org.hid4java.jna.HidDeviceInfoStructure;

import java.util.List;
import java.util.stream.Collectors;

public class DeviceLookupHid implements DeviceLookup {

    private static List<ActualDevice> findDevices(int vendorId, int productId, int interfaceNumber) {
        HidServices hidServices = HidManager.getHidServices();

        HidDeviceInfoStructure hidDeviceInfoStructure = HidApi.enumerateDevices(vendorId, productId);
        System.out.println(hidDeviceInfoStructure);
        hidServices.getAttachedHidDevices()
                .stream()
                .filter(device -> vendorId == device.getVendorId())
                .forEach(System.out::println);

        return hidServices.getAttachedHidDevices()
                .stream()
                .filter(device -> vendorId == device.getVendorId())
                .filter(device -> productId == device.getProductId())
                .filter(device -> interfaceNumber == device.getInterfaceNumber())
                .map(ActualDeviceHid::new)
                .collect(Collectors.toList());
    }

    @Override
    public ActualDevice findDevice(int vendorId, int productId) throws Exception {
        List<ActualDevice> devices = findDevices(vendorId, productId, DasKeyboard.X50Q.OUT_INTERFACE);
        if (devices.size() > 1) {
            throw new IllegalStateException("Found more than one device matching the description.");
        } else if (devices.isEmpty()) {
            throw new IllegalStateException("Did not find any devices matching the description.");
        }
        return devices.get(0);
    }

}
