package antlr;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
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
            PrintWriter openOutputFile = this.tool.openOutputFile(this.grammar.getClassName() + ".smap");
            String replace = this.grammar.getFilename().replace('\\', '/');
            int lastIndexOf = replace.lastIndexOf(47);
            if (lastIndexOf != -1) {
                replace = replace.substring(lastIndexOf + 1);
            }
            this.smapOutput.dump(openOutputFile, this.grammar.getClassName(), replace);
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
