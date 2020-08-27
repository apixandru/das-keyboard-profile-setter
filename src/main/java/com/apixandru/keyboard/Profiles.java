package com.apixandru.keyboard;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.apixandru.keyboard.Utils.parseByteArrays;

class Profiles {

    static Map<String, List<byte[]>> readProfiles() throws URISyntaxException, IOException {
        Map<String, List<byte[]>> codes = new LinkedHashMap<>();

        URI uri = Profiles.class
                .getResource("/profiles")
                .toURI();

        List<Path> collect = Files.list(Paths.get(uri))
                .collect(Collectors.toList());

        for (Path profile : collect) {
            List<byte[]> strings = readBytes(profile);
            codes.put(profile.getFileName().toString(), strings);
        }
        return codes;
    }

    private static List<byte[]> readBytes(Path profile) throws IOException {
        List<String> strings = Files.readAllLines(profile);
        return parseByteArrays(strings);
    }

}
