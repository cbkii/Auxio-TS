package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class StringUtils {
    public static String stripBack(String str, char c2) {
        while (str.length() > 0 && str.charAt(str.length() - 1) == c2) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String stripBack(String str, String str2) {
        while (true) {
            String strSubstring = str;
            boolean z = false;
            for (int i = 0; i < str2.length(); i++) {
                char cCharAt = str2.charAt(i);
                while (strSubstring.length() > 0 && strSubstring.charAt(strSubstring.length() - 1) == cCharAt) {
                    strSubstring = strSubstring.substring(0, strSubstring.length() - 1);
                    z = true;
                }
            }
            if (!z) {
                return strSubstring;
            }
            str = strSubstring;
        }
    }

    public static String stripFront(String str, char c2) {
        while (str.length() > 0 && str.charAt(0) == c2) {
            str = str.substring(1);
        }
        return str;
    }

    public static String stripFront(String str, String str2) {
        while (true) {
            String strSubstring = str;
            boolean z = false;
            for (int i = 0; i < str2.length(); i++) {
                char cCharAt = str2.charAt(i);
                while (strSubstring.length() > 0 && strSubstring.charAt(0) == cCharAt) {
                    strSubstring = strSubstring.substring(1);
                    z = true;
                }
            }
            if (!z) {
                return strSubstring;
            }
            str = strSubstring;
        }
    }

    public static String stripFrontBack(String str, String str2, String str3) {
        int iIndexOf = str.indexOf(str2);
        int iLastIndexOf = str.lastIndexOf(str3);
        return (iIndexOf == -1 || iLastIndexOf == -1) ? str : str.substring(iIndexOf + 1, iLastIndexOf);
    }
}
