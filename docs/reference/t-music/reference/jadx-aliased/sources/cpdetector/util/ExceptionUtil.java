package cpdetector.util;

import cpdetector.p074io.MultiplexingOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
public final class ExceptionUtil {
    public static ExceptionUtil instance;

    public static class InputStreamTracer implements Runnable {
        public Charset m_charset;
        public String m_match;
        public boolean m_matched;
        public InputStream m_streamToTrace;

        public InputStreamTracer(InputStream inputStream, String str, Charset charset) {
            this.m_streamToTrace = inputStream;
            this.m_match = str;
            this.m_charset = charset;
        }

        public boolean isMatched() {
            return this.m_matched;
        }

        @Override // java.lang.Runnable
        public void run() {
            String readLine;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.m_streamToTrace, this.m_charset));
            do {
                try {
                    readLine = bufferedReader.readLine();
                    if (readLine != null && readLine.contains(this.m_match)) {
                        this.m_matched = true;
                        return;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } while (readLine != null);
        }
    }

    public static InputStream captureSystemErrForDebuggingPurposesOnly(boolean z) {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        System.setErr(new PrintStream(z ? new MultiplexingOutputStream(System.err, pipedOutputStream) : pipedOutputStream));
        return pipedInputStream;
    }

    public static InputStream captureSystemOutForDebuggingPurposesOnly(boolean z) {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        System.setOut(new PrintStream(z ? new MultiplexingOutputStream(System.out, pipedOutputStream) : pipedOutputStream));
        return pipedInputStream;
    }

    public static void dumpThreadStack(PrintStream printStream) {
        printStream.println(StringUtil.arrayToString(Thread.currentThread().getStackTrace(), "\n"));
    }

    public static InputStreamTracer findMatchInSystemErr(String str) {
        InputStreamTracer inputStreamTracer = new InputStreamTracer(captureSystemErrForDebuggingPurposesOnly(true), str, Charset.defaultCharset());
        Thread thread = new Thread(inputStreamTracer);
        thread.setDaemon(true);
        thread.start();
        return inputStreamTracer;
    }

    public static InputStreamTracer findMatchInSystemOut(String str) {
        InputStreamTracer inputStreamTracer = new InputStreamTracer(captureSystemOutForDebuggingPurposesOnly(true), str, Charset.defaultCharset());
        Thread thread = new Thread(inputStreamTracer);
        thread.setDaemon(true);
        thread.start();
        return inputStreamTracer;
    }

    public static ExceptionUtil instance() {
        if (instance == null) {
            instance = new ExceptionUtil();
        }
        return instance;
    }
}
