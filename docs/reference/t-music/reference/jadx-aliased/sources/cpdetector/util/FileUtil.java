package cpdetector.util;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import p054a.p055a.p056a.p003a.C0000a;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes4.dex */
public final class FileUtil {
    public static FileUtil instance;
    public final ResourceBundle m_bundle = ResourceBundle.getBundle("messages");

    public static Map.Entry<String, String> cutDirectoryInformation(String str) {
        String absolutePath;
        StringBuffer stringBuffer = new StringBuffer();
        String property = System.getProperty("file.separator");
        StringTokenizer stringTokenizer = new StringTokenizer(str, property);
        int countTokens = stringTokenizer.countTokens();
        String str2 = "";
        if (countTokens != 0) {
            if (countTokens != 1) {
                while (stringTokenizer.hasMoreElements()) {
                    String nextToken = stringTokenizer.nextToken();
                    if (stringTokenizer.hasMoreTokens()) {
                        stringBuffer.append(nextToken);
                        stringBuffer.append(property);
                    } else if (new File(str).isFile()) {
                        str2 = nextToken;
                    } else {
                        stringBuffer.append(nextToken);
                    }
                }
            } else {
                File file = new File(stringTokenizer.nextToken());
                if (new File(str).isDirectory()) {
                    absolutePath = file.getAbsolutePath();
                } else {
                    stringBuffer.append(new File(".").getAbsolutePath());
                    str2 = str;
                }
            }
            return new Entry(stringBuffer.toString(), str2);
        }
        absolutePath = new File(".").getAbsolutePath();
        stringBuffer.append(absolutePath);
        return new Entry(stringBuffer.toString(), str2);
    }

    public static Map.Entry<String, String> cutDirectoryInformation(URL url) {
        StringBuffer stringBuffer = new StringBuffer();
        String externalForm = url.toExternalForm();
        String str = "";
        if (!externalForm.endsWith("/")) {
            StringTokenizer stringTokenizer = new StringTokenizer(url.getFile(), "/");
            stringBuffer.append(url.getProtocol());
            stringBuffer.append(":");
            stringBuffer.append(url.getHost());
            while (stringTokenizer.hasMoreElements()) {
                stringBuffer.append(str);
                str = stringTokenizer.nextToken();
                stringBuffer.append("/");
            }
            externalForm = stringBuffer.toString();
        }
        return new Entry(externalForm, str);
    }

    public static Map.Entry<String, String> cutExtension(String str) {
        String str2;
        StringTokenizer stringTokenizer = new StringTokenizer(str, ".");
        int countTokens = stringTokenizer.countTokens();
        if (countTokens > 1) {
            StringBuffer stringBuffer = new StringBuffer();
            while (countTokens > 1) {
                countTokens--;
                stringBuffer.append(stringTokenizer.nextToken());
                if (countTokens > 1) {
                    stringBuffer.append(".");
                }
            }
            str = stringBuffer.toString();
            str2 = stringTokenizer.nextToken();
        } else {
            str2 = "";
        }
        return new Entry(str, str2);
    }

    public static String getDefaultFileName(String str) {
        File file = new File(str);
        if (file.exists()) {
            Map.Entry<String, String> cutExtension = cutExtension(str);
            String key = cutExtension.getKey();
            String value = cutExtension.getValue();
            int i = 0;
            while (file.exists()) {
                file = new File(key + '_' + i + '.' + value);
                i++;
            }
        }
        return file.getAbsolutePath();
    }

    public static FileUtil getInstance() {
        if (instance == null) {
            instance = new FileUtil();
        }
        return instance;
    }

    public static boolean isAllASCII(File file) {
        return isAllASCII(new FileInputStream(file));
    }

    public static boolean isAllASCII(InputStream inputStream) {
        int read;
        do {
            read = inputStream.read();
            if (read > 127) {
                return false;
            }
        } while (read != -1);
        return true;
    }

    public static boolean isEqual(File file, Charset charset, Charset charset2) {
        Throwable th;
        InputStreamReader inputStreamReader;
        int read;
        boolean z;
        InputStreamReader inputStreamReader2 = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileInputStream fileInputStream2 = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream, charset);
            try {
                InputStreamReader inputStreamReader3 = new InputStreamReader(fileInputStream2, charset2);
                do {
                    try {
                        int read2 = inputStreamReader.read();
                        read = inputStreamReader3.read();
                        if (read2 == read) {
                            if (read2 == -1) {
                                break;
                            }
                        } else {
                            z = false;
                            break;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        inputStreamReader2 = inputStreamReader3;
                        if (inputStreamReader != null) {
                            inputStreamReader.close();
                        }
                        if (inputStreamReader2 == null) {
                            throw th;
                        }
                        inputStreamReader2.close();
                        throw th;
                    }
                } while (read != -1);
                z = true;
                inputStreamReader.close();
                inputStreamReader3.close();
                return z;
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Throwable th4) {
            th = th4;
            inputStreamReader = null;
        }
    }

    public static InputStream readCache(File file) {
        return new ByteArrayInputStream(readRAM(file));
    }

