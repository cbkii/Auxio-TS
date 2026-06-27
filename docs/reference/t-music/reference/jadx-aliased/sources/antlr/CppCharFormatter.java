package antlr;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class CppCharFormatter implements CharFormatter {
    @Override // antlr.CharFormatter
    public String escapeChar(int i, boolean z) {
        if (i == 9) {
            return "\\t";
        }
        if (i == 10) {
            return "\\n";
        }
        if (i == 13) {
            return "\\r";
        }
        if (i == 34) {
            return "\\\"";
        }
        if (i == 39) {
            return "\\'";
        }
        if (i == 92) {
            return "\\\\";
        }
        if (i >= 32 && i <= 126) {
            return String.valueOf((char) i);
        }
        if (i <= 255) {
            StringBuilder m5a = C0000a.m5a("\\");
            m5a.append(Integer.toString(i, 8));
            return m5a.toString();
        }
        String num = Integer.toString(i, 16);
        while (num.length() < 4) {
            num = '0' + num;
        }
        return C0000a.m1a("\\u", num);
    }

    @Override // antlr.CharFormatter
    public String escapeString(String str) {
        String str2 = new String();
        for (int i = 0; i < str.length(); i++) {
            StringBuilder m5a = C0000a.m5a(str2);
            m5a.append(escapeChar(str.charAt(i), false));
            str2 = m5a.toString();
        }
        return str2;
    }

    @Override // antlr.CharFormatter
    public String literalChar(int i) {
        StringBuilder m5a = C0000a.m5a("0x");
        m5a.append(Integer.toString(i, 16));
        String sb = m5a.toString();
        if (i < 0 || i > 126) {
            return sb;
        }
        StringBuilder m9b = C0000a.m9b(sb, " /* '");
        m9b.append(escapeChar(i, true));
        m9b.append("' */ ");
        return m9b.toString();
    }

    @Override // antlr.CharFormatter
    public String literalString(String str) {
        StringBuilder m5a = C0000a.m5a("\"");
        m5a.append(escapeString(str));
        m5a.append("\"");
        return m5a.toString();
    }
}
