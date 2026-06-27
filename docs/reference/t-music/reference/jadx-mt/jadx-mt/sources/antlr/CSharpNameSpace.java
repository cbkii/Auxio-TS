package antlr;

import java.io.PrintWriter;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class CSharpNameSpace extends NameSpace {
    public CSharpNameSpace(String str) {
        super(str);
    }

    @Override // antlr.NameSpace
    public void emitClosures(PrintWriter printWriter) {
        printWriter.println("}");
    }

    @Override // antlr.NameSpace
    public void emitDeclarations(PrintWriter printWriter) {
        StringBuilder sbM5a = C0000a.m5a("namespace ");
        sbM5a.append(getName());
        printWriter.println(sbM5a.toString());
        printWriter.println("{");
    }
}
