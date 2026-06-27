package antlr;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

/* loaded from: classes3.dex */
public class NameSpace {
    public String _name;
    public Vector names = new Vector();

    public NameSpace(String str) {
        this._name = new String(str);
        parse(str);
    }

    public void emitClosures(PrintWriter printWriter) {
        for (int i = 0; i < this.names.size(); i++) {
            printWriter.println("ANTLR_END_NAMESPACE");
        }
    }

    public void emitDeclarations(PrintWriter printWriter) {
        Enumeration elements = this.names.elements();
        while (elements.hasMoreElements()) {
            printWriter.println("ANTLR_BEGIN_NAMESPACE(" + ((String) elements.nextElement()) + ")");
        }
    }

    public String getName() {
        return this._name;
    }

    public void parse(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, "::");
        while (stringTokenizer.hasMoreTokens()) {
            this.names.addElement(stringTokenizer.nextToken());
        }
    }
}
