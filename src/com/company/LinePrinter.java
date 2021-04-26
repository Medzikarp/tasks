package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Prints line from file specified by line number.
 * First run creates an index (which takes a while) for
 * line offsets which optimizes subsequent examinations of a file.
 */
public class LinePrinter {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments.");
        }

        // Try to read args from CLI
        String fileNameToRead = args[0];
        Integer lineNumber = Integer.parseInt(args[1]);

        // Determine name of index file
        // If file to be scanned is named file.txt index will be named file_idx
        int lastDotIndex = fileNameToRead.lastIndexOf('.');
        String indexFileName = fileNameToRead.substring(0, lastDotIndex ) + "_idx";

        File indexFile = new File(indexFileName);
        File fileToRead = new File(fileNameToRead);


        if(!fileExists(fileToRead)) {
            // Nothing to read from
            throw new FileNotFoundException("Requested file doesn't exist.");
        }

        // Create index file if doesn't exist
        if(!fileExists(indexFile)) {
            createIndexFile(fileNameToRead, indexFile);
            indexFile = new File(indexFileName);
        }

        // Requested line number too big
        if (lineNumber > indexFile.length()/Integer.BYTES) {
            throw new IllegalArgumentException("Requested line number exceeds the actual number of lines.");
        }

        // Read offset of the line to be read from index file
        int lineByteOffset = readIntWithOffset(indexFile, lineNumber*Integer.BYTES);

        // Read the actual line
        String line = readLineWithOffset(fileToRead, lineByteOffset);

        // Print the line and exit
        System.out.println(line);

    }

    public static Boolean fileExists(File file) {
        if(!file.exists() || file.isDirectory()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static int readIntWithOffset(File indexFile, int bytesOffset) throws IOException {
        int intRead;
        try (RandomAccessFile file = new RandomAccessFile(indexFile, "r")) {
            file.seek(bytesOffset);
            intRead = file.readInt();
        }
        return intRead;
    }


    public static String readLineWithOffset(File fileToRead, int bytesOffset) throws IOException {
        String lineRead;
        try (RandomAccessFile file = new RandomAccessFile(fileToRead, "r")) {
            file.seek(bytesOffset);
            lineRead = file.readLine();
        }
        return lineRead;
    }

    public static void createIndexFile(String fileNameToRead, File indexFile) throws IOException {


        // Naive estimation :-)
        int lineSeparatorSize = System.getProperty("line.separator").length();

        // Open buffered reader for reading - it will read a chunk of bytes
        // Also open stream for writing to index file
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileNameToRead));
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(indexFile)))) {
            int bytesRead = 0;
            // Read each line until the end of a file
            for (String line; (line = br.readLine()) != null;) {
                // Write position of first byte for line
                out.writeInt(bytesRead);
                // Increment bytes read
                bytesRead += (line.getBytes().length + lineSeparatorSize);
            }
        }
    }


}
