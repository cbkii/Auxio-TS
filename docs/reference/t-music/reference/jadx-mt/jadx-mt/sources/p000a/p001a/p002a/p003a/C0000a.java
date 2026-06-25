package p000a.p001a.p002a.p003a;

import antlr.ANTLRHashString;
import antlr.CSharpCodeGenerator;
import antlr.CppCodeGenerator;
import antlr.Grammar;
import antlr.Token;
import java.util.Hashtable;

/* JADX INFO: renamed from: a.a.a.a.a */
/* JADX INFO: compiled from: outline */
/* JADX INFO: loaded from: classes.dex */
public class C0000a {
    /* JADX INFO: renamed from: a */
    public static String m0a(Token token, StringBuilder sb) {
        sb.append(token.getText());
        return sb.toString();
    }

    /* JADX INFO: renamed from: a */
    public static String m1a(String str, String str2) {
        return str + str2;
    }

    /* JADX INFO: renamed from: a */
    public static String m2a(String str, String str2, String str3) {
        return str + str2 + str3;
    }

    /* JADX INFO: renamed from: a */
    public static String m3a(StringBuilder sb, String str, String str2) {
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    /* JADX INFO: renamed from: a */
    public static StringBuilder m4a(Grammar grammar, StringBuilder sb, String str) {
        sb.append(grammar.getClassName());
        sb.append(str);
        return sb;
    }

    /* JADX INFO: renamed from: a */
    public static StringBuilder m5a(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        return sb;
    }

    /* JADX INFO: renamed from: a */
    public static void m6a(int i, Hashtable hashtable, ANTLRHashString aNTLRHashString) {
        hashtable.put(aNTLRHashString, new Integer(i));
    }

    /* JADX INFO: renamed from: a */
    public static void m7a(StringBuilder sb, String str, String str2, CSharpCodeGenerator cSharpCodeGenerator) {
        sb.append(str);
        sb.append(str2);
        cSharpCodeGenerator.println(sb.toString());
    }

    /* JADX INFO: renamed from: a */
    public static void m8a(StringBuilder sb, String str, String str2, CppCodeGenerator cppCodeGenerator) {
        sb.append(str);
        sb.append(str2);
        cppCodeGenerator.println(sb.toString());
    }

    /* JADX INFO: renamed from: b */
    public static StringBuilder m9b(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        return sb;
    }

    /* JADX INFO: renamed from: b */
    public static StringBuilder m10b(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        return sb;
    }
}
