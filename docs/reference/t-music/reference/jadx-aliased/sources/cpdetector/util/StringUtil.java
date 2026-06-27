package cpdetector.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public final class StringUtil {
    public static StringUtil instance;

    public static final String appendSpaces(String str, int i) {
        StringBuffer stringBuffer = new StringBuffer(str);
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append(" ");
        }
        return stringBuffer.toString();
    }

    public static final String arrayToString(Object obj) {
        return arrayToString(obj, ",");
    }

    public static final String arrayToString(Object obj, String str) {
        if (obj == null) {
            return "null";
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            int length = Array.getLength(obj);
            stringBuffer.append("[");
            for (int i = 0; i < length; i++) {
                Object obj2 = Array.get(obj, i);
                if (obj2 == null) {
                    stringBuffer.append("null");
                } else {
                    stringBuffer.append(obj2.toString());
                }
                if (i < length - 1) {
                    stringBuffer.append(str);
                }
            }
            stringBuffer.append("]");
            return stringBuffer.toString();
        } catch (ArrayIndexOutOfBoundsException unused) {
            stringBuffer.append("]");
            return stringBuffer.toString();
        } catch (IllegalArgumentException unused2) {
            return obj.toString();
        }
    }

    public static String getNewLine() {
        return System.getProperty("line.separator");
    }

    public static StringUtil instance() {
        if (instance == null) {
            instance = new StringUtil();
        }
        return instance;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static final void listOfArraysToString(List<Object> list) {
        if (list == null) {
            return;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            list.add(i, arrayToString(list.remove(i)));
        }
    }

    public static final int longestStringRepresentation(List<Object> list) {
        if (list == null) {
            return 0;
        }
        int size = list.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            int length = arrayToString(list.get(i2)).length();
            if (length > i) {
                i = length;
            }
        }
        return i;
    }

    public static Map.Entry<String, String> prefixIntersection(String str, String str2) {
        String str3 = "";
        if (str2.indexOf(str) != -1) {
            str3 = str2.substring(str.length());
        } else {
            str = "";
        }
        return new Entry(str, str3);
    }

    public static final String setSize(String str, int i) {
        int length = str.length();
        if (length <= i) {
            return appendSpaces(str, i - length);
        }
        System.err.println("greenpeace.util.setSize(String s,int length): length (" + i + ") is smaller than s.length(" + length + ") : " + str);
        return str;
    }

    public static final void toLongestString(List<Object> list) {
        if (list == null) {
            return;
        }
        int size = list.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            arrayToString(list.get(i2));
            String str = (String) list.get(i2);
            int length = str.length();
            if (length > i) {
                i = length;
            }
            list.add(i2, str);
        }
        for (int i3 = 0; i3 < size; i3++) {
            list.add(i3, setSize((String) list.remove(i3), i));
        }
    }
}
