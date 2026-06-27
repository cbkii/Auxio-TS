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
import p000a.p001a.p002a.p003a.C0000a;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* JADX INFO: loaded from: classes4.dex */
public final class FileUtil {
    public static FileUtil instance;
    public final ResourceBundle m_bundle = ResourceBundle.getBundle("messages");

    public static Map.Entry<String, String> cutDirectoryInformation(String str) {
        String absolutePath;
        StringBuffer stringBuffer = new StringBuffer();
        String property = System.getProperty("file.separator");
        StringTokenizer stringTokenizer = new StringTokenizer(str, property);
        int iCountTokens = stringTokenizer.countTokens();
        String str2 = "";
        if (iCountTokens != 0) {
            if (iCountTokens != 1) {
                while (stringTokenizer.hasMoreElements()) {
                    String strNextToken = stringTokenizer.nextToken();
                    if (stringTokenizer.hasMoreTokens()) {
                        stringBuffer.append(strNextToken);
                        stringBuffer.append(property);
                    } else if (new File(str).isFile()) {
                        str2 = strNextToken;
                    } else {
                        stringBuffer.append(strNextToken);
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
        String strNextToken = "";
        if (!externalForm.endsWith("/")) {
            StringTokenizer stringTokenizer = new StringTokenizer(url.getFile(), "/");
            stringBuffer.append(url.getProtocol());
            stringBuffer.append(":");
            stringBuffer.append(url.getHost());
            while (stringTokenizer.hasMoreElements()) {
                stringBuffer.append(strNextToken);
                strNextToken = stringTokenizer.nextToken();
                stringBuffer.append("/");
            }
            externalForm = stringBuffer.toString();
        }
        return new Entry(externalForm, strNextToken);
    }

    public static Map.Entry<String, String> cutExtension(String str) {
        String strNextToken;
        StringTokenizer stringTokenizer = new StringTokenizer(str, ".");
        int iCountTokens = stringTokenizer.countTokens();
        if (iCountTokens > 1) {
            StringBuffer stringBuffer = new StringBuffer();
            while (iCountTokens > 1) {
                iCountTokens--;
                stringBuffer.append(stringTokenizer.nextToken());
                if (iCountTokens > 1) {
                    stringBuffer.append(".");
                }
            }
            str = stringBuffer.toString();
            strNextToken = stringTokenizer.nextToken();
        } else {
            strNextToken = "";
        }
        return new Entry(str, strNextToken);
    }

    public static String getDefaultFileName(String str) {
        File file = new File(str);
        if (file.exists()) {
            Map.Entry<String, String> entryCutExtension = cutExtension(str);
            String key = entryCutExtension.getKey();
            String value = entryCutExtension.getValue();
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

    public static boolean isAllASCII(InputStream inputStream) throws IOException {
        int i;
        do {
            i = inputStream.read();
            if (i > 127) {
                return false;
            }
        } while (i != -1);
        return true;
    }

    public static boolean isEqual(File file, Charset charset, Charset charset2) throws Throwable {
        Throwable th;
        InputStreamReader inputStreamReader;
        int i;
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
                        int i2 = inputStreamReader.read();
                        i = inputStreamReader3.read();
                        if (i2 == i) {
                            if (i2 == -1) {
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
                } while (i != -1);
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

    public static byte[] readRAM(File file) throws IOException {
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

    /* JADX WARN: Can't wrap try/catch for region: R(15:9|87|(4:81|10|95|11)|(4:12|(1:14)(1:100)|(2:93|70)|(3:75|86|79)(1:112))|15|(3:18|(6:103|22|(1:24)(1:25)|26|106|105)(4:104|21|107|105)|16)|101|27|98|28|89|29|33|86|79) */
    /* JADX WARN: Can't wrap try/catch for region: R(18:9|87|81|10|95|11|(4:12|(1:14)(1:100)|(2:93|70)|(3:75|86|79)(1:112))|15|(3:18|(6:103|22|(1:24)(1:25)|26|106|105)(4:104|21|107|105)|16)|101|27|98|28|89|29|33|86|79) */
    /* JADX WARN: Code restructure failed: missing block: B:108:?, code lost:
    
        throw r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:?, code lost:
    
        throw r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00ac, code lost:
    
        r10 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00ad, code lost:
    
        r10.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00b8, code lost:
    
        r10 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00b9, code lost:
    
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00bb, code lost:
    
        r10 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00bc, code lost:
    
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00be, code lost:
    
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00c9, code lost:
    
        r10.printStackTrace(java.lang.System.err);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00ce, code lost:
    
        if (r4 != null) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00d0, code lost:
    
        r4.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00d4, code lost:
    
        r10 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00d5, code lost:
    
        r10.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00d8, code lost:
    
        if (r2 != null) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00da, code lost:
    
        r2.flush();
        r2.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00e1, code lost:
    
        if (r4 != null) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00e3, code lost:
    
        r4.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00e7, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00e8, code lost:
    
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00eb, code lost:
    
        if (r2 != null) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00ed, code lost:
    
        r2.flush();
        r2.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00f4, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00f5, code lost:
    
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00f8, code lost:
    
        throw r10;
     */
    /* JADX WARN: Removed duplicated region for block: B:112:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0106 A[Catch: IOException -> 0x010d, TRY_ENTER, TRY_LEAVE, TryCatch #6 {IOException -> 0x010d, blocks: (B:33:0x00b0, B:55:0x00da, B:75:0x0106), top: B:87:0x0040 }] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x00fc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:78:0x010e -> B:86:0x0111). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void removeDuplicateLineBreaks(File file) throws Throwable {
        BufferedInputStream bufferedInputStream;
        StringBuffer stringBuffer;
        PrintStream printStream;
        StringBuilder sbM5a;
        String str;
        String newLine = StringUtil.getNewLine();
        if (!file.exists()) {
            printStream = System.err;
            sbM5a = C0000a.m5a("FileUtil.removeDuplicateLineBreak(File f): ");
            sbM5a.append(file.getAbsolutePath());
            str = " does not exist!";
        } else {
            if (!file.isDirectory()) {
                FileWriter fileWriter = null;
                try {
                    try {
                        try {
                            bufferedInputStream = new BufferedInputStream(new FileInputStream(file), 1024);
                            try {
                                stringBuffer = new StringBuffer();
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
                while (true) {
                    int i = bufferedInputStream.read();
                    if (i == -1) {
                        break;
                    } else {
                        stringBuffer.append((char) i);
                    }
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    if (fileWriter == null) {
                        fileWriter.flush();
                        fileWriter.close();
                        return;
                    }
                    return;
                }
                StringTokenizer stringTokenizer = new StringTokenizer(stringBuffer.toString(), newLine, true);
                StringBuffer stringBuffer2 = new StringBuffer();
                int i2 = 0;
                while (stringTokenizer.hasMoreTokens()) {
                    String strTrim = stringTokenizer.nextToken().trim();
                    if (!strTrim.equals("") || i2 <= 0) {
                        if (strTrim.equals("")) {
                            i2++;
                            strTrim = newLine;
                        } else {
                            i2 = 0;
                        }
                        stringBuffer2.append(strTrim);
                    } else {
                        i2++;
                    }
                }
                file.delete();
                file.createNewFile();
                FileWriter fileWriter2 = new FileWriter(file);
                fileWriter2.write(stringBuffer2.toString());
                bufferedInputStream.close();
                fileWriter2.flush();
                fileWriter2.close();
                return;
            }
            printStream = System.err;
            sbM5a = C0000a.m5a("FileUtil.removeDuplicateLineBreak(File f): ");
            sbM5a.append(file.getAbsolutePath());
            str = " is a directory!";
        }
        sbM5a.append(str);
        printStream.println(sbM5a.toString());
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
        long jAbs = Math.abs(j);
        if (Math.abs(j) < 1024) {
            string = this.m_bundle.getString("GUI_FILEUTIL_FILESIZE_BYTES_1");
            objArr = new Object[]{new Long(jAbs)};
        } else if (jAbs < PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            string = this.m_bundle.getString("GUI_FILEUTIL_FILESIZE_KBYTES_1");
            objArr = new Object[]{new Double(jAbs / 1024.0d)};
        } else {
            ResourceBundle resourceBundle = this.m_bundle;
            if (jAbs < IjkMediaMeta.AV_CH_STEREO_RIGHT) {
                return MessageFormat.format(resourceBundle.getString("GUI_FILEUTIL_FILESIZE_MBYTES_1"), new Double(j / 1048576.0d));
            }
            string = resourceBundle.getString("GUI_FILEUTIL_FILESIZE_GBYTES_1");
            objArr = new Object[]{new Double(jAbs / 1.073741824E9d)};
        }
        return MessageFormat.format(string, objArr);
    }

    public int hashCode() {
        ResourceBundle resourceBundle = this.m_bundle;
        return (resourceBundle == null ? 0 : resourceBundle.hashCode()) + 31;
    }
}
