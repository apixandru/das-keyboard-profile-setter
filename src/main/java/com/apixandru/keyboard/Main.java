package com.apixandru.keyboard;

import com.apixandru.keyboard.usb.DeviceLookupUsb;

import java.util.Map;

import static com.apixandru.keyboard.IoUtils.readYesOrNo;
import static java.util.Arrays.copyOfRange;

public class Main {

    public static void main(String[] args) throws Exception {
//        HID doesn't work on linux, the interface is all funny so it gets rejected
//        usbhid 5-1.3:1.2: couldn't find an input interrupt endpoint
//        DeviceLookup lookup = new DeviceLookupHid();

//        direct usb works great!
        DeviceLookup lookup = new DeviceLookupUsb();
        ActualDevice device = lookup.findDevice(DasKeyboard.VENDOR_ID, DasKeyboard.X50Q.PRODUCT_ID);
        applyProfiles(device);
    }

    public static void doApplyProfiles(ActualDevice device) throws Exception {
        Map<String, byte[]> profiles = Profiles.readProfiles();
        for (Map.Entry<String, byte[]> profile : profiles.entrySet()) {
            if (!readYesOrNo("Continue with " + profile.getKey() + " ? ")) {
                return;
            }
            applyProfile(device, profile.getValue());
        }
    }

    public static void applyProfiles(ActualDevice device) throws Exception {
        System.out.println("Executing on device " + device.getDescriptor());
        device.open();
        try {
            doApplyProfiles(device);
        } finally {
            device.close();
        }
    }

    public static void applyProfile(ActualDevice device, byte[] profile) throws InterruptedException {
        for (int i = 0; i < 25; i++) {
            int startSequence = i * 64;
            int endSequence = startSequence + 64;
            byte[] bytes = copyOfRange(profile, startSequence, endSequence);
            device.write(bytes);
            Thread.sleep(50);
        }
    }

}
