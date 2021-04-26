package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

/**
 * Generates file test.txt with 1e+10 lines of random strings of 10 chars.
 */
public class Generator {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        File file = new File("test.txt");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(writer);


        for (long length = 0; length <= 1e+10; length += 39) {
            out.write(generateRandomString());
            out.newLine();
        }
        out.flush();
        out.close();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000f + " seconds");
    }

    public static String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}

