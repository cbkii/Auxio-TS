package antlr.preprocessor;

import antlr.collections.impl.Vector;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class Tool {
    public antlr.Tool antlrTool;
    public String[] args;
    public String grammarFileName;
    public Vector grammars;
    public int nargs;
    public Hierarchy theHierarchy;

    public Tool(antlr.Tool tool, String[] strArr) {
        this.antlrTool = tool;
        processArguments(strArr);
    }

    public static void main(String[] strArr) {
        Tool tool = new Tool(new antlr.Tool(), strArr);
        tool.preprocess();
        for (String str : tool.preprocessedArgList()) {
            PrintStream printStream = System.out;
            StringBuilder sbM5a = C0000a.m5a(" ");
            sbM5a.append(str);
            printStream.print(sbM5a.toString());
        }
        System.out.println();
    }

    private void processArguments(String[] strArr) {
        antlr.Tool tool;
        String str;
        this.nargs = 0;
        this.args = new String[strArr.length];
        int i = 0;
        while (i < strArr.length) {
            if (strArr[i].length() == 0) {
                tool = this.antlrTool;
                str = "Zero length argument ignoring...";
            } else {
                if (strArr[i].equals("-glib")) {
                    if (!File.separator.equals("\\") || strArr[i].indexOf(47) == -1) {
                        i++;
                        this.grammars = antlr.Tool.parseSeparatedList(strArr[i], ';');
                    } else {
                        tool = this.antlrTool;
                        str = "-glib cannot deal with '/' on a PC: use '\\'; ignoring...";
                    }
                } else if (strArr[i].equals("-o")) {
                    String[] strArr2 = this.args;
                    int i2 = this.nargs;
                    this.nargs = i2 + 1;
                    strArr2[i2] = strArr[i];
                    int i3 = i + 1;
                    if (i3 >= strArr.length) {
                        this.antlrTool.error("missing output directory with -o option; ignoring");
                    } else {
                        int i4 = this.nargs;
                        this.nargs = i4 + 1;
                        strArr2[i4] = strArr[i3];
                        this.antlrTool.setOutputDirectory(strArr[i3]);
                        i = i3;
                    }
                } else if (strArr[i].charAt(0) == '-') {
                    String[] strArr3 = this.args;
                    int i5 = this.nargs;
                    this.nargs = i5 + 1;
                    strArr3[i5] = strArr[i];
                } else {
                    this.grammarFileName = strArr[i];
                    if (this.grammars == null) {
                        this.grammars = new Vector(10);
                    }
                    this.grammars.appendElement(this.grammarFileName);
                    if (i + 1 < strArr.length) {
                        this.antlrTool.warning("grammar file must be last; ignoring other arguments...");
                        return;
                    }
                }
                i++;
            }
            tool.warning(str);
            i++;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0053 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0054  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean preprocess() {
        String strNameForExpandedGrammarFile;
        antlr.Tool tool;
        StringBuilder sb;
        String string;
        if (this.grammarFileName == null) {
            tool = this.antlrTool;
            string = "no grammar file specified";
        } else {
            if (this.grammars != null) {
                this.theHierarchy = new Hierarchy(this.antlrTool);
                Enumeration enumerationElements = this.grammars.elements();
                while (enumerationElements.hasMoreElements()) {
                    String str = (String) enumerationElements.nextElement();
                    try {
                        this.theHierarchy.readGrammarFile(str);
                    } catch (FileNotFoundException unused) {
                        tool = this.antlrTool;
                        sb = new StringBuilder();
                        sb.append("file ");
                        sb.append(str);
                        strNameForExpandedGrammarFile = " not found";
                        sb.append(strNameForExpandedGrammarFile);
                        string = sb.toString();
                        tool.toolError(string);
                        return false;
                    }
                }
                if (this.theHierarchy.verifyThatHierarchyIsComplete()) {
                    return false;
                }
                this.theHierarchy.expandGrammarsInFile(this.grammarFileName);
                GrammarFile file = this.theHierarchy.getFile(this.grammarFileName);
                strNameForExpandedGrammarFile = file.nameForExpandedGrammarFile(this.grammarFileName);
                if (strNameForExpandedGrammarFile.equals(this.grammarFileName)) {
                    String[] strArr = this.args;
                    int i = this.nargs;
                    this.nargs = i + 1;
                    strArr[i] = this.grammarFileName;
                    return true;
                }
                try {
                    file.generateExpandedFile();
                    String[] strArr2 = this.args;
                    int i2 = this.nargs;
                    this.nargs = i2 + 1;
                    strArr2[i2] = this.antlrTool.getOutputDirectory() + System.getProperty("file.separator") + strNameForExpandedGrammarFile;
                    return true;
                } catch (IOException unused2) {
                    tool = this.antlrTool;
                    sb = new StringBuilder();
                    sb.append("cannot write expanded grammar file ");
                    sb.append(strNameForExpandedGrammarFile);
                    string = sb.toString();
                    tool.toolError(string);
                    return false;
                }
            }
            if (this.theHierarchy.verifyThatHierarchyIsComplete()) {
            }
        }
        tool.toolError(string);
        return false;
    }

    public String[] preprocessedArgList() {
        int i = this.nargs;
        String[] strArr = new String[i];
        System.arraycopy(this.args, 0, strArr, 0, i);
        this.args = strArr;
        return this.args;
    }
}
