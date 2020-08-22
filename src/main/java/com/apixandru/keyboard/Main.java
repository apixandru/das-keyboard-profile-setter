package com.apixandru.keyboard;

import org.hid4java.HidDevice;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static com.apixandru.keyboard.HidDevices.findDevice;
import static com.apixandru.keyboard.IoUtils.readYesOrNo;

public class Main {

    private static final int DAS_KEYBOARD_VENDOR_ID = 0x24f0;
    private static final int X50Q_PRODUCT_ID = 0x202b;

    public static void main(String[] args) throws InterruptedException, URISyntaxException, IOException {
        HidDevice device = findDevice(DAS_KEYBOARD_VENDOR_ID, X50Q_PRODUCT_ID, 2);
        System.out.println("Found device " + device.getPath());
        device.open();
        try {
            doApplyProfiles(device);
        } finally {
            device.close();
        }
    }

    private static void doApplyProfiles(HidDevice device) throws IOException, URISyntaxException, InterruptedException {
        Map<String, List<byte[]>> profiles = Profiles.readProfiles();
        for (Map.Entry<String, List<byte[]>> profile : profiles.entrySet()) {
            if (!readYesOrNo("Continue with " + profile.getKey() + " ? ")) {
                return;
            }
            for (byte[] sequence : profile.getValue()) {
                int write = writeMessage(device, sequence);
                if (write != 65) {
                    System.err.println("Expected 65 but got " + write + ". I have no idea what this means.");
                }
                Thread.sleep(50);
            }

        }
    }

    private static int writeMessage(HidDevice attachedHidDevice, byte[] message) {
        return attachedHidDevice.write(message, message.length, (byte) 0);
    }

}
