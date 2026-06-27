package antlr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class PreservingFileWriter extends FileWriter {
    public File target_file;
    public File tmp_file;

    public PreservingFileWriter(String str) throws IOException {
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

    /* JADX WARN: Removed duplicated region for block: B:41:0x00a7 A[Catch: all -> 0x00ab, LOOP:2: B:72:0x0089->B:41:0x00a7, LOOP_END, TRY_ENTER, TRY_LEAVE, TryCatch #3 {all -> 0x00ab, blocks: (B:31:0x0089, B:41:0x00a7), top: B:72:0x0089 }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x008f A[EDGE_INSN: B:70:0x008f->B:33:0x008f BREAK  A[LOOP:2: B:72:0x0089->B:41:0x00a7], EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x00bd A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x00b8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.io.OutputStreamWriter, java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void close() throws Throwable {
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        File file;
        int i;
        File file2;
        try {
            super.close();
            char[] cArr = new char[1024];
            if (this.target_file.length() != this.tmp_file.length()) {
                bufferedReader = new BufferedReader(new FileReader(this.tmp_file));
                bufferedWriter = new BufferedWriter(new FileWriter(this.target_file));
                while (true) {
                    i = bufferedReader.read(cArr, 0, 1024);
                    if (i != -1) {
                    }
                    bufferedWriter.write(cArr, 0, i);
                }
                bufferedReader.close();
                bufferedWriter.close();
                file2 = this.tmp_file;
                if (file2 == null) {
                    return;
                } else {
                    return;
                }
            }
            char[] cArr2 = new char[1024];
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader(this.tmp_file));
            try {
                BufferedReader bufferedReader3 = new BufferedReader(new FileReader(this.target_file));
                boolean z = true;
                while (true) {
                    if (!z) {
                        break;
                    }
                    int i2 = bufferedReader2.read(cArr, 0, 1024);
                    if (i2 != bufferedReader3.read(cArr2, 0, 1024)) {
                        z = false;
                        break;
                    }
                    if (i2 == -1) {
                        break;
                    }
                    int i3 = 0;
                    while (true) {
                        if (i3 >= i2) {
                            break;
                        }
                        if (cArr[i3] != cArr2[i3]) {
                            z = false;
                            break;
                        }
                        i3++;
                    }
                }
                bufferedReader2.close();
                bufferedReader3.close();
                if (z) {
                    File file3 = this.tmp_file;
                    if (file3 == null || !file3.exists()) {
                        return;
                    }
                    this.tmp_file.delete();
                    this.tmp_file = null;
                    return;
                }
                try {
                    bufferedReader = new BufferedReader(new FileReader(this.tmp_file));
                    try {
                        bufferedWriter = new BufferedWriter(new FileWriter(this.target_file));
                        while (true) {
                            try {
                                i = bufferedReader.read(cArr, 0, 1024);
                                if (i != -1) {
                                    try {
                                        break;
                                    } catch (IOException unused) {
                                    }
                                } else {
                                    bufferedWriter.write(cArr, 0, i);
                                }
                            } catch (Throwable th) {
                                th = th;
                            }
                        }
                        bufferedReader.close();
                        try {
                            bufferedWriter.close();
                        } catch (IOException unused2) {
                        }
                        file2 = this.tmp_file;
                        if (file2 == null && file2.exists()) {
                            this.tmp_file.delete();
                            this.tmp_file = null;
                            return;
                        }
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedWriter = null;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    bufferedReader = null;
                    bufferedWriter = null;
                }
            } catch (Throwable th4) {
                th = th4;
                bufferedReader = bufferedReader2;
                bufferedWriter = null;
                if (bufferedReader != null) {
                }
                if (bufferedWriter != null) {
                }
                file = this.tmp_file;
                if (file == null) {
                    throw th;
                }
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            bufferedReader = null;
        }
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException unused3) {
            }
        }
        if (bufferedWriter != null) {
            try {
                bufferedWriter.close();
            } catch (IOException unused4) {
            }
        }
        file = this.tmp_file;
        if (file == null || !file.exists()) {
            throw th;
        }
        this.tmp_file.delete();
        this.tmp_file = null;
        throw th;
    }
}
