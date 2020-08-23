package com.apixandru.keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Utils {

    private static byte hexStringToByteArray(String hexByteString) {
        if (hexByteString == null || !hexByteString.startsWith("0x")) {
            throw new IllegalArgumentException(hexByteString);
        }
        String hexWithout0x = hexByteString.substring(2);
        byte parsedValue = (byte) Integer.parseInt(hexWithout0x, 16);
        String backToHex = String.format("%02x", parsedValue);
        if (!hexWithout0x.equals(backToHex)) {
            throw new IllegalArgumentException("Failed to convert " + hexByteString);
        }
        return parsedValue;
    }

    private static byte[] parseHexBytes(List<String> hexLines) {
        String[] hexStrings = hexLines.stream()
                .map(line -> line.split(" "))
                .flatMap(Arrays::stream)
                .toArray(String[]::new);

        byte[] bytes = new byte[hexStrings.length];
        for (int i = 0; i < hexStrings.length; i++) {
            bytes[i] = hexStringToByteArray(hexStrings[i]);
        }
        return bytes;
    }

    public static List<byte[]> parseByteArrays(List<String> strings) {
        List<byte[]> packages = new ArrayList<>();
        List<String> buffer = new ArrayList<>();
        for (String string : strings) {
            if (string.isEmpty()) {
                packages.add(parseHexBytes(buffer));
                buffer = new ArrayList<>();
            } else {
                buffer.add(string);
            }
        }
        return packages;
    }

}