    public static byte[] readRAM(File file) {
        int length = (int) file.length();
        byte[] bArr = new byte[length];
        FileInputStream fileInputStream = new FileInputStream(file);
        int i = 0;
        int i2 = 0;
        do {
            try {
                i2 = fileInputStream.read(bArr, i, length - i2);
                if (i2 > 0) {
                    i += i2;
                }
                if (i2 == -1) {
                    break;
                }
            } finally {
                fileInputStream.close();
            }
        } while (i != length);
        return bArr;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:89:0x010e -> B:38:0x0111). Please report as a decompilation issue!!! */
    public static void removeDuplicateLineBreaks(File file) {
        BufferedInputStream bufferedInputStream;
        StringBuffer stringBuffer;
        FileWriter fileWriter;
        PrintStream printStream;
        StringBuilder m5a;
        String str;
        String newLine = StringUtil.getNewLine();
        if (!file.exists()) {
            printStream = System.err;
            m5a = C0000a.m5a("FileUtil.removeDuplicateLineBreak(File f): ");
            m5a.append(file.getAbsolutePath());
            str = " does not exist!";
        } else {
            if (!file.isDirectory()) {
                FileWriter fileWriter2 = null;
                try {
                    try {
                        try {
                            bufferedInputStream = new BufferedInputStream(new FileInputStream(file), 1024);
                            try {
                                StringBuffer stringBuffer2 = new StringBuffer();
                                while (true) {
                                    int read = bufferedInputStream.read();
                                    if (read == -1) {
                                        break;
                                    } else {
                                        stringBuffer2.append((char) read);
                                    }
                                }
                                StringTokenizer stringTokenizer = new StringTokenizer(stringBuffer2.toString(), newLine, true);
                                stringBuffer = new StringBuffer();
                                int i = 0;
                                while (stringTokenizer.hasMoreTokens()) {
                                    String trim = stringTokenizer.nextToken().trim();
                                    if (!trim.equals("") || i <= 0) {
                                        if (trim.equals("")) {
                                            i++;
                                            trim = newLine;
                                        } else {
                                            i = 0;
                                        }
                                        stringBuffer.append(trim);
                                    } else {
                                        i++;
                                    }
                                }
                                file.delete();
                                file.createNewFile();
                                fileWriter = new FileWriter(file);
                            } catch (FileNotFoundException unused) {
                            } catch (IOException e) {
                                e = e;
                            }
                        } catch (Throwable th) {
                            th = th;
                        }
                    } catch (FileNotFoundException unused2) {
                        bufferedInputStream = null;
                    } catch (IOException e2) {
                        e = e2;
                        bufferedInputStream = null;
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedInputStream = null;
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                try {
                    fileWriter.write(stringBuffer.toString());
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    fileWriter.flush();
                    fileWriter.close();
                } catch (FileNotFoundException unused3) {
                    fileWriter2 = fileWriter;
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                        }
                    }
                    if (fileWriter2 != null) {
                        fileWriter2.flush();
                        fileWriter2.close();
                        return;
                    }
                    return;
                } catch (IOException e6) {
                    e = e6;
                    fileWriter2 = fileWriter;
                    e.printStackTrace(System.err);
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (IOException e7) {
                            e7.printStackTrace();
                        }
                    }
                    if (fileWriter2 != null) {
                        fileWriter2.flush();
                        fileWriter2.close();
                        return;
                    }
                    return;
                } catch (Throwable th3) {
                    th = th3;
                    fileWriter2 = fileWriter;
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (IOException e8) {
                            e8.printStackTrace();
                        }
                    }
                    if (fileWriter2 == null) {
                        throw th;
                    }
                    try {
                        fileWriter2.flush();
                        fileWriter2.close();
                        throw th;
                    } catch (IOException e9) {
                        e9.printStackTrace();
                        throw th;
                    }
                }
                return;
            }
            printStream = System.err;
            m5a = C0000a.m5a("FileUtil.removeDuplicateLineBreak(File f): ");
            m5a.append(file.getAbsolutePath());
            str = " is a directory!";
        }
        m5a.append(str);
        printStream.println(m5a.toString());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || FileUtil.class != obj.getClass()) {
            return false;
        }
        FileUtil fileUtil = (FileUtil) obj;
        ResourceBundle resourceBundle = this.m_bundle;
        if (resourceBundle == null) {
            if (fileUtil.m_bundle != null) {
                return false;
            }
        } else if (!resourceBundle.equals(fileUtil.m_bundle)) {
            return false;
        }
        return true;
    }

    public String formatFilesize(long j, Locale locale) {
        String string;
        Object[] objArr;
        long abs = Math.abs(j);
        if (Math.abs(j) < 1024) {
            string = this.m_bundle.getString("GUI_FILEUTIL_FILESIZE_BYTES_1");
            objArr = new Object[]{new Long(abs)};
        } else if (abs < PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            string = this.m_bundle.getString("GUI_FILEUTIL_FILESIZE_KBYTES_1");
            objArr = new Object[]{new Double(abs / 1024.0d)};
        } else {
            ResourceBundle resourceBundle = this.m_bundle;
            if (abs < IjkMediaMeta.AV_CH_STEREO_RIGHT) {
                return MessageFormat.format(resourceBundle.getString("GUI_FILEUTIL_FILESIZE_MBYTES_1"), new Double(j / 1048576.0d));
            }
            string = resourceBundle.getString("GUI_FILEUTIL_FILESIZE_GBYTES_1");
            objArr = new Object[]{new Double(abs / 1.073741824E9d)};
        }
        return MessageFormat.format(string, objArr);
    }

    public int hashCode() {
        ResourceBundle resourceBundle = this.m_bundle;
        return (resourceBundle == null ? 0 : resourceBundle.hashCode()) + 31;
    }
}
