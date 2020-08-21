package com.apixandru.keyboard;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.apixandru.keyboard.Utils.parseByteArrays;

public class Main {

    private static final List<String> YES_ANSWERS = Arrays.asList("Y", "y", "");
    private static final List<String> NO_ANSWERS = Arrays.asList("n", "N", "NO", "no");

    public static void main(String[] args) throws InterruptedException, URISyntaxException, IOException {
        HidServices hidServices = HidManager.getHidServices();
        List<HidDevice> attachedHidDevices = hidServices.getAttachedHidDevices();
        for (HidDevice attachedHidDevice : attachedHidDevices) {
            if (attachedHidDevice.getProductId() == 0x202B && attachedHidDevice.getInterfaceNumber() == 2) {
                attachedHidDevice.open();

                URI uri = Main.class
                        .getResource("/profiles")
                        .toURI();
                Path profiles = Paths.get(uri);
                List<Path> collect = Files.list(profiles)
                        .collect(Collectors.toList());

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                for (Path profile : collect) {
                    while (true) {
                        System.out.print("Continue with " + profile.getFileName() + " ? ");
                        String s = br.readLine();
                        if (NO_ANSWERS.contains(s)) {
                            return;
                        } else if (YES_ANSWERS.contains(s)) {
                            break;
                        }
                    }
                    List<byte[]> strings = readBytes(profile);
                    for (byte[] sequence : strings) {
                        int write = writeMessage(attachedHidDevice, sequence);
                        if (write != 65) {
                            System.err.println("Expected 65 but got " + write + ". I have no idea what this means.");
                        }
                        Thread.sleep(50);
                    }
                }
            }
        }
    }

    private static List<byte[]> readBytes(Path profile) throws IOException {
        List<String> strings = Files.readAllLines(profile);
        strings.removeIf(next -> next.endsWith("."));
        return parseByteArrays(strings);
    }

    private static int writeMessage(HidDevice attachedHidDevice, byte[] message) {
        return attachedHidDevice.write(message, message.length, (byte) 0);
    }

}
