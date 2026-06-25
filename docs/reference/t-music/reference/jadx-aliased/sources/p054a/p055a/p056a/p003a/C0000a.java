package p054a.p055a.p056a.p003a;

import antlr.ANTLRHashString;
import antlr.CSharpCodeGenerator;
import antlr.CppCodeGenerator;
import antlr.Grammar;
import antlr.Token;
import java.util.Hashtable;

/* compiled from: outline */
/* renamed from: a.a.a.a.a */
/* loaded from: classes.dex */
public class C0000a {
    /* renamed from: a */
    public static String m0a(Token token, StringBuilder sb) {
        sb.append(token.getText());
        return sb.toString();
    }

    /* renamed from: a */
    public static String m1a(String str, String str2) {
        return str + str2;
    }

    /* renamed from: a */
    public static String m2a(String str, String str2, String str3) {
        return str + str2 + str3;
    }

    /* renamed from: a */
    public static String m3a(StringBuilder sb, String str, String str2) {
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    /* renamed from: a */
    public static StringBuilder m4a(Grammar grammar, StringBuilder sb, String str) {
        sb.append(grammar.getClassName());
        sb.append(str);
        return sb;
    }

    /* renamed from: a */
    public static StringBuilder m5a(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        return sb;
    }

    /* renamed from: a */
    public static void m6a(int i, Hashtable hashtable, ANTLRHashString aNTLRHashString) {
        hashtable.put(aNTLRHashString, new Integer(i));
    }

    /* renamed from: a */
    public static void m7a(StringBuilder sb, String str, String str2, CSharpCodeGenerator cSharpCodeGenerator) {
        sb.append(str);
        sb.append(str2);
        cSharpCodeGenerator.println(sb.toString());
    }

    /* renamed from: a */
    public static void m8a(StringBuilder sb, String str, String str2, CppCodeGenerator cppCodeGenerator) {
        sb.append(str);
        sb.append(str2);
        cppCodeGenerator.println(sb.toString());
    }

    /* renamed from: b */
    public static StringBuilder m9b(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        return sb;
    }

    /* renamed from: b */
    public static StringBuilder m10b(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        return sb;
    }
}
