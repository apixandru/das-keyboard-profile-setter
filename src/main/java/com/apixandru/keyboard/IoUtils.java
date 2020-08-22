package com.apixandru.keyboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

class IoUtils {

    private static final List<String> YES_ANSWERS = Arrays.asList("Y", "y", "");
    private static final List<String> NO_ANSWERS = Arrays.asList("n", "N", "NO", "no");

    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static boolean readYesOrNo(String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String inputLine = br.readLine();
            if (NO_ANSWERS.contains(inputLine)) {
                return false;
            } else if (YES_ANSWERS.contains(inputLine)) {
                return true;
            }
        }
    }

}
