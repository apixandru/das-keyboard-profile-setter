package com.apixandru.keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Utils {

    private static byte hexStringToByteArray(String hexString) {
        byte parsedValue = (byte) Integer.parseInt(hexString, 16);
        String backToHex = String.format("%02x", parsedValue);
        if (!hexString.equals(backToHex)) {
            throw new IllegalArgumentException("Failed to convert " + hexString);
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
