package antlr;

import antlr.collections.AST;
import antlr.collections.ASTEnumeration;
import antlr.collections.impl.ASTEnumerator;
import antlr.collections.impl.Vector;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public abstract class BaseAST implements AST, Serializable {
    public static String[] tokenNames;
    public static boolean verboseStringConversion;
    public BaseAST down;
    public BaseAST right;

    public static String decode(String str) {
        String str2;
        String str3;
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (i < str.length()) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '&') {
                char cCharAt2 = str.charAt(i + 1);
                char cCharAt3 = str.charAt(i + 2);
                char cCharAt4 = str.charAt(i + 3);
                int i2 = i + 4;
                char cCharAt5 = str.charAt(i2);
                int i3 = i + 5;
                char cCharAt6 = str.charAt(i3);
                if (cCharAt2 == 'a' && cCharAt3 == 'm' && cCharAt4 == 'p' && cCharAt5 == ';') {
                    stringBuffer.append("&");
                    i = i3;
                } else {
                    if (cCharAt2 == 'l' && cCharAt3 == 't' && cCharAt4 == ';') {
                        str3 = "<";
                    } else if (cCharAt2 == 'g' && cCharAt3 == 't' && cCharAt4 == ';') {
                        str3 = ">";
                    } else {
                        if (cCharAt2 == 'q' && cCharAt3 == 'u' && cCharAt4 == 'o' && cCharAt5 == 't' && cCharAt6 == ';') {
                            str2 = "\"";
                        } else if (cCharAt2 == 'a' && cCharAt3 == 'p' && cCharAt4 == 'o' && cCharAt5 == 's' && cCharAt6 == ';') {
                            str2 = "'";
                        } else {
                            stringBuffer.append("&");
                        }
                        stringBuffer.append(str2);
                        i += 6;
                    }
                    stringBuffer.append(str3);
                    i = i2;
                }
            } else {
                stringBuffer.append(cCharAt);
            }
            i++;
        }
        return new String(stringBuffer);
    }

    public static void doWorkForFindAll(AST ast, Vector vector, AST ast2, boolean z) {
        while (ast != null) {
            if ((z && ast.equalsTreePartial(ast2)) || (!z && ast.equalsTree(ast2))) {
                vector.appendElement(ast);
            }
            if (ast.getFirstChild() != null) {
                doWorkForFindAll(ast.getFirstChild(), vector, ast2, z);
            }
            ast = ast.getNextSibling();
        }
    }

    public static String encode(String str) {
        String str2;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '\"') {
                str2 = "&quot;";
            } else if (cCharAt == '<') {
                str2 = "&lt;";
            } else if (cCharAt == '>') {
                str2 = "&gt;";
            } else if (cCharAt == '&') {
                str2 = "&amp;";
            } else if (cCharAt != '\'') {
                stringBuffer.append(cCharAt);
            } else {
                str2 = "&apos;";
            }
            stringBuffer.append(str2);
        }
        return new String(stringBuffer);
    }

    public static String[] getTokenNames() {
        return tokenNames;
    }

    public static void setVerboseStringConversion(boolean z, String[] strArr) {
        verboseStringConversion = z;
        tokenNames = strArr;
    }

    @Override // antlr.collections.AST
    public void addChild(AST ast) {
        if (ast == null) {
            return;
        }
        BaseAST baseAST = this.down;
        if (baseAST == null) {
            this.down = (BaseAST) ast;
            return;
        }
        while (true) {
            BaseAST baseAST2 = baseAST.right;
            if (baseAST2 == null) {
                baseAST.right = (BaseAST) ast;
                return;
            }
            baseAST = baseAST2;
        }
    }

    @Override // antlr.collections.AST
    public boolean equals(AST ast) {
        if (ast == null) {
            return false;
        }
        if ((getText() != null || ast.getText() == null) && (getText() == null || ast.getText() != null)) {
            return (getText() == null && ast.getText() == null) ? getType() == ast.getType() : getText().equals(ast.getText()) && getType() == ast.getType();
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [antlr.collections.AST] */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5 */
    @Override // antlr.collections.AST
    public boolean equalsList(AST ast) {
        ?? r3 = this;
        if (ast == null) {
            return false;
        }
        while (r3 != 0 && ast != null) {
            if (!r3.equals(ast)) {
                return false;
            }
            if (r3.getFirstChild() != null) {
                if (!r3.getFirstChild().equalsList(ast.getFirstChild())) {
                    return false;
                }
            } else if (ast.getFirstChild() != null) {
                return false;
            }
            AST nextSibling = r3.getNextSibling();
            ast = ast.getNextSibling();
            r3 = nextSibling;
        }
        return r3 == 0 && ast == null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1, types: [antlr.collections.AST] */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v4 */
    @Override // antlr.collections.AST
    public boolean equalsListPartial(AST ast) {
        ?? r4 = this;
        if (ast == null) {
            return true;
        }
        while (r4 != 0 && ast != null) {
            if (!r4.equals(ast)) {
                return false;
            }
            if (r4.getFirstChild() != null && !r4.getFirstChild().equalsListPartial(ast.getFirstChild())) {
                return false;
            }
            AST nextSibling = r4.getNextSibling();
            ast = ast.getNextSibling();
            r4 = nextSibling;
        }
        return r4 != 0 || ast == null;
    }

    @Override // antlr.collections.AST
    public boolean equalsTree(AST ast) {
        if (equals(ast)) {
            return getFirstChild() != null ? getFirstChild().equalsList(ast.getFirstChild()) : ast.getFirstChild() == null;
        }
        return false;
    }

    @Override // antlr.collections.AST
    public boolean equalsTreePartial(AST ast) {
        if (ast == null) {
            return true;
        }
        if (equals(ast)) {
            return getFirstChild() == null || getFirstChild().equalsListPartial(ast.getFirstChild());
        }
        return false;
    }

    @Override // antlr.collections.AST
    public ASTEnumeration findAll(AST ast) {
        Vector vector = new Vector(10);
        if (ast == null) {
            return null;
        }
        doWorkForFindAll(this, vector, ast, false);
        return new ASTEnumerator(vector);
    }

    @Override // antlr.collections.AST
    public ASTEnumeration findAllPartial(AST ast) {
        Vector vector = new Vector(10);
        if (ast == null) {
            return null;
        }
        doWorkForFindAll(this, vector, ast, true);
        return new ASTEnumerator(vector);
    }

    @Override // antlr.collections.AST
    public int getColumn() {
        return 0;
    }

    @Override // antlr.collections.AST
    public AST getFirstChild() {
        return this.down;
    }

    @Override // antlr.collections.AST
    public int getLine() {
        return 0;
    }

    @Override // antlr.collections.AST
    public AST getNextSibling() {
        return this.right;
    }

    @Override // antlr.collections.AST
    public int getNumberOfChildren() {
        BaseAST baseAST = this.down;
        if (baseAST == null) {
            return 0;
        }
        int i = 1;
        while (true) {
            baseAST = baseAST.right;
            if (baseAST == null) {
                return i;
            }
            i++;
        }
    }

    @Override // antlr.collections.AST
    public String getText() {
        return "";
    }

    @Override // antlr.collections.AST
    public int getType() {
        return 0;
    }

    @Override // antlr.collections.AST
    public abstract void initialize(int i, String str);

    @Override // antlr.collections.AST
    public abstract void initialize(Token token);

    @Override // antlr.collections.AST
    public abstract void initialize(AST ast);

    public void removeChildren() {
        this.down = null;
    }

    @Override // antlr.collections.AST
    public void setFirstChild(AST ast) {
        this.down = (BaseAST) ast;
    }

    @Override // antlr.collections.AST
    public void setNextSibling(AST ast) {
        this.right = (BaseAST) ast;
    }

    @Override // antlr.collections.AST
    public void setText(String str) {
    }

    @Override // antlr.collections.AST
    public void setType(int i) {
    }

    @Override // antlr.collections.AST
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (!verboseStringConversion || getText() == null || getText().equalsIgnoreCase(tokenNames[getType()]) || getText().equalsIgnoreCase(StringUtils.stripFrontBack(tokenNames[getType()], "\"", "\""))) {
            return getText();
        }
        stringBuffer.append('[');
        stringBuffer.append(getText());
        stringBuffer.append(",<");
        stringBuffer.append(tokenNames[getType()]);
        stringBuffer.append(">]");
        return stringBuffer.toString();
    }

    @Override // antlr.collections.AST
    public String toStringList() {
        StringBuilder sbM9b = C0000a.m9b(getFirstChild() != null ? C0000a.m1a("", " (") : "", " ");
        sbM9b.append(toString());
        String string = sbM9b.toString();
        if (getFirstChild() != null) {
            StringBuilder sbM5a = C0000a.m5a(string);
            sbM5a.append(((BaseAST) getFirstChild()).toStringList());
            string = sbM5a.toString();
        }
        if (getFirstChild() != null) {
            string = C0000a.m1a(string, " )");
        }
        if (getNextSibling() == null) {
            return string;
        }
        StringBuilder sbM5a2 = C0000a.m5a(string);
        sbM5a2.append(((BaseAST) getNextSibling()).toStringList());
        return sbM5a2.toString();
    }

    @Override // antlr.collections.AST
    public String toStringTree() {
        StringBuilder sbM9b = C0000a.m9b(getFirstChild() != null ? C0000a.m1a("", " (") : "", " ");
        sbM9b.append(toString());
        String string = sbM9b.toString();
        if (getFirstChild() != null) {
            StringBuilder sbM5a = C0000a.m5a(string);
            sbM5a.append(((BaseAST) getFirstChild()).toStringList());
            string = sbM5a.toString();
        }
        return getFirstChild() != null ? C0000a.m1a(string, " )") : string;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r2v1, types: [antlr.collections.AST] */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4 */
    public void xmlSerialize(Writer writer) throws IOException {
        for (?? nextSibling = this; nextSibling != 0; nextSibling = nextSibling.getNextSibling()) {
            if (nextSibling.getFirstChild() == null) {
                ((BaseAST) nextSibling).xmlSerializeNode(writer);
            } else {
                BaseAST baseAST = (BaseAST) nextSibling;
                baseAST.xmlSerializeRootOpen(writer);
                ((BaseAST) nextSibling.getFirstChild()).xmlSerialize(writer);
                baseAST.xmlSerializeRootClose(writer);
            }
        }
    }

    public void xmlSerializeNode(Writer writer) throws IOException {
        StringBuffer stringBuffer = new StringBuffer(100);
        stringBuffer.append("<");
        stringBuffer.append(getClass().getName() + " ");
        stringBuffer.append("text=\"" + encode(getText()) + "\" type=\"" + getType() + "\"/>");
        writer.write(stringBuffer.toString());
    }

    public void xmlSerializeRootClose(Writer writer) throws IOException {
        StringBuilder sbM5a = C0000a.m5a("</");
        sbM5a.append(getClass().getName());
        sbM5a.append(">\n");
        writer.write(sbM5a.toString());
    }

    public void xmlSerializeRootOpen(Writer writer) throws IOException {
        StringBuffer stringBuffer = new StringBuffer(100);
        stringBuffer.append("<");
        stringBuffer.append(getClass().getName() + " ");
        stringBuffer.append("text=\"" + encode(getText()) + "\" type=\"" + getType() + "\">\n");
        writer.write(stringBuffer.toString());
    }
}
