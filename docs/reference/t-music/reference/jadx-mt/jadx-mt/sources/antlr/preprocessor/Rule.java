package antlr.preprocessor;

import antlr.collections.impl.IndexedVector;
import java.util.Enumeration;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
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
        boolean zEquals = this.name.equals(rule.getName());
        String str = this.args;
        boolean zEquals2 = str != null ? str.equals(rule.getArgs()) : true;
        String str2 = this.returnValue;
        return zEquals && zEquals2 && (str2 != null ? str2.equals(rule.getReturnValue()) : true);
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
        String string;
        if (this.returnValue == null) {
            string = "";
        } else {
            StringBuilder sbM5a = C0000a.m5a("returns ");
            sbM5a.append(this.returnValue);
            string = sbM5a.toString();
        }
        String str = this.args;
        if (str == null) {
            str = "";
        }
        String str2 = getBang() ? "!" : "";
        StringBuilder sbM5a2 = C0000a.m5a("");
        sbM5a2.append(this.visibility != null ? C0000a.m3a(new StringBuilder(), this.visibility, " ") : "");
        StringBuilder sbM5a3 = C0000a.m5a(sbM5a2.toString());
        sbM5a3.append(this.name);
        sbM5a3.append(str2);
        sbM5a3.append(str);
        sbM5a3.append(" ");
        sbM5a3.append(string);
        sbM5a3.append(this.throwsSpec);
        String string2 = sbM5a3.toString();
        if (this.options != null) {
            StringBuilder sbM5a4 = C0000a.m5a(string2);
            sbM5a4.append(System.getProperty("line.separator"));
            sbM5a4.append("options {");
            sbM5a4.append(System.getProperty("line.separator"));
            String string3 = sbM5a4.toString();
            Enumeration enumerationElements = this.options.elements();
            while (enumerationElements.hasMoreElements()) {
                StringBuilder sbM5a5 = C0000a.m5a(string3);
                sbM5a5.append((Option) enumerationElements.nextElement());
                sbM5a5.append(System.getProperty("line.separator"));
                string3 = sbM5a5.toString();
            }
            StringBuilder sbM9b = C0000a.m9b(string3, "}");
            sbM9b.append(System.getProperty("line.separator"));
            string2 = sbM9b.toString();
        }
        if (this.initAction != null) {
            StringBuilder sbM5a6 = C0000a.m5a(string2);
            sbM5a6.append(this.initAction);
            sbM5a6.append(System.getProperty("line.separator"));
            string2 = sbM5a6.toString();
        }
        StringBuilder sbM5a7 = C0000a.m5a(string2);
        sbM5a7.append(this.block);
        return sbM5a7.toString();
    }
}
