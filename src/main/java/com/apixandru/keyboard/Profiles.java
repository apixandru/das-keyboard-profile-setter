package com.apixandru.keyboard;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.toList;

class Profiles {

    static Map<String, byte[]> readProfiles() throws URISyntaxException, IOException {
        URI uri = Profiles.class
                .getResource("/profiles")
                .toURI();

        List<Path> profiles = Files.list(Paths.get(uri))
                .collect(toList());

        Map<String, byte[]> bytesForProfiles = new TreeMap<>();
        Base64.Decoder decoder = Base64.getDecoder();
        for (Path profile : profiles) {
            String base64Encoded = readString(profile);
            byte[] strings = decoder.decode(base64Encoded);
            bytesForProfiles.put(profile.getFileName().toString(), strings);
        }
        return bytesForProfiles;
    }

    private static String readString(Path profile) throws IOException {
        return new String(Files.readAllBytes(profile));
    }

}
