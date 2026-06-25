package antlr;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class DefaultJavaCodeGeneratorPrintWriterManager implements JavaCodeGeneratorPrintWriterManager {
    public String currentFileName;
    public PrintWriter currentOutput;
    public Grammar grammar;
    public PrintWriterWithSMAP smapOutput;
    public Map sourceMaps = new HashMap();
    public Tool tool;

    @Override // antlr.JavaCodeGeneratorPrintWriterManager
    public void endMapping() {
        this.smapOutput.endMapping();
    }

    @Override // antlr.JavaCodeGeneratorPrintWriterManager
    public void finishOutput() {
        this.currentOutput.close();
        if (this.grammar != null) {
            PrintWriter printWriterOpenOutputFile = this.tool.openOutputFile(this.grammar.getClassName() + ".smap");
            String strReplace = this.grammar.getFilename().replace('\\', '/');
            int iLastIndexOf = strReplace.lastIndexOf(47);
            if (iLastIndexOf != -1) {
                strReplace = strReplace.substring(iLastIndexOf + 1);
            }
            this.smapOutput.dump(printWriterOpenOutputFile, this.grammar.getClassName(), strReplace);
            this.sourceMaps.put(this.currentFileName, this.smapOutput.getSourceMap());
        }
        this.currentOutput = null;
    }

    public int getCurrentOutputLine() {
        return this.smapOutput.getCurrentOutputLine();
    }

    @Override // antlr.JavaCodeGeneratorPrintWriterManager
    public Map getSourceMaps() {
        return this.sourceMaps;
    }

    @Override // antlr.JavaCodeGeneratorPrintWriterManager
    public PrintWriter setupOutput(Tool tool, Grammar grammar) {
        return setupOutput(tool, grammar, null);
    }

    public PrintWriter setupOutput(Tool tool, Grammar grammar, String str) {
        this.tool = tool;
        this.grammar = grammar;
        if (str == null) {
            str = grammar.getClassName();
        }
        this.smapOutput = new PrintWriterWithSMAP(tool.openOutputFile(str + ".java"));
        this.currentFileName = C0000a.m1a(str, ".java");
        this.currentOutput = this.smapOutput;
        return this.currentOutput;
    }

    @Override // antlr.JavaCodeGeneratorPrintWriterManager
    public PrintWriter setupOutput(Tool tool, String str) {
        return setupOutput(tool, null, str);
    }

    @Override // antlr.JavaCodeGeneratorPrintWriterManager
    public void startMapping(int i) {
        this.smapOutput.startMapping(i);
    }

    @Override // antlr.JavaCodeGeneratorPrintWriterManager
    public void startSingleSourceLineMapping(int i) {
        this.smapOutput.startSingleSourceLineMapping(i);
    }
}
