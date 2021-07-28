package com.mapsoft.sh;

/**
 * author: cx
 * created on: 2018/8/10 001015:34
 * packagename: com.mapsoft.lcd.util
 * projectname: LCDApplication
 * description:
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public final class IOUtil {
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private IOUtil() {
    }

    public static void shutdownReader(Reader input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException var2) {
            }

        }
    }

    public static void shutdownWriter(Writer output) {
        if (output != null) {
            try {
                output.close();
            } catch (IOException var2) {
            }

        }
    }

    public static void shutdownStream(OutputStream output) {
        if (output != null) {
            try {
                output.close();
            } catch (IOException var2) {
            }

        }
    }

    public static void shutdownStream(Closeable input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException var2) {
            }

        }
    }

    public static void copy(String input, String output) throws IOException {
        copy(new File(input), new File(output));
    }

    public static void copy(File f, File t) throws IOException {
        File dir = t.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (t.exists()) {
            t.delete();
        }
        t.createNewFile();
        InputStream ins = new FileInputStream(f);
        OutputStream ous = new FileOutputStream(t);
        copy(ins, ous);
        shutdownStream(ins);
        shutdownStream(ous);
    }

    public static void copy(InputStream input, OutputStream output) throws IOException {
        copy(input, output, 4096);
    }

    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        boolean var4 = false;

        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }

    }

    public static void copy(Reader input, Writer output) throws IOException {
        copy(input, output, 4096);
    }

    public static void copy(Reader input, Writer output, int bufferSize) throws IOException {
        char[] buffer = new char[bufferSize];
        boolean var4 = false;

        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }

    }

    public static void copy(InputStream input, Writer output) throws IOException {
        copy(input, output, 4096);
    }

    public static void copy(InputStream input, Writer output, int bufferSize) throws IOException {
        InputStreamReader in = new InputStreamReader(input);
        copy(in, output, bufferSize);
    }

    public static void copy(InputStream input, Writer output, String encoding) throws IOException {
        InputStreamReader in = new InputStreamReader(input, encoding);
        copy(in, output);
    }

    public static void copy(InputStream input, Writer output, String encoding, int bufferSize) throws IOException {
        InputStreamReader in = new InputStreamReader(input, encoding);
        copy(in, output, bufferSize);
    }

    public static String toString(InputStream input) throws IOException {
        return toString(input, 4096);
    }

    public static String toString(InputStream input, int bufferSize) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw, bufferSize);
        return sw.toString();
    }

    public static String toString(InputStream input, String encoding) throws IOException {
        return toString(input, encoding, 4096);
    }

    public static String toString(InputStream input, String encoding, int bufferSize) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw, encoding, bufferSize);
        return sw.toString();
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        return toByteArray(input, 4096);
    }

    public static byte[] toByteArray(InputStream input, int bufferSize) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output, bufferSize);
        return output.toByteArray();
    }

    public static void copy(Reader input, OutputStream output) throws IOException {
        copy(input, output, 4096);
    }

    public static void copy(Reader input, OutputStream output, int bufferSize) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output);
        copy(input, out, bufferSize);
        out.flush();
    }

    public static String toString(Reader input) throws IOException {
        return toString(input, 4096);
    }

    public static String toString(Reader input, int bufferSize) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw, bufferSize);
        return sw.toString();
    }

    public static byte[] toByteArray(Reader input) throws IOException {
        return toByteArray(input, 4096);
    }

    public static byte[] toByteArray(Reader input, int bufferSize) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output, bufferSize);
        return output.toByteArray();
    }

    public static void copy(String input, OutputStream output) throws IOException {
        copy(input, output, 4096);
    }

    public static void copy(String input, OutputStream output, int bufferSize) throws IOException {
        StringReader in = new StringReader(input);
        OutputStreamWriter out = new OutputStreamWriter(output);
        copy(in, out, bufferSize);
        out.flush();
    }

    public static void copy(String input, Writer output) throws IOException {
        output.write(input);
    }

    /**
     * @deprecated
     */
    public static void bufferedCopy(InputStream input, OutputStream output) throws IOException {
        BufferedInputStream in = new BufferedInputStream(input);
        BufferedOutputStream out = new BufferedOutputStream(output);
        copy(in, out);
        out.flush();
    }

    public static byte[] toByteArray(String input) throws IOException {
        return toByteArray(input, 4096);
    }

    public static byte[] toByteArray(String input, int bufferSize) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output, bufferSize);
        return output.toByteArray();
    }

    public static void copy(byte[] input, Writer output) throws IOException {
        copy(input, output, 4096);
    }

    public static void copy(byte[] input, Writer output, int bufferSize) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        copy(in, output, bufferSize);
    }

    public static void copy(byte[] input, Writer output, String encoding) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        copy(in, output, encoding);
    }

    public static void copy(byte[] input, Writer output, String encoding, int bufferSize) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        copy(in, output, encoding, bufferSize);
    }

    public static String toString(byte[] input) throws IOException {
        return toString(input, 4096);
    }

    public static String toString(byte[] input, int bufferSize) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw, bufferSize);
        return sw.toString();
    }

    public static String toString(byte[] input, String encoding) throws IOException {
        return toString(input, encoding, 4096);
    }

    public static String toString(byte[] input, String encoding, int bufferSize) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw, encoding, bufferSize);
        return sw.toString();
    }

    public static void copy(byte[] input, OutputStream output) throws IOException {
        copy(input, output, 4096);
    }

    public static void copy(byte[] input, OutputStream output, int bufferSize) throws IOException {
        output.write(input);
    }

    public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
        InputStream bufferedInput1 = new BufferedInputStream(input1);
        InputStream bufferedInput2 = new BufferedInputStream(input2);

        int ch2;
        for (int ch = bufferedInput1.read(); -1 != ch; ch = bufferedInput1.read()) {
            ch2 = bufferedInput2.read();
            if (ch != ch2) {
                return false;
            }
        }

        ch2 = bufferedInput2.read();
        return -1 == ch2;
    }
}
