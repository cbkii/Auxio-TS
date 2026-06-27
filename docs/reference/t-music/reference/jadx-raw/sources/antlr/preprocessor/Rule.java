package antlr.preprocessor;

import antlr.collections.impl.IndexedVector;
import java.util.Enumeration;
import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class Rule {
    public String args;
    public boolean bang = false;
    public String block;
    public Grammar enclosingGrammar;
    public String initAction;
    public String name;
    public IndexedVector options;
    public String returnValue;
    public String throwsSpec;
    public String visibility;

    public Rule(String str, String str2, IndexedVector indexedVector, Grammar grammar) {
        this.name = str;
        this.block = str2;
        this.options = indexedVector;
        setEnclosingGrammar(grammar);
    }

    public String getArgs() {
        return this.args;
    }

    public boolean getBang() {
        return this.bang;
    }

    public String getName() {
        return this.name;
    }

    public String getReturnValue() {
        return this.returnValue;
    }

    public String getVisibility() {
        return this.visibility;
    }

    public boolean narrowerVisibility(Rule rule) {
        if (this.visibility.equals("public")) {
            return !rule.equals("public");
        }
        if (this.visibility.equals("protected")) {
            return rule.equals("private");
        }
        this.visibility.equals("private");
        return false;
    }

    public boolean sameSignature(Rule rule) {
        boolean equals = this.name.equals(rule.getName());
        String str = this.args;
        boolean equals2 = str != null ? str.equals(rule.getArgs()) : true;
        String str2 = this.returnValue;
        return equals && equals2 && (str2 != null ? str2.equals(rule.getReturnValue()) : true);
    }

    public void setArgs(String str) {
        this.args = str;
    }

    public void setBang() {
        this.bang = true;
    }

    public void setEnclosingGrammar(Grammar grammar) {
        this.enclosingGrammar = grammar;
    }

    public void setInitAction(String str) {
        this.initAction = str;
    }

    public void setOptions(IndexedVector indexedVector) {
        this.options = indexedVector;
    }

    public void setReturnValue(String str) {
        this.returnValue = str;
    }

    public void setThrowsSpec(String str) {
        this.throwsSpec = str;
    }

    public void setVisibility(String str) {
        this.visibility = str;
    }

    public String toString() {
        String sb;
        if (this.returnValue == null) {
            sb = "";
        } else {
            StringBuilder m5a = C0000a.m5a("returns ");
            m5a.append(this.returnValue);
            sb = m5a.toString();
        }
        String str = this.args;
        if (str == null) {
            str = "";
        }
        String str2 = getBang() ? "!" : "";
        StringBuilder m5a2 = C0000a.m5a("");
        m5a2.append(this.visibility != null ? C0000a.m3a(new StringBuilder(), this.visibility, " ") : "");
        StringBuilder m5a3 = C0000a.m5a(m5a2.toString());
        m5a3.append(this.name);
        m5a3.append(str2);
        m5a3.append(str);
        m5a3.append(" ");
        m5a3.append(sb);
        m5a3.append(this.throwsSpec);
        String sb2 = m5a3.toString();
        if (this.options != null) {
            StringBuilder m5a4 = C0000a.m5a(sb2);
            m5a4.append(System.getProperty("line.separator"));
            m5a4.append("options {");
            m5a4.append(System.getProperty("line.separator"));
            String sb3 = m5a4.toString();
            Enumeration elements = this.options.elements();
            while (elements.hasMoreElements()) {
                StringBuilder m5a5 = C0000a.m5a(sb3);
                m5a5.append((Option) elements.nextElement());
                m5a5.append(System.getProperty("line.separator"));
                sb3 = m5a5.toString();
            }
            StringBuilder m9b = C0000a.m9b(sb3, "}");
            m9b.append(System.getProperty("line.separator"));
            sb2 = m9b.toString();
        }
        if (this.initAction != null) {
            StringBuilder m5a6 = C0000a.m5a(sb2);
            m5a6.append(this.initAction);
            m5a6.append(System.getProperty("line.separator"));
            sb2 = m5a6.toString();
        }
        StringBuilder m5a7 = C0000a.m5a(sb2);
        m5a7.append(this.block);
        return m5a7.toString();
    }
}
