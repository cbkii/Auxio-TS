package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class PythonCharFormatter implements CharFormatter {
    public String _escapeChar(int i, boolean z) {
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
            StringBuilder sbM5a = C0000a.m5a("\\u000");
            sbM5a.append(Integer.toString(i, 16));
            return sbM5a.toString();
        }
        if (16 <= i && i <= 255) {
            StringBuilder sbM5a2 = C0000a.m5a("\\u00");
            sbM5a2.append(Integer.toString(i, 16));
            return sbM5a2.toString();
        }
        if (256 > i || i > 4095) {
            StringBuilder sbM5a3 = C0000a.m5a("\\u");
            sbM5a3.append(Integer.toString(i, 16));
            return sbM5a3.toString();
        }
        StringBuilder sbM5a4 = C0000a.m5a("\\u0");
        sbM5a4.append(Integer.toString(i, 16));
        return sbM5a4.toString();
    }

    @Override // antlr.CharFormatter
    public String escapeChar(int i, boolean z) {
        return _escapeChar(i, z);
    }

    @Override // antlr.CharFormatter
    public String escapeString(String str) {
        String str2 = new String();
        for (int i = 0; i < str.length(); i++) {
            StringBuilder sbM5a = C0000a.m5a(str2);
            sbM5a.append(escapeChar(str.charAt(i), false));
            str2 = sbM5a.toString();
        }
        return str2;
    }

    @Override // antlr.CharFormatter
    public String literalChar(int i) {
        StringBuilder sbM5a = C0000a.m5a("");
        sbM5a.append(escapeChar(i, true));
        sbM5a.append("");
        return sbM5a.toString();
    }

    @Override // antlr.CharFormatter
    public String literalString(String str) {
        StringBuilder sbM5a = C0000a.m5a("\"");
        sbM5a.append(escapeString(str));
        sbM5a.append("\"");
        return sbM5a.toString();
    }
}
