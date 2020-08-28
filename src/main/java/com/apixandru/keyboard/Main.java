package com.apixandru.keyboard;

import java.util.Map;
import java.util.function.Consumer;

import static com.apixandru.keyboard.DasKeyboard.X50Q.findX50Q;
import static com.apixandru.keyboard.HidDevices.executeManaged;
import static com.apixandru.keyboard.IoUtils.readYesOrNo;
import static java.util.Arrays.copyOfRange;

public class Main {

    public static void main(String[] args) throws Exception {
        executeManaged(findX50Q(), Main::doApplyProfiles);
    }

    private static void doApplyProfiles(Consumer<byte[]> device) throws Exception {
        Map<String, byte[]> profiles = Profiles.readProfiles();
        for (Map.Entry<String, byte[]> profile : profiles.entrySet()) {
            if (!readYesOrNo("Continue with " + profile.getKey() + " ? ")) {
                return;
            }
            byte[] value = profile.getValue();
            for (int i = 0; i < 25; i++) {
                int startSequence = i * 64;
                int endSequence = startSequence + 64;
                device.accept(copyOfRange(value, startSequence, endSequence));
                Thread.sleep(50);
            }
        }
    }

}
