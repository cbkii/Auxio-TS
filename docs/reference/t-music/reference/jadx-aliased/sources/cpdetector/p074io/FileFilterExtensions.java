package cpdetector.p074io;

import java.io.File;
import java.io.FileFilter;
import java.util.StringTokenizer;

/* loaded from: classes4.dex */
public final class FileFilterExtensions implements FileFilter {
    public String[] m_extensions;

    public FileFilterExtensions(String[] strArr) {
        verify(strArr);
        this.m_extensions = strArr;
    }

    private void verify(String[] strArr) {
        String str;
        StringBuffer stringBuffer = new StringBuffer();
        for (int length = strArr.length - 1; length >= 0; length--) {
            String str2 = strArr[length];
            if (str2 == null) {
                str = "Extension at index " + length + " is null!\n";
            } else if (str2.indexOf(46) != -1) {
                str = "Extension \"" + str2 + "\" contains a dot!\n";
            }
            stringBuffer.append(str);
        }
        if (stringBuffer.length() > 0) {
            throw new IllegalArgumentException(stringBuffer.toString());
        }
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        StringTokenizer stringTokenizer = new StringTokenizer(file.getAbsolutePath(), ".");
        String str = "no.txt";
        while (stringTokenizer.hasMoreElements()) {
            str = stringTokenizer.nextToken();
        }
        for (int length = this.m_extensions.length - 1; length >= 0; length--) {
            if (this.m_extensions[length].equals(str)) {
                return true;
            }
        }
        return false;
    }
}
