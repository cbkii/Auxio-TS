package antlr.preprocessor;

import antlr.collections.impl.IndexedVector;
import java.io.PrintWriter;
import java.util.Enumeration;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class GrammarFile {
    public String fileName;
    public IndexedVector options;
    public antlr.Tool tool;
    public String headerAction = "";
    public boolean expanded = false;
    public IndexedVector grammars = new IndexedVector();

    public GrammarFile(antlr.Tool tool, String str) {
        this.fileName = str;
        this.tool = tool;
    }

    public void addGrammar(Grammar grammar) {
        this.grammars.appendElement(grammar.getName(), grammar);
    }

    public void addHeaderAction(String str) {
        this.headerAction += str + System.getProperty("line.separator");
    }

    public void generateExpandedFile() {
        if (this.expanded) {
            PrintWriter printWriterOpenOutputFile = this.tool.openOutputFile(nameForExpandedGrammarFile(getName()));
            printWriterOpenOutputFile.println(toString());
            printWriterOpenOutputFile.close();
        }
    }

    public IndexedVector getGrammars() {
        return this.grammars;
    }

    public String getName() {
        return this.fileName;
    }

    public String nameForExpandedGrammarFile(String str) {
        if (!this.expanded) {
            return str;
        }
        StringBuilder sbM5a = C0000a.m5a("expanded");
        sbM5a.append(this.tool.fileMinusPath(str));
        return sbM5a.toString();
    }

    public void setExpanded(boolean z) {
        this.expanded = z;
    }

    public void setOptions(IndexedVector indexedVector) {
        this.options = indexedVector;
    }

    public String toString() {
        String str = this.headerAction;
        if (str == null) {
            str = "";
        }
        IndexedVector indexedVector = this.options;
        String strOptionsToString = indexedVector != null ? Hierarchy.optionsToString(indexedVector) : "";
        StringBuffer stringBuffer = new StringBuffer(10000);
        stringBuffer.append(str);
        stringBuffer.append(strOptionsToString);
        Enumeration enumerationElements = this.grammars.elements();
        while (enumerationElements.hasMoreElements()) {
            stringBuffer.append(((Grammar) enumerationElements.nextElement()).toString());
        }
        return stringBuffer.toString();
    }
}
