package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class JavaCharFormatter implements CharFormatter {
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
            return z ? "\"" : "\\\"";
        }
        if (i == 39) {
            return z ? "\\'" : "'";
        }
        if (i == 92) {
            return "\\\\";
        }
        if (i >= 32 && i <= 126) {
            return String.valueOf((char) i);
        }
        if (i >= 0 && i <= 15) {
            StringBuilder m5a = C0000a.m5a("\\u000");
            m5a.append(Integer.toString(i, 16));
            return m5a.toString();
        }
        if (16 <= i && i <= 255) {
            StringBuilder m5a2 = C0000a.m5a("\\u00");
            m5a2.append(Integer.toString(i, 16));
            return m5a2.toString();
        }
        if (256 > i || i > 4095) {
            StringBuilder m5a3 = C0000a.m5a("\\u");
            m5a3.append(Integer.toString(i, 16));
            return m5a3.toString();
        }
        StringBuilder m5a4 = C0000a.m5a("\\u0");
        m5a4.append(Integer.toString(i, 16));
        return m5a4.toString();
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
        StringBuilder m5a = C0000a.m5a("'");
        m5a.append(escapeChar(i, true));
        m5a.append("'");
        return m5a.toString();
    }

    @Override // antlr.CharFormatter
    public String literalString(String str) {
        StringBuilder m5a = C0000a.m5a("\"");
        m5a.append(escapeString(str));
        m5a.append("\"");
        return m5a.toString();
    }
}
