package com.apixandru.keyboard;

import org.hid4java.HidDevice;

import static com.apixandru.keyboard.HidDevices.findDevice;

public final class DasKeyboard {

    public static final int VENDOR_ID = 0x24f0;

    public static final class X50Q {

        public static final int PRODUCT_ID = 0x202b;
        public static final int OUT_INTERFACE = 2;

        public static HidDevice findX50Q() {
            return findDevice(VENDOR_ID, PRODUCT_ID, OUT_INTERFACE);
        }

    }

}
