package antlr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class PreservingFileWriter extends FileWriter {
    public File target_file;
    public File tmp_file;

    public PreservingFileWriter(String str) {
        super(C0000a.m1a(str, ".antlr.tmp"));
        this.target_file = new File(str);
        String parent = this.target_file.getParent();
        if (parent != null) {
            File file = new File(parent);
            if (!file.exists()) {
                throw new IOException(C0000a.m2a("destination directory of '", str, "' doesn't exist"));
            }
            if (!file.canWrite()) {
                throw new IOException(C0000a.m2a("destination directory of '", str, "' isn't writeable"));
            }
        }
        if (this.target_file.exists() && !this.target_file.canWrite()) {
            throw new IOException(C0000a.m2a("cannot write to '", str, "'"));
        }
        this.tmp_file = new File(C0000a.m1a(str, ".antlr.tmp"));
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:52:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00bd A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00b8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.io.OutputStreamWriter, java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void close() {
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        File file;
        try {
            super.close();
            char[] cArr = new char[1024];
            if (this.target_file.length() == this.tmp_file.length()) {
                char[] cArr2 = new char[1024];
                BufferedReader bufferedReader2 = new BufferedReader(new FileReader(this.tmp_file));
                try {
                    BufferedReader bufferedReader3 = new BufferedReader(new FileReader(this.target_file));
                    boolean z = true;
                    while (true) {
                        if (!z) {
                            break;
                        }
                        int read = bufferedReader2.read(cArr, 0, 1024);
                        if (read != bufferedReader3.read(cArr2, 0, 1024)) {
                            z = false;
                            break;
                        }
                        if (read == -1) {
                            break;
                        }
                        int i = 0;
                        while (true) {
                            if (i >= read) {
                                break;
                            }
                            if (cArr[i] != cArr2[i]) {
                                z = false;
                                break;
                            }
                            i++;
                        }
                    }
                    bufferedReader2.close();
                    bufferedReader3.close();
                    if (z) {
                        File file2 = this.tmp_file;
                        if (file2 == null || !file2.exists()) {
                            return;
                        }
                        this.tmp_file.delete();
                        this.tmp_file = null;
                        return;
                    }
                } catch (Throwable th) {
                    th = th;
                    bufferedReader = bufferedReader2;
                    bufferedWriter = null;
                    if (bufferedReader != null) {
                    }
                    if (bufferedWriter != null) {
                    }
                    file = this.tmp_file;
                    if (file == null) {
                    }
                }
            }
            try {
                bufferedReader = new BufferedReader(new FileReader(this.tmp_file));
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter(this.target_file));
                    while (true) {
                        try {
                            int read2 = bufferedReader.read(cArr, 0, 1024);
                            if (read2 == -1) {
                                try {
                                    break;
                                } catch (IOException unused) {
                                }
                            } else {
                                bufferedWriter.write(cArr, 0, read2);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException unused2) {
                                }
                            }
                            if (bufferedWriter != null) {
                                try {
                                    bufferedWriter.close();
                                } catch (IOException unused3) {
                                }
                            }
                            file = this.tmp_file;
                            if (file == null) {
                                throw th;
                            }
                            if (!file.exists()) {
                                throw th;
                            }
                            this.tmp_file.delete();
                            this.tmp_file = null;
                            throw th;
                        }
                    }
                    bufferedReader.close();
                    try {
                        bufferedWriter.close();
                    } catch (IOException unused4) {
                    }
                    File file3 = this.tmp_file;
                    if (file3 == null || !file3.exists()) {
                        return;
                    }
                    this.tmp_file.delete();
                    this.tmp_file = null;
                } catch (Throwable th3) {
                    th = th3;
                    bufferedWriter = null;
                    if (bufferedReader != null) {
                    }
                    if (bufferedWriter != null) {
                    }
                    file = this.tmp_file;
                    if (file == null) {
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                bufferedReader = null;
                bufferedWriter = null;
            }
        } catch (Throwable th5) {
            th = th5;
            bufferedReader = null;
        }
    }
}
