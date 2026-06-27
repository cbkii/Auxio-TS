package antlr;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
public class PrintWriterWithSMAP extends PrintWriter {
    public boolean anythingWrittenSinceMapping;
    public int currentOutputLine;
    public int currentSourceLine;
    public boolean lastPrintCharacterWasCR;
    public boolean mapLines;
    public boolean mapSingleSourceLine;
    public Map sourceMap;

    public PrintWriterWithSMAP(OutputStream outputStream) {
        super(outputStream);
        this.currentOutputLine = 1;
        this.currentSourceLine = 0;
        this.sourceMap = new HashMap();
        this.lastPrintCharacterWasCR = false;
        this.mapLines = false;
        this.mapSingleSourceLine = false;
        this.anythingWrittenSinceMapping = false;
    }

    public PrintWriterWithSMAP(OutputStream outputStream, boolean z) {
        super(outputStream, z);
        this.currentOutputLine = 1;
        this.currentSourceLine = 0;
        this.sourceMap = new HashMap();
        this.lastPrintCharacterWasCR = false;
        this.mapLines = false;
        this.mapSingleSourceLine = false;
        this.anythingWrittenSinceMapping = false;
    }

    public PrintWriterWithSMAP(Writer writer) {
        super(writer);
        this.currentOutputLine = 1;
        this.currentSourceLine = 0;
        this.sourceMap = new HashMap();
        this.lastPrintCharacterWasCR = false;
        this.mapLines = false;
        this.mapSingleSourceLine = false;
        this.anythingWrittenSinceMapping = false;
    }

    public PrintWriterWithSMAP(Writer writer, boolean z) {
        super(writer, z);
        this.currentOutputLine = 1;
        this.currentSourceLine = 0;
        this.sourceMap = new HashMap();
        this.lastPrintCharacterWasCR = false;
        this.mapLines = false;
        this.mapSingleSourceLine = false;
        this.anythingWrittenSinceMapping = false;
    }

    public void checkChar(int i) {
        if ((this.lastPrintCharacterWasCR && i != 10) || i == 10) {
            mapLine(true);
        } else if (!Character.isWhitespace((char) i)) {
            this.anythingWrittenSinceMapping = true;
        }
        this.lastPrintCharacterWasCR = i == 13;
    }

    public void dump(PrintWriter printWriter, String str, String str2) {
        printWriter.println("SMAP");
        printWriter.println(str + ".java");
        printWriter.println("G");
        printWriter.println("*S G");
        printWriter.println("*F");
        printWriter.println("+ 0 " + str2);
        printWriter.println(str2);
        printWriter.println("*L");
        ArrayList<Integer> arrayList = new ArrayList(this.sourceMap.keySet());
        Collections.sort(arrayList);
        for (Integer num : arrayList) {
            Iterator it = ((List) this.sourceMap.get(num)).iterator();
            while (it.hasNext()) {
                printWriter.println(num + ":" + ((Integer) it.next()));
            }
        }
        printWriter.println("*E");
        printWriter.close();
    }

    public void endMapping() {
        mapLine(false);
        this.mapLines = false;
        this.mapSingleSourceLine = false;
    }

    public int getCurrentOutputLine() {
        return this.currentOutputLine;
    }

    public Map getSourceMap() {
        return this.sourceMap;
    }

    public void mapLine(boolean z) {
        if (this.mapLines && this.anythingWrittenSinceMapping) {
            Integer num = new Integer(this.currentSourceLine);
            Integer num2 = new Integer(this.currentOutputLine);
            List arrayList = (List) this.sourceMap.get(num);
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.sourceMap.put(num, arrayList);
            }
            if (!arrayList.contains(num2)) {
                arrayList.add(num2);
            }
        }
        if (z) {
            this.currentOutputLine++;
        }
        if (!this.mapSingleSourceLine) {
            this.currentSourceLine++;
        }
        this.anythingWrittenSinceMapping = false;
    }

    @Override // java.io.PrintWriter
    public void println() {
        mapLine(true);
        super.println();
        this.lastPrintCharacterWasCR = false;
    }

    public void startMapping(int i) {
        this.mapLines = true;
        if (i != -888) {
            this.currentSourceLine = i;
        }
    }

    public void startSingleSourceLineMapping(int i) {
        this.mapSingleSourceLine = true;
        this.mapLines = true;
        if (i != -888) {
            this.currentSourceLine = i;
        }
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(int i) {
        checkChar(i);
        super.write(i);
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(String str, int i, int i2) {
        int i3 = i + i2;
        for (int i4 = i; i4 < i3; i4++) {
            checkChar(str.charAt(i4));
        }
        super.write(str, i, i2);
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(char[] cArr, int i, int i2) {
        int i3 = i + i2;
        for (int i4 = i; i4 < i3; i4++) {
            checkChar(cArr[i4]);
        }
        super.write(cArr, i, i2);
    }
}
