package com.apixandru.keyboard;

import com.apixandru.keyboard.sequences.CyanSequences;
import com.apixandru.keyboard.sequences.GreenSequences;
import com.apixandru.keyboard.sequences.OrangeSequences;
import com.apixandru.keyboard.sequences.White1Sequences;
import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        HidServices hidServices = HidManager.getHidServices();
        List<HidDevice> attachedHidDevices = hidServices.getAttachedHidDevices();
        for (HidDevice attachedHidDevice : attachedHidDevices) {
            if (attachedHidDevice.getProductId() == 0x202B && attachedHidDevice.getInterfaceNumber() == 2) {
                System.out.println(attachedHidDevice);
                attachedHidDevice.open();

                try {

                    List<Class> colors = Arrays.asList(
                            OrangeSequences.class,
                            GreenSequences.class,
                            White1Sequences.class,
                            CyanSequences.class);

                    for (Class color : colors) {
                        for (int[] sequence : getFields(color)) {
                            int write = writeMessage(attachedHidDevice, asBytes(sequence));
                            System.out.println(write);
                            Thread.sleep(50);
                        }
                    }
                } finally {
                    attachedHidDevice.close();
                }

            }
        }
    }

    private static int writeMessage(HidDevice attachedHidDevice, byte[] message) {
        return attachedHidDevice.write(message, message.length, (byte) 0);
    }

    private static byte[] asBytes(int... vals) {
        int[] ints = vals;
        if (ints.length > 64) {
            ints = Arrays.copyOfRange(vals, vals.length - 64, vals.length);
        }

        byte[] bytes = new byte[64];
        for (int i = 0; i < ints.length; i++) {
            bytes[i] = (byte) ints[i];
        }
        return bytes;
    }

    public static List<int[]> getFields(Class<?> sequenceClass) {
        try {
            Field[] declaredFields = sequenceClass.getDeclaredFields();
            if (declaredFields.length != 25) {
                throw new IllegalArgumentException();
            }
            List<int[]> values = new ArrayList<>();
            for (Field field : declaredFields) {
                values.add((int[]) field.get(null));
            }
            return values;
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
