package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
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
            StringBuilder sbM5a = C0000a.m5a("\\");
            sbM5a.append(Integer.toString(i, 8));
            return sbM5a.toString();
        }
        String string = Integer.toString(i, 16);
        while (string.length() < 4) {
            string = '0' + string;
        }
        return C0000a.m1a("\\u", string);
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
        StringBuilder sbM5a = C0000a.m5a("0x");
        sbM5a.append(Integer.toString(i, 16));
        String string = sbM5a.toString();
        if (i < 0 || i > 126) {
            return string;
        }
        StringBuilder sbM9b = C0000a.m9b(string, " /* '");
        sbM9b.append(escapeChar(i, true));
        sbM9b.append("' */ ");
        return sbM9b.toString();
    }

    @Override // antlr.CharFormatter
    public String literalString(String str) {
        StringBuilder sbM5a = C0000a.m5a("\"");
        sbM5a.append(escapeString(str));
        sbM5a.append("\"");
        return sbM5a.toString();
    }
}
