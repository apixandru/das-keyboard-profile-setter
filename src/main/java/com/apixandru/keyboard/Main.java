package com.apixandru.keyboard;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.apixandru.keyboard.DasKeyboard.X50Q.findX50Q;
import static com.apixandru.keyboard.HidDevices.executeManaged;
import static com.apixandru.keyboard.IoUtils.readYesOrNo;

public class Main {


    public static void main(String[] args) throws Exception {
        executeManaged(findX50Q(), Main::doApplyProfiles);
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
