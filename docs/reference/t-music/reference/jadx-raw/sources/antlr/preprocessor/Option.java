package antlr.preprocessor;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class Option {
    public Grammar enclosingGrammar;
    public String name;
    public String rhs;

    public Option(String str, String str2, Grammar grammar) {
        this.name = str;
        this.rhs = str2;
        setEnclosingGrammar(grammar);
    }

    public Grammar getEnclosingGrammar() {
        return this.enclosingGrammar;
    }

    public String getName() {
        return this.name;
    }

    public String getRHS() {
        return this.rhs;
    }

    public void setEnclosingGrammar(Grammar grammar) {
        this.enclosingGrammar = grammar;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setRHS(String str) {
        this.rhs = str;
    }

    public String toString() {
        StringBuilder m5a = C0000a.m5a("\t");
        m5a.append(this.name);
        m5a.append("=");
        m5a.append(this.rhs);
        return m5a.toString();
    }
}
