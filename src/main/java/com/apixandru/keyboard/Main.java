package com.apixandru.keyboard;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.apixandru.keyboard.HidDevices.execute;
import static com.apixandru.keyboard.IoUtils.readYesOrNo;

public class Main {

    private static final int DAS_KEYBOARD_VENDOR_ID = 0x24f0;
    private static final int X50Q_PRODUCT_ID = 0x202b;

    public static void main(String[] args) throws Exception {
        execute(Main::doApplyProfiles, DAS_KEYBOARD_VENDOR_ID, X50Q_PRODUCT_ID, 2);
    }

    private static void doApplyProfiles(Consumer<byte[]> device) throws Exception {
        Map<String, List<byte[]>> profiles = Profiles.readProfiles();
        for (Map.Entry<String, List<byte[]>> profile : profiles.entrySet()) {
            if (!readYesOrNo("Continue with " + profile.getKey() + " ? ")) {
                return;
            }
            for (byte[] sequence : profile.getValue()) {
                device.accept(sequence);
                Thread.sleep(50);
            }
        }
    }

}
