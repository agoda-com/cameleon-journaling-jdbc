package com.agoda;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FileReaderUtil {

    /**
     * Reads a file from the resources folder and returns its content as a String.
     *
     * @param fileName The name of the file in the resources folder.
     * @return The content of the file as a String.
     * @throws IOException If the file cannot be read.
     */
    public static String readFileFromResources(String fileName) throws IOException {
        // Get the ClassLoader
        ClassLoader classLoader = FileReaderUtil.class.getClassLoader();

        // Load the file as an InputStream
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IOException("File not found: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            // Read the entire file into a single String
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}